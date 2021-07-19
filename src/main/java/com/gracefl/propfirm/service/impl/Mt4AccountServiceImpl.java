package com.gracefl.propfirm.service.impl;

import com.gracefl.propfirm.domain.Mt4Account;
import com.gracefl.propfirm.repository.Mt4AccountRepository;
import com.gracefl.propfirm.service.Mt4AccountService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Mt4Account}.
 */
@Service
@Transactional
public class Mt4AccountServiceImpl implements Mt4AccountService {

    private final Logger log = LoggerFactory.getLogger(Mt4AccountServiceImpl.class);

    private final Mt4AccountRepository mt4AccountRepository;

    public Mt4AccountServiceImpl(Mt4AccountRepository mt4AccountRepository) {
        this.mt4AccountRepository = mt4AccountRepository;
    }

    @Override
    public Mt4Account save(Mt4Account mt4Account) {
        log.debug("Request to save Mt4Account : {}", mt4Account);
        return mt4AccountRepository.save(mt4Account);
    }

    @Override
    public Optional<Mt4Account> partialUpdate(Mt4Account mt4Account) {
        log.debug("Request to partially update Mt4Account : {}", mt4Account);

        return mt4AccountRepository
            .findById(mt4Account.getId())
            .map(
                existingMt4Account -> {
                    if (mt4Account.getAccountType() != null) {
                        existingMt4Account.setAccountType(mt4Account.getAccountType());
                    }
                    if (mt4Account.getAccountBroker() != null) {
                        existingMt4Account.setAccountBroker(mt4Account.getAccountBroker());
                    }
                    if (mt4Account.getAccountLogin() != null) {
                        existingMt4Account.setAccountLogin(mt4Account.getAccountLogin());
                    }
                    if (mt4Account.getAccountPassword() != null) {
                        existingMt4Account.setAccountPassword(mt4Account.getAccountPassword());
                    }
                    if (mt4Account.getAccountInvestorLogin() != null) {
                        existingMt4Account.setAccountInvestorLogin(mt4Account.getAccountInvestorLogin());
                    }
                    if (mt4Account.getAccountInvestorPassword() != null) {
                        existingMt4Account.setAccountInvestorPassword(mt4Account.getAccountInvestorPassword());
                    }
                    if (mt4Account.getAccountReal() != null) {
                        existingMt4Account.setAccountReal(mt4Account.getAccountReal());
                    }
                    if (mt4Account.getAccountInfoDouble() != null) {
                        existingMt4Account.setAccountInfoDouble(mt4Account.getAccountInfoDouble());
                    }
                    if (mt4Account.getAccountInfoInteger() != null) {
                        existingMt4Account.setAccountInfoInteger(mt4Account.getAccountInfoInteger());
                    }
                    if (mt4Account.getAccountInfoString() != null) {
                        existingMt4Account.setAccountInfoString(mt4Account.getAccountInfoString());
                    }
                    if (mt4Account.getAccountBalance() != null) {
                        existingMt4Account.setAccountBalance(mt4Account.getAccountBalance());
                    }
                    if (mt4Account.getAccountCredit() != null) {
                        existingMt4Account.setAccountCredit(mt4Account.getAccountCredit());
                    }
                    if (mt4Account.getAccountCompany() != null) {
                        existingMt4Account.setAccountCompany(mt4Account.getAccountCompany());
                    }
                    if (mt4Account.getAccountCurrency() != null) {
                        existingMt4Account.setAccountCurrency(mt4Account.getAccountCurrency());
                    }
                    if (mt4Account.getAccountEquity() != null) {
                        existingMt4Account.setAccountEquity(mt4Account.getAccountEquity());
                    }
                    if (mt4Account.getAccountFreeMargin() != null) {
                        existingMt4Account.setAccountFreeMargin(mt4Account.getAccountFreeMargin());
                    }
                    if (mt4Account.getAccountFreeMarginCheck() != null) {
                        existingMt4Account.setAccountFreeMarginCheck(mt4Account.getAccountFreeMarginCheck());
                    }
                    if (mt4Account.getAccountFreeMarginMode() != null) {
                        existingMt4Account.setAccountFreeMarginMode(mt4Account.getAccountFreeMarginMode());
                    }
                    if (mt4Account.getAccountLeverage() != null) {
                        existingMt4Account.setAccountLeverage(mt4Account.getAccountLeverage());
                    }
                    if (mt4Account.getAccountMargin() != null) {
                        existingMt4Account.setAccountMargin(mt4Account.getAccountMargin());
                    }
                    if (mt4Account.getAccountName() != null) {
                        existingMt4Account.setAccountName(mt4Account.getAccountName());
                    }
                    if (mt4Account.getAccountNumber() != null) {
                        existingMt4Account.setAccountNumber(mt4Account.getAccountNumber());
                    }
                    if (mt4Account.getAccountProfit() != null) {
                        existingMt4Account.setAccountProfit(mt4Account.getAccountProfit());
                    }
                    if (mt4Account.getAccountServer() != null) {
                        existingMt4Account.setAccountServer(mt4Account.getAccountServer());
                    }
                    if (mt4Account.getAccountStopoutLevel() != null) {
                        existingMt4Account.setAccountStopoutLevel(mt4Account.getAccountStopoutLevel());
                    }
                    if (mt4Account.getAccountStopoutMode() != null) {
                        existingMt4Account.setAccountStopoutMode(mt4Account.getAccountStopoutMode());
                    }
                    if (mt4Account.getInActive() != null) {
                        existingMt4Account.setInActive(mt4Account.getInActive());
                    }
                    if (mt4Account.getInActiveDate() != null) {
                        existingMt4Account.setInActiveDate(mt4Account.getInActiveDate());
                    }

                    return existingMt4Account;
                }
            )
            .map(mt4AccountRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Mt4Account> findAll(Pageable pageable) {
        log.debug("Request to get all Mt4Accounts");
        return mt4AccountRepository.findAll(pageable);
    }

    /**
     *  Get all the mt4Accounts where TradeChallenge is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Mt4Account> findAllWhereTradeChallengeIsNull() {
        log.debug("Request to get all mt4Accounts where TradeChallenge is null");
        return StreamSupport
            .stream(mt4AccountRepository.findAll().spliterator(), false)
            .filter(mt4Account -> mt4Account.getTradeChallenge() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Mt4Account> findOne(Long id) {
        log.debug("Request to get Mt4Account : {}", id);
        return mt4AccountRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Mt4Account : {}", id);
        mt4AccountRepository.deleteById(id);
    }
}
