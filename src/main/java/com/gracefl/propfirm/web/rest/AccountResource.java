package com.gracefl.propfirm.web.rest;

import com.gracefl.propfirm.domain.AccountDataTimeSeries;
import com.gracefl.propfirm.domain.ChallengeType;
import com.gracefl.propfirm.domain.Mt4Account;
import com.gracefl.propfirm.domain.SiteAccount;
import com.gracefl.propfirm.domain.TradeChallenge;
import com.gracefl.propfirm.domain.User;
import com.gracefl.propfirm.domain.enumeration.BROKER;
import com.gracefl.propfirm.repository.UserRepository;
import com.gracefl.propfirm.security.SecurityUtils;
import com.gracefl.propfirm.service.AccountDataTimeSeriesService;
import com.gracefl.propfirm.service.ChallengeTypeService;
import com.gracefl.propfirm.service.MailService;
import com.gracefl.propfirm.service.Mt4AccountService;
import com.gracefl.propfirm.service.SiteAccountService;
import com.gracefl.propfirm.service.TradeChallengeService;
import com.gracefl.propfirm.service.UserService;
import com.gracefl.propfirm.service.dto.AdminUserDTO;
import com.gracefl.propfirm.service.dto.PasswordChangeDTO;
import com.gracefl.propfirm.service.dto.UserDTO;
import com.gracefl.propfirm.web.rest.errors.*;
import com.gracefl.propfirm.web.rest.vm.KeyAndPasswordVM;
import com.gracefl.propfirm.web.rest.vm.ManagedUserVM;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;
    
    private final SiteAccountService siteAccountService;
    
    private final Mt4AccountService mt4AccountService;
    
    private final TradeChallengeService tradeChallengeService;
    
    private final ChallengeTypeService challengeTypeService;
    
    private final AccountDataTimeSeriesService accountDataTimeSeriesService;
    
    private final MailService mailService;

    public AccountResource(UserRepository userRepository, UserService userService, MailService mailService, SiteAccountService siteAccountService,
    		Mt4AccountService mt4AccountService, TradeChallengeService tradeChallengeService, ChallengeTypeService challengeTypeService,
    		AccountDataTimeSeriesService accountDataTimeSeriesService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.siteAccountService = siteAccountService;
        this.mt4AccountService = mt4AccountService;
        this.tradeChallengeService = tradeChallengeService;
        this.challengeTypeService = challengeTypeService;
        this.accountDataTimeSeriesService = accountDataTimeSeriesService;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (isPasswordLengthInvalid(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        
        Optional<ChallengeType> challengeType = challengeTypeService.findOne(1L);
        
        if (challengeType.isPresent()) {
	        // next create a SiteAccount entity
	        SiteAccount siteAccount = new SiteAccount();
	        siteAccount.setUser(user);
	        siteAccount.setAccountName(user.getEmail());
	        siteAccount.setInActive(false);
	        siteAccountService.save(siteAccount);
	        
	        // next create a Mt4Account entity
	        Mt4Account mt4Account = new Mt4Account();
	        mt4Account.setAccountLogin(user.getEmail() + "_1");
	        mt4Account.setInActive(false);
	        mt4Account.setAccountBroker(BROKER.FXPRO);
	        mt4Account.setAccountInfoString("NEW ACCOUNT:" + user.getEmail());
	        mt4Account.setAccountBalance(challengeType.get().getAccountSize().doubleValue());
	        mt4Account.setAccountEquity(challengeType.get().getAccountSize().doubleValue());
	        mt4AccountService.save(mt4Account);
	        
	        // then create a TradeChallenge entity with the right challenge type from the 
	        Calendar cal = Calendar.getInstance();
	        String month = new SimpleDateFormat("MMM").format(cal.getTime());
	        String year = new SimpleDateFormat("YYYY").format(cal.getTime());
	        
	        TradeChallenge tradeChallenge = new TradeChallenge();
	        tradeChallenge.setMt4Account(mt4Account);
	        tradeChallenge.setRunningMaxDailyDrawdown(0D);
	        tradeChallenge.setRunningMaxTotalDrawdown(0D);
	        tradeChallenge.setStartDate(Instant.now());
	        tradeChallenge.setTradeChallengeName(challengeType.get() + " " + month + " " + year);
	        tradeChallenge.setRulesViolated(false);
	        tradeChallenge.setSiteAccount(siteAccount);
	        tradeChallenge.setChallengeType(challengeType.get());
	        tradeChallengeService.save(tradeChallenge);
	        
	        AccountDataTimeSeries accountDataTimeSeries = new AccountDataTimeSeries();
	        accountDataTimeSeries.setAccountBalance(tradeChallenge.getChallengeType().getAccountSize().doubleValue());
	        accountDataTimeSeries.setAccountEquity(tradeChallenge.getChallengeType().getAccountSize().doubleValue());
	        accountDataTimeSeries.setMt4Account(mt4Account);
	        accountDataTimeSeries.setDateStamp(Instant.now());
	        accountDataTimeSeriesService.save(accountDataTimeSeries);
	        
	        mt4Account.addAccountDataTimeSeries(accountDataTimeSeries);
	        
	        mt4Account.setTradeChallenge(tradeChallenge);
	        mt4AccountService.save(mt4Account);
	        
	        mailService.sendActivationEmail(user);
        } else {
        	// TODO send a mail to the ADMIN account because a sign up error occurred
        	// send a mail to the ADMIN account because a sign up error occurred
        }
        
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public AdminUserDTO getAccount() {
        return userService
            .getUserWithAuthorities()
            .map(AdminUserDTO::new)
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody AdminUserDTO userDTO) {
        String userLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(
            userDTO.getFirstName(),
            userDTO.getLastName(),
            userDTO.getEmail(),
            userDTO.getLangKey(),
            userDTO.getImageUrl()
        );
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
        Optional<User> user = userService.requestPasswordReset(mail);
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.get());
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            log.warn("Password reset requested for non existing mail");
        }
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (isPasswordLengthInvalid(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
            password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
            password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }
}
