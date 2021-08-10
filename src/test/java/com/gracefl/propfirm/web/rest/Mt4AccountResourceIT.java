package com.gracefl.propfirm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gracefl.propfirm.IntegrationTest;
import com.gracefl.propfirm.domain.AccountDataTimeSeries;
import com.gracefl.propfirm.domain.Mt4Account;
import com.gracefl.propfirm.domain.Mt4Trade;
import com.gracefl.propfirm.domain.TradeChallenge;
import com.gracefl.propfirm.domain.enumeration.ACCOUNTTYPE;
import com.gracefl.propfirm.domain.enumeration.BROKER;
import com.gracefl.propfirm.repository.Mt4AccountRepository;
import com.gracefl.propfirm.service.criteria.Mt4AccountCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link Mt4AccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class Mt4AccountResourceIT {

    private static final ACCOUNTTYPE DEFAULT_ACCOUNT_TYPE = ACCOUNTTYPE.REAL;
    private static final ACCOUNTTYPE UPDATED_ACCOUNT_TYPE = ACCOUNTTYPE.DEMO;

    private static final BROKER DEFAULT_ACCOUNT_BROKER = BROKER.FXPRO;
    private static final BROKER UPDATED_ACCOUNT_BROKER = BROKER.ALPARI;

    private static final String DEFAULT_ACCOUNT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_INVESTOR_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_INVESTOR_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_INVESTOR_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_INVESTOR_PASSWORD = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACCOUNT_REAL = false;
    private static final Boolean UPDATED_ACCOUNT_REAL = true;

    private static final Double DEFAULT_ACCOUNT_INFO_DOUBLE = 1D;
    private static final Double UPDATED_ACCOUNT_INFO_DOUBLE = 2D;
    private static final Double SMALLER_ACCOUNT_INFO_DOUBLE = 1D - 1D;

    private static final Integer DEFAULT_ACCOUNT_INFO_INTEGER = 1;
    private static final Integer UPDATED_ACCOUNT_INFO_INTEGER = 2;
    private static final Integer SMALLER_ACCOUNT_INFO_INTEGER = 1 - 1;

    private static final String DEFAULT_ACCOUNT_INFO_STRING = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_INFO_STRING = "BBBBBBBBBB";

    private static final Double DEFAULT_ACCOUNT_BALANCE = 1D;
    private static final Double UPDATED_ACCOUNT_BALANCE = 2D;
    private static final Double SMALLER_ACCOUNT_BALANCE = 1D - 1D;

    private static final Double DEFAULT_ACCOUNT_CREDIT = 1D;
    private static final Double UPDATED_ACCOUNT_CREDIT = 2D;
    private static final Double SMALLER_ACCOUNT_CREDIT = 1D - 1D;

    private static final String DEFAULT_ACCOUNT_COMPANY = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_COMPANY = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_CURRENCY = "BBBBBBBBBB";

    private static final Double DEFAULT_ACCOUNT_EQUITY = 1D;
    private static final Double UPDATED_ACCOUNT_EQUITY = 2D;
    private static final Double SMALLER_ACCOUNT_EQUITY = 1D - 1D;

    private static final Double DEFAULT_ACCOUNT_FREE_MARGIN = 1D;
    private static final Double UPDATED_ACCOUNT_FREE_MARGIN = 2D;
    private static final Double SMALLER_ACCOUNT_FREE_MARGIN = 1D - 1D;

    private static final Double DEFAULT_ACCOUNT_FREE_MARGIN_CHECK = 1D;
    private static final Double UPDATED_ACCOUNT_FREE_MARGIN_CHECK = 2D;
    private static final Double SMALLER_ACCOUNT_FREE_MARGIN_CHECK = 1D - 1D;

    private static final Double DEFAULT_ACCOUNT_FREE_MARGIN_MODE = 1D;
    private static final Double UPDATED_ACCOUNT_FREE_MARGIN_MODE = 2D;
    private static final Double SMALLER_ACCOUNT_FREE_MARGIN_MODE = 1D - 1D;

    private static final Integer DEFAULT_ACCOUNT_LEVERAGE = 1;
    private static final Integer UPDATED_ACCOUNT_LEVERAGE = 2;
    private static final Integer SMALLER_ACCOUNT_LEVERAGE = 1 - 1;

    private static final Double DEFAULT_ACCOUNT_MARGIN = 1D;
    private static final Double UPDATED_ACCOUNT_MARGIN = 2D;
    private static final Double SMALLER_ACCOUNT_MARGIN = 1D - 1D;

    private static final String DEFAULT_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ACCOUNT_NUMBER = 1;
    private static final Integer UPDATED_ACCOUNT_NUMBER = 2;
    private static final Integer SMALLER_ACCOUNT_NUMBER = 1 - 1;

    private static final Double DEFAULT_ACCOUNT_PROFIT = 1D;
    private static final Double UPDATED_ACCOUNT_PROFIT = 2D;
    private static final Double SMALLER_ACCOUNT_PROFIT = 1D - 1D;

    private static final String DEFAULT_ACCOUNT_SERVER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_SERVER = "BBBBBBBBBB";

    private static final Integer DEFAULT_ACCOUNT_STOPOUT_LEVEL = 1;
    private static final Integer UPDATED_ACCOUNT_STOPOUT_LEVEL = 2;
    private static final Integer SMALLER_ACCOUNT_STOPOUT_LEVEL = 1 - 1;

    private static final Integer DEFAULT_ACCOUNT_STOPOUT_MODE = 1;
    private static final Integer UPDATED_ACCOUNT_STOPOUT_MODE = 2;
    private static final Integer SMALLER_ACCOUNT_STOPOUT_MODE = 1 - 1;

    private static final Boolean DEFAULT_IN_ACTIVE = false;
    private static final Boolean UPDATED_IN_ACTIVE = true;

    private static final Instant DEFAULT_IN_ACTIVE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_IN_ACTIVE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/mt-4-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private Mt4AccountRepository mt4AccountRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMt4AccountMockMvc;

    private Mt4Account mt4Account;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mt4Account createEntity(EntityManager em) {
        Mt4Account mt4Account = new Mt4Account()
            .accountType(DEFAULT_ACCOUNT_TYPE)
            .accountBroker(DEFAULT_ACCOUNT_BROKER)
            .accountLogin(DEFAULT_ACCOUNT_LOGIN)
            .accountPassword(DEFAULT_ACCOUNT_PASSWORD)
            .accountInvestorLogin(DEFAULT_ACCOUNT_INVESTOR_LOGIN)
            .accountInvestorPassword(DEFAULT_ACCOUNT_INVESTOR_PASSWORD)
            .accountReal(DEFAULT_ACCOUNT_REAL)
            .accountInfoDouble(DEFAULT_ACCOUNT_INFO_DOUBLE)
            .accountInfoInteger(DEFAULT_ACCOUNT_INFO_INTEGER)
            .accountInfoString(DEFAULT_ACCOUNT_INFO_STRING)
            .accountBalance(DEFAULT_ACCOUNT_BALANCE)
            .accountCredit(DEFAULT_ACCOUNT_CREDIT)
            .accountCompany(DEFAULT_ACCOUNT_COMPANY)
            .accountCurrency(DEFAULT_ACCOUNT_CURRENCY)
            .accountEquity(DEFAULT_ACCOUNT_EQUITY)
            .accountFreeMargin(DEFAULT_ACCOUNT_FREE_MARGIN)
            .accountFreeMarginCheck(DEFAULT_ACCOUNT_FREE_MARGIN_CHECK)
            .accountFreeMarginMode(DEFAULT_ACCOUNT_FREE_MARGIN_MODE)
            .accountLeverage(DEFAULT_ACCOUNT_LEVERAGE)
            .accountMargin(DEFAULT_ACCOUNT_MARGIN)
            .accountName(DEFAULT_ACCOUNT_NAME)
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .accountProfit(DEFAULT_ACCOUNT_PROFIT)
            .accountServer(DEFAULT_ACCOUNT_SERVER)
            .accountStopoutLevel(DEFAULT_ACCOUNT_STOPOUT_LEVEL)
            .accountStopoutMode(DEFAULT_ACCOUNT_STOPOUT_MODE)
            .inActive(DEFAULT_IN_ACTIVE)
            .inActiveDate(DEFAULT_IN_ACTIVE_DATE);
        return mt4Account;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mt4Account createUpdatedEntity(EntityManager em) {
        Mt4Account mt4Account = new Mt4Account()
            .accountType(UPDATED_ACCOUNT_TYPE)
            .accountBroker(UPDATED_ACCOUNT_BROKER)
            .accountLogin(UPDATED_ACCOUNT_LOGIN)
            .accountPassword(UPDATED_ACCOUNT_PASSWORD)
            .accountInvestorLogin(UPDATED_ACCOUNT_INVESTOR_LOGIN)
            .accountInvestorPassword(UPDATED_ACCOUNT_INVESTOR_PASSWORD)
            .accountReal(UPDATED_ACCOUNT_REAL)
            .accountInfoDouble(UPDATED_ACCOUNT_INFO_DOUBLE)
            .accountInfoInteger(UPDATED_ACCOUNT_INFO_INTEGER)
            .accountInfoString(UPDATED_ACCOUNT_INFO_STRING)
            .accountBalance(UPDATED_ACCOUNT_BALANCE)
            .accountCredit(UPDATED_ACCOUNT_CREDIT)
            .accountCompany(UPDATED_ACCOUNT_COMPANY)
            .accountCurrency(UPDATED_ACCOUNT_CURRENCY)
            .accountEquity(UPDATED_ACCOUNT_EQUITY)
            .accountFreeMargin(UPDATED_ACCOUNT_FREE_MARGIN)
            .accountFreeMarginCheck(UPDATED_ACCOUNT_FREE_MARGIN_CHECK)
            .accountFreeMarginMode(UPDATED_ACCOUNT_FREE_MARGIN_MODE)
            .accountLeverage(UPDATED_ACCOUNT_LEVERAGE)
            .accountMargin(UPDATED_ACCOUNT_MARGIN)
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .accountProfit(UPDATED_ACCOUNT_PROFIT)
            .accountServer(UPDATED_ACCOUNT_SERVER)
            .accountStopoutLevel(UPDATED_ACCOUNT_STOPOUT_LEVEL)
            .accountStopoutMode(UPDATED_ACCOUNT_STOPOUT_MODE)
            .inActive(UPDATED_IN_ACTIVE)
            .inActiveDate(UPDATED_IN_ACTIVE_DATE);
        return mt4Account;
    }

    @BeforeEach
    public void initTest() {
        mt4Account = createEntity(em);
    }

    @Test
    @Transactional
    void createMt4Account() throws Exception {
        int databaseSizeBeforeCreate = mt4AccountRepository.findAll().size();
        // Create the Mt4Account
        restMt4AccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mt4Account)))
            .andExpect(status().isCreated());

        // Validate the Mt4Account in the database
        List<Mt4Account> mt4AccountList = mt4AccountRepository.findAll();
        assertThat(mt4AccountList).hasSize(databaseSizeBeforeCreate + 1);
        Mt4Account testMt4Account = mt4AccountList.get(mt4AccountList.size() - 1);
        assertThat(testMt4Account.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);
        assertThat(testMt4Account.getAccountBroker()).isEqualTo(DEFAULT_ACCOUNT_BROKER);
        assertThat(testMt4Account.getAccountLogin()).isEqualTo(DEFAULT_ACCOUNT_LOGIN);
        assertThat(testMt4Account.getAccountPassword()).isEqualTo(DEFAULT_ACCOUNT_PASSWORD);
        assertThat(testMt4Account.getAccountInvestorLogin()).isEqualTo(DEFAULT_ACCOUNT_INVESTOR_LOGIN);
        assertThat(testMt4Account.getAccountInvestorPassword()).isEqualTo(DEFAULT_ACCOUNT_INVESTOR_PASSWORD);
        assertThat(testMt4Account.getAccountReal()).isEqualTo(DEFAULT_ACCOUNT_REAL);
        assertThat(testMt4Account.getAccountInfoDouble()).isEqualTo(DEFAULT_ACCOUNT_INFO_DOUBLE);
        assertThat(testMt4Account.getAccountInfoInteger()).isEqualTo(DEFAULT_ACCOUNT_INFO_INTEGER);
        assertThat(testMt4Account.getAccountInfoString()).isEqualTo(DEFAULT_ACCOUNT_INFO_STRING);
        assertThat(testMt4Account.getAccountBalance()).isEqualTo(DEFAULT_ACCOUNT_BALANCE);
        assertThat(testMt4Account.getAccountCredit()).isEqualTo(DEFAULT_ACCOUNT_CREDIT);
        assertThat(testMt4Account.getAccountCompany()).isEqualTo(DEFAULT_ACCOUNT_COMPANY);
        assertThat(testMt4Account.getAccountCurrency()).isEqualTo(DEFAULT_ACCOUNT_CURRENCY);
        assertThat(testMt4Account.getAccountEquity()).isEqualTo(DEFAULT_ACCOUNT_EQUITY);
        assertThat(testMt4Account.getAccountFreeMargin()).isEqualTo(DEFAULT_ACCOUNT_FREE_MARGIN);
        assertThat(testMt4Account.getAccountFreeMarginCheck()).isEqualTo(DEFAULT_ACCOUNT_FREE_MARGIN_CHECK);
        assertThat(testMt4Account.getAccountFreeMarginMode()).isEqualTo(DEFAULT_ACCOUNT_FREE_MARGIN_MODE);
        assertThat(testMt4Account.getAccountLeverage()).isEqualTo(DEFAULT_ACCOUNT_LEVERAGE);
        assertThat(testMt4Account.getAccountMargin()).isEqualTo(DEFAULT_ACCOUNT_MARGIN);
        assertThat(testMt4Account.getAccountName()).isEqualTo(DEFAULT_ACCOUNT_NAME);
        assertThat(testMt4Account.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testMt4Account.getAccountProfit()).isEqualTo(DEFAULT_ACCOUNT_PROFIT);
        assertThat(testMt4Account.getAccountServer()).isEqualTo(DEFAULT_ACCOUNT_SERVER);
        assertThat(testMt4Account.getAccountStopoutLevel()).isEqualTo(DEFAULT_ACCOUNT_STOPOUT_LEVEL);
        assertThat(testMt4Account.getAccountStopoutMode()).isEqualTo(DEFAULT_ACCOUNT_STOPOUT_MODE);
        assertThat(testMt4Account.getInActive()).isEqualTo(DEFAULT_IN_ACTIVE);
        assertThat(testMt4Account.getInActiveDate()).isEqualTo(DEFAULT_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void createMt4AccountWithExistingId() throws Exception {
        // Create the Mt4Account with an existing ID
        mt4Account.setId(1L);

        int databaseSizeBeforeCreate = mt4AccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMt4AccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mt4Account)))
            .andExpect(status().isBadRequest());

        // Validate the Mt4Account in the database
        List<Mt4Account> mt4AccountList = mt4AccountRepository.findAll();
        assertThat(mt4AccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMt4Accounts() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList
        restMt4AccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mt4Account.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].accountBroker").value(hasItem(DEFAULT_ACCOUNT_BROKER.toString())))
            .andExpect(jsonPath("$.[*].accountLogin").value(hasItem(DEFAULT_ACCOUNT_LOGIN)))
            .andExpect(jsonPath("$.[*].accountPassword").value(hasItem(DEFAULT_ACCOUNT_PASSWORD)))
            .andExpect(jsonPath("$.[*].accountInvestorLogin").value(hasItem(DEFAULT_ACCOUNT_INVESTOR_LOGIN)))
            .andExpect(jsonPath("$.[*].accountInvestorPassword").value(hasItem(DEFAULT_ACCOUNT_INVESTOR_PASSWORD)))
            .andExpect(jsonPath("$.[*].accountReal").value(hasItem(DEFAULT_ACCOUNT_REAL.booleanValue())))
            .andExpect(jsonPath("$.[*].accountInfoDouble").value(hasItem(DEFAULT_ACCOUNT_INFO_DOUBLE.doubleValue())))
            .andExpect(jsonPath("$.[*].accountInfoInteger").value(hasItem(DEFAULT_ACCOUNT_INFO_INTEGER)))
            .andExpect(jsonPath("$.[*].accountInfoString").value(hasItem(DEFAULT_ACCOUNT_INFO_STRING)))
            .andExpect(jsonPath("$.[*].accountBalance").value(hasItem(DEFAULT_ACCOUNT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].accountCredit").value(hasItem(DEFAULT_ACCOUNT_CREDIT.doubleValue())))
            .andExpect(jsonPath("$.[*].accountCompany").value(hasItem(DEFAULT_ACCOUNT_COMPANY)))
            .andExpect(jsonPath("$.[*].accountCurrency").value(hasItem(DEFAULT_ACCOUNT_CURRENCY)))
            .andExpect(jsonPath("$.[*].accountEquity").value(hasItem(DEFAULT_ACCOUNT_EQUITY.doubleValue())))
            .andExpect(jsonPath("$.[*].accountFreeMargin").value(hasItem(DEFAULT_ACCOUNT_FREE_MARGIN.doubleValue())))
            .andExpect(jsonPath("$.[*].accountFreeMarginCheck").value(hasItem(DEFAULT_ACCOUNT_FREE_MARGIN_CHECK.doubleValue())))
            .andExpect(jsonPath("$.[*].accountFreeMarginMode").value(hasItem(DEFAULT_ACCOUNT_FREE_MARGIN_MODE.doubleValue())))
            .andExpect(jsonPath("$.[*].accountLeverage").value(hasItem(DEFAULT_ACCOUNT_LEVERAGE)))
            .andExpect(jsonPath("$.[*].accountMargin").value(hasItem(DEFAULT_ACCOUNT_MARGIN.doubleValue())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].accountProfit").value(hasItem(DEFAULT_ACCOUNT_PROFIT.doubleValue())))
            .andExpect(jsonPath("$.[*].accountServer").value(hasItem(DEFAULT_ACCOUNT_SERVER)))
            .andExpect(jsonPath("$.[*].accountStopoutLevel").value(hasItem(DEFAULT_ACCOUNT_STOPOUT_LEVEL)))
            .andExpect(jsonPath("$.[*].accountStopoutMode").value(hasItem(DEFAULT_ACCOUNT_STOPOUT_MODE)))
            .andExpect(jsonPath("$.[*].inActive").value(hasItem(DEFAULT_IN_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].inActiveDate").value(hasItem(DEFAULT_IN_ACTIVE_DATE.toString())));
    }

    @Test
    @Transactional
    void getMt4Account() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get the mt4Account
        restMt4AccountMockMvc
            .perform(get(ENTITY_API_URL_ID, mt4Account.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mt4Account.getId().intValue()))
            .andExpect(jsonPath("$.accountType").value(DEFAULT_ACCOUNT_TYPE.toString()))
            .andExpect(jsonPath("$.accountBroker").value(DEFAULT_ACCOUNT_BROKER.toString()))
            .andExpect(jsonPath("$.accountLogin").value(DEFAULT_ACCOUNT_LOGIN))
            .andExpect(jsonPath("$.accountPassword").value(DEFAULT_ACCOUNT_PASSWORD))
            .andExpect(jsonPath("$.accountInvestorLogin").value(DEFAULT_ACCOUNT_INVESTOR_LOGIN))
            .andExpect(jsonPath("$.accountInvestorPassword").value(DEFAULT_ACCOUNT_INVESTOR_PASSWORD))
            .andExpect(jsonPath("$.accountReal").value(DEFAULT_ACCOUNT_REAL.booleanValue()))
            .andExpect(jsonPath("$.accountInfoDouble").value(DEFAULT_ACCOUNT_INFO_DOUBLE.doubleValue()))
            .andExpect(jsonPath("$.accountInfoInteger").value(DEFAULT_ACCOUNT_INFO_INTEGER))
            .andExpect(jsonPath("$.accountInfoString").value(DEFAULT_ACCOUNT_INFO_STRING))
            .andExpect(jsonPath("$.accountBalance").value(DEFAULT_ACCOUNT_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.accountCredit").value(DEFAULT_ACCOUNT_CREDIT.doubleValue()))
            .andExpect(jsonPath("$.accountCompany").value(DEFAULT_ACCOUNT_COMPANY))
            .andExpect(jsonPath("$.accountCurrency").value(DEFAULT_ACCOUNT_CURRENCY))
            .andExpect(jsonPath("$.accountEquity").value(DEFAULT_ACCOUNT_EQUITY.doubleValue()))
            .andExpect(jsonPath("$.accountFreeMargin").value(DEFAULT_ACCOUNT_FREE_MARGIN.doubleValue()))
            .andExpect(jsonPath("$.accountFreeMarginCheck").value(DEFAULT_ACCOUNT_FREE_MARGIN_CHECK.doubleValue()))
            .andExpect(jsonPath("$.accountFreeMarginMode").value(DEFAULT_ACCOUNT_FREE_MARGIN_MODE.doubleValue()))
            .andExpect(jsonPath("$.accountLeverage").value(DEFAULT_ACCOUNT_LEVERAGE))
            .andExpect(jsonPath("$.accountMargin").value(DEFAULT_ACCOUNT_MARGIN.doubleValue()))
            .andExpect(jsonPath("$.accountName").value(DEFAULT_ACCOUNT_NAME))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.accountProfit").value(DEFAULT_ACCOUNT_PROFIT.doubleValue()))
            .andExpect(jsonPath("$.accountServer").value(DEFAULT_ACCOUNT_SERVER))
            .andExpect(jsonPath("$.accountStopoutLevel").value(DEFAULT_ACCOUNT_STOPOUT_LEVEL))
            .andExpect(jsonPath("$.accountStopoutMode").value(DEFAULT_ACCOUNT_STOPOUT_MODE))
            .andExpect(jsonPath("$.inActive").value(DEFAULT_IN_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.inActiveDate").value(DEFAULT_IN_ACTIVE_DATE.toString()));
    }

    @Test
    @Transactional
    void getMt4AccountsByIdFiltering() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        Long id = mt4Account.getId();

        defaultMt4AccountShouldBeFound("id.equals=" + id);
        defaultMt4AccountShouldNotBeFound("id.notEquals=" + id);

        defaultMt4AccountShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMt4AccountShouldNotBeFound("id.greaterThan=" + id);

        defaultMt4AccountShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMt4AccountShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountType equals to DEFAULT_ACCOUNT_TYPE
        defaultMt4AccountShouldBeFound("accountType.equals=" + DEFAULT_ACCOUNT_TYPE);

        // Get all the mt4AccountList where accountType equals to UPDATED_ACCOUNT_TYPE
        defaultMt4AccountShouldNotBeFound("accountType.equals=" + UPDATED_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountType not equals to DEFAULT_ACCOUNT_TYPE
        defaultMt4AccountShouldNotBeFound("accountType.notEquals=" + DEFAULT_ACCOUNT_TYPE);

        // Get all the mt4AccountList where accountType not equals to UPDATED_ACCOUNT_TYPE
        defaultMt4AccountShouldBeFound("accountType.notEquals=" + UPDATED_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountTypeIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountType in DEFAULT_ACCOUNT_TYPE or UPDATED_ACCOUNT_TYPE
        defaultMt4AccountShouldBeFound("accountType.in=" + DEFAULT_ACCOUNT_TYPE + "," + UPDATED_ACCOUNT_TYPE);

        // Get all the mt4AccountList where accountType equals to UPDATED_ACCOUNT_TYPE
        defaultMt4AccountShouldNotBeFound("accountType.in=" + UPDATED_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountType is not null
        defaultMt4AccountShouldBeFound("accountType.specified=true");

        // Get all the mt4AccountList where accountType is null
        defaultMt4AccountShouldNotBeFound("accountType.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountBrokerIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountBroker equals to DEFAULT_ACCOUNT_BROKER
        defaultMt4AccountShouldBeFound("accountBroker.equals=" + DEFAULT_ACCOUNT_BROKER);

        // Get all the mt4AccountList where accountBroker equals to UPDATED_ACCOUNT_BROKER
        defaultMt4AccountShouldNotBeFound("accountBroker.equals=" + UPDATED_ACCOUNT_BROKER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountBrokerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountBroker not equals to DEFAULT_ACCOUNT_BROKER
        defaultMt4AccountShouldNotBeFound("accountBroker.notEquals=" + DEFAULT_ACCOUNT_BROKER);

        // Get all the mt4AccountList where accountBroker not equals to UPDATED_ACCOUNT_BROKER
        defaultMt4AccountShouldBeFound("accountBroker.notEquals=" + UPDATED_ACCOUNT_BROKER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountBrokerIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountBroker in DEFAULT_ACCOUNT_BROKER or UPDATED_ACCOUNT_BROKER
        defaultMt4AccountShouldBeFound("accountBroker.in=" + DEFAULT_ACCOUNT_BROKER + "," + UPDATED_ACCOUNT_BROKER);

        // Get all the mt4AccountList where accountBroker equals to UPDATED_ACCOUNT_BROKER
        defaultMt4AccountShouldNotBeFound("accountBroker.in=" + UPDATED_ACCOUNT_BROKER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountBrokerIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountBroker is not null
        defaultMt4AccountShouldBeFound("accountBroker.specified=true");

        // Get all the mt4AccountList where accountBroker is null
        defaultMt4AccountShouldNotBeFound("accountBroker.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountLogin equals to DEFAULT_ACCOUNT_LOGIN
        defaultMt4AccountShouldBeFound("accountLogin.equals=" + DEFAULT_ACCOUNT_LOGIN);

        // Get all the mt4AccountList where accountLogin equals to UPDATED_ACCOUNT_LOGIN
        defaultMt4AccountShouldNotBeFound("accountLogin.equals=" + UPDATED_ACCOUNT_LOGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountLoginIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountLogin not equals to DEFAULT_ACCOUNT_LOGIN
        defaultMt4AccountShouldNotBeFound("accountLogin.notEquals=" + DEFAULT_ACCOUNT_LOGIN);

        // Get all the mt4AccountList where accountLogin not equals to UPDATED_ACCOUNT_LOGIN
        defaultMt4AccountShouldBeFound("accountLogin.notEquals=" + UPDATED_ACCOUNT_LOGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountLoginIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountLogin in DEFAULT_ACCOUNT_LOGIN or UPDATED_ACCOUNT_LOGIN
        defaultMt4AccountShouldBeFound("accountLogin.in=" + DEFAULT_ACCOUNT_LOGIN + "," + UPDATED_ACCOUNT_LOGIN);

        // Get all the mt4AccountList where accountLogin equals to UPDATED_ACCOUNT_LOGIN
        defaultMt4AccountShouldNotBeFound("accountLogin.in=" + UPDATED_ACCOUNT_LOGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountLogin is not null
        defaultMt4AccountShouldBeFound("accountLogin.specified=true");

        // Get all the mt4AccountList where accountLogin is null
        defaultMt4AccountShouldNotBeFound("accountLogin.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountLoginContainsSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountLogin contains DEFAULT_ACCOUNT_LOGIN
        defaultMt4AccountShouldBeFound("accountLogin.contains=" + DEFAULT_ACCOUNT_LOGIN);

        // Get all the mt4AccountList where accountLogin contains UPDATED_ACCOUNT_LOGIN
        defaultMt4AccountShouldNotBeFound("accountLogin.contains=" + UPDATED_ACCOUNT_LOGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountLoginNotContainsSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountLogin does not contain DEFAULT_ACCOUNT_LOGIN
        defaultMt4AccountShouldNotBeFound("accountLogin.doesNotContain=" + DEFAULT_ACCOUNT_LOGIN);

        // Get all the mt4AccountList where accountLogin does not contain UPDATED_ACCOUNT_LOGIN
        defaultMt4AccountShouldBeFound("accountLogin.doesNotContain=" + UPDATED_ACCOUNT_LOGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountPassword equals to DEFAULT_ACCOUNT_PASSWORD
        defaultMt4AccountShouldBeFound("accountPassword.equals=" + DEFAULT_ACCOUNT_PASSWORD);

        // Get all the mt4AccountList where accountPassword equals to UPDATED_ACCOUNT_PASSWORD
        defaultMt4AccountShouldNotBeFound("accountPassword.equals=" + UPDATED_ACCOUNT_PASSWORD);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountPassword not equals to DEFAULT_ACCOUNT_PASSWORD
        defaultMt4AccountShouldNotBeFound("accountPassword.notEquals=" + DEFAULT_ACCOUNT_PASSWORD);

        // Get all the mt4AccountList where accountPassword not equals to UPDATED_ACCOUNT_PASSWORD
        defaultMt4AccountShouldBeFound("accountPassword.notEquals=" + UPDATED_ACCOUNT_PASSWORD);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountPassword in DEFAULT_ACCOUNT_PASSWORD or UPDATED_ACCOUNT_PASSWORD
        defaultMt4AccountShouldBeFound("accountPassword.in=" + DEFAULT_ACCOUNT_PASSWORD + "," + UPDATED_ACCOUNT_PASSWORD);

        // Get all the mt4AccountList where accountPassword equals to UPDATED_ACCOUNT_PASSWORD
        defaultMt4AccountShouldNotBeFound("accountPassword.in=" + UPDATED_ACCOUNT_PASSWORD);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountPassword is not null
        defaultMt4AccountShouldBeFound("accountPassword.specified=true");

        // Get all the mt4AccountList where accountPassword is null
        defaultMt4AccountShouldNotBeFound("accountPassword.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountPasswordContainsSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountPassword contains DEFAULT_ACCOUNT_PASSWORD
        defaultMt4AccountShouldBeFound("accountPassword.contains=" + DEFAULT_ACCOUNT_PASSWORD);

        // Get all the mt4AccountList where accountPassword contains UPDATED_ACCOUNT_PASSWORD
        defaultMt4AccountShouldNotBeFound("accountPassword.contains=" + UPDATED_ACCOUNT_PASSWORD);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountPassword does not contain DEFAULT_ACCOUNT_PASSWORD
        defaultMt4AccountShouldNotBeFound("accountPassword.doesNotContain=" + DEFAULT_ACCOUNT_PASSWORD);

        // Get all the mt4AccountList where accountPassword does not contain UPDATED_ACCOUNT_PASSWORD
        defaultMt4AccountShouldBeFound("accountPassword.doesNotContain=" + UPDATED_ACCOUNT_PASSWORD);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInvestorLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInvestorLogin equals to DEFAULT_ACCOUNT_INVESTOR_LOGIN
        defaultMt4AccountShouldBeFound("accountInvestorLogin.equals=" + DEFAULT_ACCOUNT_INVESTOR_LOGIN);

        // Get all the mt4AccountList where accountInvestorLogin equals to UPDATED_ACCOUNT_INVESTOR_LOGIN
        defaultMt4AccountShouldNotBeFound("accountInvestorLogin.equals=" + UPDATED_ACCOUNT_INVESTOR_LOGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInvestorLoginIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInvestorLogin not equals to DEFAULT_ACCOUNT_INVESTOR_LOGIN
        defaultMt4AccountShouldNotBeFound("accountInvestorLogin.notEquals=" + DEFAULT_ACCOUNT_INVESTOR_LOGIN);

        // Get all the mt4AccountList where accountInvestorLogin not equals to UPDATED_ACCOUNT_INVESTOR_LOGIN
        defaultMt4AccountShouldBeFound("accountInvestorLogin.notEquals=" + UPDATED_ACCOUNT_INVESTOR_LOGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInvestorLoginIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInvestorLogin in DEFAULT_ACCOUNT_INVESTOR_LOGIN or UPDATED_ACCOUNT_INVESTOR_LOGIN
        defaultMt4AccountShouldBeFound("accountInvestorLogin.in=" + DEFAULT_ACCOUNT_INVESTOR_LOGIN + "," + UPDATED_ACCOUNT_INVESTOR_LOGIN);

        // Get all the mt4AccountList where accountInvestorLogin equals to UPDATED_ACCOUNT_INVESTOR_LOGIN
        defaultMt4AccountShouldNotBeFound("accountInvestorLogin.in=" + UPDATED_ACCOUNT_INVESTOR_LOGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInvestorLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInvestorLogin is not null
        defaultMt4AccountShouldBeFound("accountInvestorLogin.specified=true");

        // Get all the mt4AccountList where accountInvestorLogin is null
        defaultMt4AccountShouldNotBeFound("accountInvestorLogin.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInvestorLoginContainsSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInvestorLogin contains DEFAULT_ACCOUNT_INVESTOR_LOGIN
        defaultMt4AccountShouldBeFound("accountInvestorLogin.contains=" + DEFAULT_ACCOUNT_INVESTOR_LOGIN);

        // Get all the mt4AccountList where accountInvestorLogin contains UPDATED_ACCOUNT_INVESTOR_LOGIN
        defaultMt4AccountShouldNotBeFound("accountInvestorLogin.contains=" + UPDATED_ACCOUNT_INVESTOR_LOGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInvestorLoginNotContainsSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInvestorLogin does not contain DEFAULT_ACCOUNT_INVESTOR_LOGIN
        defaultMt4AccountShouldNotBeFound("accountInvestorLogin.doesNotContain=" + DEFAULT_ACCOUNT_INVESTOR_LOGIN);

        // Get all the mt4AccountList where accountInvestorLogin does not contain UPDATED_ACCOUNT_INVESTOR_LOGIN
        defaultMt4AccountShouldBeFound("accountInvestorLogin.doesNotContain=" + UPDATED_ACCOUNT_INVESTOR_LOGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInvestorPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInvestorPassword equals to DEFAULT_ACCOUNT_INVESTOR_PASSWORD
        defaultMt4AccountShouldBeFound("accountInvestorPassword.equals=" + DEFAULT_ACCOUNT_INVESTOR_PASSWORD);

        // Get all the mt4AccountList where accountInvestorPassword equals to UPDATED_ACCOUNT_INVESTOR_PASSWORD
        defaultMt4AccountShouldNotBeFound("accountInvestorPassword.equals=" + UPDATED_ACCOUNT_INVESTOR_PASSWORD);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInvestorPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInvestorPassword not equals to DEFAULT_ACCOUNT_INVESTOR_PASSWORD
        defaultMt4AccountShouldNotBeFound("accountInvestorPassword.notEquals=" + DEFAULT_ACCOUNT_INVESTOR_PASSWORD);

        // Get all the mt4AccountList where accountInvestorPassword not equals to UPDATED_ACCOUNT_INVESTOR_PASSWORD
        defaultMt4AccountShouldBeFound("accountInvestorPassword.notEquals=" + UPDATED_ACCOUNT_INVESTOR_PASSWORD);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInvestorPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInvestorPassword in DEFAULT_ACCOUNT_INVESTOR_PASSWORD or UPDATED_ACCOUNT_INVESTOR_PASSWORD
        defaultMt4AccountShouldBeFound(
            "accountInvestorPassword.in=" + DEFAULT_ACCOUNT_INVESTOR_PASSWORD + "," + UPDATED_ACCOUNT_INVESTOR_PASSWORD
        );

        // Get all the mt4AccountList where accountInvestorPassword equals to UPDATED_ACCOUNT_INVESTOR_PASSWORD
        defaultMt4AccountShouldNotBeFound("accountInvestorPassword.in=" + UPDATED_ACCOUNT_INVESTOR_PASSWORD);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInvestorPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInvestorPassword is not null
        defaultMt4AccountShouldBeFound("accountInvestorPassword.specified=true");

        // Get all the mt4AccountList where accountInvestorPassword is null
        defaultMt4AccountShouldNotBeFound("accountInvestorPassword.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInvestorPasswordContainsSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInvestorPassword contains DEFAULT_ACCOUNT_INVESTOR_PASSWORD
        defaultMt4AccountShouldBeFound("accountInvestorPassword.contains=" + DEFAULT_ACCOUNT_INVESTOR_PASSWORD);

        // Get all the mt4AccountList where accountInvestorPassword contains UPDATED_ACCOUNT_INVESTOR_PASSWORD
        defaultMt4AccountShouldNotBeFound("accountInvestorPassword.contains=" + UPDATED_ACCOUNT_INVESTOR_PASSWORD);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInvestorPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInvestorPassword does not contain DEFAULT_ACCOUNT_INVESTOR_PASSWORD
        defaultMt4AccountShouldNotBeFound("accountInvestorPassword.doesNotContain=" + DEFAULT_ACCOUNT_INVESTOR_PASSWORD);

        // Get all the mt4AccountList where accountInvestorPassword does not contain UPDATED_ACCOUNT_INVESTOR_PASSWORD
        defaultMt4AccountShouldBeFound("accountInvestorPassword.doesNotContain=" + UPDATED_ACCOUNT_INVESTOR_PASSWORD);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountRealIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountReal equals to DEFAULT_ACCOUNT_REAL
        defaultMt4AccountShouldBeFound("accountReal.equals=" + DEFAULT_ACCOUNT_REAL);

        // Get all the mt4AccountList where accountReal equals to UPDATED_ACCOUNT_REAL
        defaultMt4AccountShouldNotBeFound("accountReal.equals=" + UPDATED_ACCOUNT_REAL);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountRealIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountReal not equals to DEFAULT_ACCOUNT_REAL
        defaultMt4AccountShouldNotBeFound("accountReal.notEquals=" + DEFAULT_ACCOUNT_REAL);

        // Get all the mt4AccountList where accountReal not equals to UPDATED_ACCOUNT_REAL
        defaultMt4AccountShouldBeFound("accountReal.notEquals=" + UPDATED_ACCOUNT_REAL);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountRealIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountReal in DEFAULT_ACCOUNT_REAL or UPDATED_ACCOUNT_REAL
        defaultMt4AccountShouldBeFound("accountReal.in=" + DEFAULT_ACCOUNT_REAL + "," + UPDATED_ACCOUNT_REAL);

        // Get all the mt4AccountList where accountReal equals to UPDATED_ACCOUNT_REAL
        defaultMt4AccountShouldNotBeFound("accountReal.in=" + UPDATED_ACCOUNT_REAL);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountRealIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountReal is not null
        defaultMt4AccountShouldBeFound("accountReal.specified=true");

        // Get all the mt4AccountList where accountReal is null
        defaultMt4AccountShouldNotBeFound("accountReal.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoDoubleIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoDouble equals to DEFAULT_ACCOUNT_INFO_DOUBLE
        defaultMt4AccountShouldBeFound("accountInfoDouble.equals=" + DEFAULT_ACCOUNT_INFO_DOUBLE);

        // Get all the mt4AccountList where accountInfoDouble equals to UPDATED_ACCOUNT_INFO_DOUBLE
        defaultMt4AccountShouldNotBeFound("accountInfoDouble.equals=" + UPDATED_ACCOUNT_INFO_DOUBLE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoDoubleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoDouble not equals to DEFAULT_ACCOUNT_INFO_DOUBLE
        defaultMt4AccountShouldNotBeFound("accountInfoDouble.notEquals=" + DEFAULT_ACCOUNT_INFO_DOUBLE);

        // Get all the mt4AccountList where accountInfoDouble not equals to UPDATED_ACCOUNT_INFO_DOUBLE
        defaultMt4AccountShouldBeFound("accountInfoDouble.notEquals=" + UPDATED_ACCOUNT_INFO_DOUBLE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoDoubleIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoDouble in DEFAULT_ACCOUNT_INFO_DOUBLE or UPDATED_ACCOUNT_INFO_DOUBLE
        defaultMt4AccountShouldBeFound("accountInfoDouble.in=" + DEFAULT_ACCOUNT_INFO_DOUBLE + "," + UPDATED_ACCOUNT_INFO_DOUBLE);

        // Get all the mt4AccountList where accountInfoDouble equals to UPDATED_ACCOUNT_INFO_DOUBLE
        defaultMt4AccountShouldNotBeFound("accountInfoDouble.in=" + UPDATED_ACCOUNT_INFO_DOUBLE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoDoubleIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoDouble is not null
        defaultMt4AccountShouldBeFound("accountInfoDouble.specified=true");

        // Get all the mt4AccountList where accountInfoDouble is null
        defaultMt4AccountShouldNotBeFound("accountInfoDouble.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoDoubleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoDouble is greater than or equal to DEFAULT_ACCOUNT_INFO_DOUBLE
        defaultMt4AccountShouldBeFound("accountInfoDouble.greaterThanOrEqual=" + DEFAULT_ACCOUNT_INFO_DOUBLE);

        // Get all the mt4AccountList where accountInfoDouble is greater than or equal to UPDATED_ACCOUNT_INFO_DOUBLE
        defaultMt4AccountShouldNotBeFound("accountInfoDouble.greaterThanOrEqual=" + UPDATED_ACCOUNT_INFO_DOUBLE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoDoubleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoDouble is less than or equal to DEFAULT_ACCOUNT_INFO_DOUBLE
        defaultMt4AccountShouldBeFound("accountInfoDouble.lessThanOrEqual=" + DEFAULT_ACCOUNT_INFO_DOUBLE);

        // Get all the mt4AccountList where accountInfoDouble is less than or equal to SMALLER_ACCOUNT_INFO_DOUBLE
        defaultMt4AccountShouldNotBeFound("accountInfoDouble.lessThanOrEqual=" + SMALLER_ACCOUNT_INFO_DOUBLE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoDoubleIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoDouble is less than DEFAULT_ACCOUNT_INFO_DOUBLE
        defaultMt4AccountShouldNotBeFound("accountInfoDouble.lessThan=" + DEFAULT_ACCOUNT_INFO_DOUBLE);

        // Get all the mt4AccountList where accountInfoDouble is less than UPDATED_ACCOUNT_INFO_DOUBLE
        defaultMt4AccountShouldBeFound("accountInfoDouble.lessThan=" + UPDATED_ACCOUNT_INFO_DOUBLE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoDoubleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoDouble is greater than DEFAULT_ACCOUNT_INFO_DOUBLE
        defaultMt4AccountShouldNotBeFound("accountInfoDouble.greaterThan=" + DEFAULT_ACCOUNT_INFO_DOUBLE);

        // Get all the mt4AccountList where accountInfoDouble is greater than SMALLER_ACCOUNT_INFO_DOUBLE
        defaultMt4AccountShouldBeFound("accountInfoDouble.greaterThan=" + SMALLER_ACCOUNT_INFO_DOUBLE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoIntegerIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoInteger equals to DEFAULT_ACCOUNT_INFO_INTEGER
        defaultMt4AccountShouldBeFound("accountInfoInteger.equals=" + DEFAULT_ACCOUNT_INFO_INTEGER);

        // Get all the mt4AccountList where accountInfoInteger equals to UPDATED_ACCOUNT_INFO_INTEGER
        defaultMt4AccountShouldNotBeFound("accountInfoInteger.equals=" + UPDATED_ACCOUNT_INFO_INTEGER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoIntegerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoInteger not equals to DEFAULT_ACCOUNT_INFO_INTEGER
        defaultMt4AccountShouldNotBeFound("accountInfoInteger.notEquals=" + DEFAULT_ACCOUNT_INFO_INTEGER);

        // Get all the mt4AccountList where accountInfoInteger not equals to UPDATED_ACCOUNT_INFO_INTEGER
        defaultMt4AccountShouldBeFound("accountInfoInteger.notEquals=" + UPDATED_ACCOUNT_INFO_INTEGER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoIntegerIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoInteger in DEFAULT_ACCOUNT_INFO_INTEGER or UPDATED_ACCOUNT_INFO_INTEGER
        defaultMt4AccountShouldBeFound("accountInfoInteger.in=" + DEFAULT_ACCOUNT_INFO_INTEGER + "," + UPDATED_ACCOUNT_INFO_INTEGER);

        // Get all the mt4AccountList where accountInfoInteger equals to UPDATED_ACCOUNT_INFO_INTEGER
        defaultMt4AccountShouldNotBeFound("accountInfoInteger.in=" + UPDATED_ACCOUNT_INFO_INTEGER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoIntegerIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoInteger is not null
        defaultMt4AccountShouldBeFound("accountInfoInteger.specified=true");

        // Get all the mt4AccountList where accountInfoInteger is null
        defaultMt4AccountShouldNotBeFound("accountInfoInteger.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoIntegerIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoInteger is greater than or equal to DEFAULT_ACCOUNT_INFO_INTEGER
        defaultMt4AccountShouldBeFound("accountInfoInteger.greaterThanOrEqual=" + DEFAULT_ACCOUNT_INFO_INTEGER);

        // Get all the mt4AccountList where accountInfoInteger is greater than or equal to UPDATED_ACCOUNT_INFO_INTEGER
        defaultMt4AccountShouldNotBeFound("accountInfoInteger.greaterThanOrEqual=" + UPDATED_ACCOUNT_INFO_INTEGER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoIntegerIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoInteger is less than or equal to DEFAULT_ACCOUNT_INFO_INTEGER
        defaultMt4AccountShouldBeFound("accountInfoInteger.lessThanOrEqual=" + DEFAULT_ACCOUNT_INFO_INTEGER);

        // Get all the mt4AccountList where accountInfoInteger is less than or equal to SMALLER_ACCOUNT_INFO_INTEGER
        defaultMt4AccountShouldNotBeFound("accountInfoInteger.lessThanOrEqual=" + SMALLER_ACCOUNT_INFO_INTEGER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoIntegerIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoInteger is less than DEFAULT_ACCOUNT_INFO_INTEGER
        defaultMt4AccountShouldNotBeFound("accountInfoInteger.lessThan=" + DEFAULT_ACCOUNT_INFO_INTEGER);

        // Get all the mt4AccountList where accountInfoInteger is less than UPDATED_ACCOUNT_INFO_INTEGER
        defaultMt4AccountShouldBeFound("accountInfoInteger.lessThan=" + UPDATED_ACCOUNT_INFO_INTEGER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoIntegerIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoInteger is greater than DEFAULT_ACCOUNT_INFO_INTEGER
        defaultMt4AccountShouldNotBeFound("accountInfoInteger.greaterThan=" + DEFAULT_ACCOUNT_INFO_INTEGER);

        // Get all the mt4AccountList where accountInfoInteger is greater than SMALLER_ACCOUNT_INFO_INTEGER
        defaultMt4AccountShouldBeFound("accountInfoInteger.greaterThan=" + SMALLER_ACCOUNT_INFO_INTEGER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoStringIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoString equals to DEFAULT_ACCOUNT_INFO_STRING
        defaultMt4AccountShouldBeFound("accountInfoString.equals=" + DEFAULT_ACCOUNT_INFO_STRING);

        // Get all the mt4AccountList where accountInfoString equals to UPDATED_ACCOUNT_INFO_STRING
        defaultMt4AccountShouldNotBeFound("accountInfoString.equals=" + UPDATED_ACCOUNT_INFO_STRING);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoStringIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoString not equals to DEFAULT_ACCOUNT_INFO_STRING
        defaultMt4AccountShouldNotBeFound("accountInfoString.notEquals=" + DEFAULT_ACCOUNT_INFO_STRING);

        // Get all the mt4AccountList where accountInfoString not equals to UPDATED_ACCOUNT_INFO_STRING
        defaultMt4AccountShouldBeFound("accountInfoString.notEquals=" + UPDATED_ACCOUNT_INFO_STRING);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoStringIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoString in DEFAULT_ACCOUNT_INFO_STRING or UPDATED_ACCOUNT_INFO_STRING
        defaultMt4AccountShouldBeFound("accountInfoString.in=" + DEFAULT_ACCOUNT_INFO_STRING + "," + UPDATED_ACCOUNT_INFO_STRING);

        // Get all the mt4AccountList where accountInfoString equals to UPDATED_ACCOUNT_INFO_STRING
        defaultMt4AccountShouldNotBeFound("accountInfoString.in=" + UPDATED_ACCOUNT_INFO_STRING);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoStringIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoString is not null
        defaultMt4AccountShouldBeFound("accountInfoString.specified=true");

        // Get all the mt4AccountList where accountInfoString is null
        defaultMt4AccountShouldNotBeFound("accountInfoString.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoStringContainsSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoString contains DEFAULT_ACCOUNT_INFO_STRING
        defaultMt4AccountShouldBeFound("accountInfoString.contains=" + DEFAULT_ACCOUNT_INFO_STRING);

        // Get all the mt4AccountList where accountInfoString contains UPDATED_ACCOUNT_INFO_STRING
        defaultMt4AccountShouldNotBeFound("accountInfoString.contains=" + UPDATED_ACCOUNT_INFO_STRING);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountInfoStringNotContainsSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountInfoString does not contain DEFAULT_ACCOUNT_INFO_STRING
        defaultMt4AccountShouldNotBeFound("accountInfoString.doesNotContain=" + DEFAULT_ACCOUNT_INFO_STRING);

        // Get all the mt4AccountList where accountInfoString does not contain UPDATED_ACCOUNT_INFO_STRING
        defaultMt4AccountShouldBeFound("accountInfoString.doesNotContain=" + UPDATED_ACCOUNT_INFO_STRING);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountBalance equals to DEFAULT_ACCOUNT_BALANCE
        defaultMt4AccountShouldBeFound("accountBalance.equals=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the mt4AccountList where accountBalance equals to UPDATED_ACCOUNT_BALANCE
        defaultMt4AccountShouldNotBeFound("accountBalance.equals=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountBalance not equals to DEFAULT_ACCOUNT_BALANCE
        defaultMt4AccountShouldNotBeFound("accountBalance.notEquals=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the mt4AccountList where accountBalance not equals to UPDATED_ACCOUNT_BALANCE
        defaultMt4AccountShouldBeFound("accountBalance.notEquals=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountBalance in DEFAULT_ACCOUNT_BALANCE or UPDATED_ACCOUNT_BALANCE
        defaultMt4AccountShouldBeFound("accountBalance.in=" + DEFAULT_ACCOUNT_BALANCE + "," + UPDATED_ACCOUNT_BALANCE);

        // Get all the mt4AccountList where accountBalance equals to UPDATED_ACCOUNT_BALANCE
        defaultMt4AccountShouldNotBeFound("accountBalance.in=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountBalance is not null
        defaultMt4AccountShouldBeFound("accountBalance.specified=true");

        // Get all the mt4AccountList where accountBalance is null
        defaultMt4AccountShouldNotBeFound("accountBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountBalance is greater than or equal to DEFAULT_ACCOUNT_BALANCE
        defaultMt4AccountShouldBeFound("accountBalance.greaterThanOrEqual=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the mt4AccountList where accountBalance is greater than or equal to UPDATED_ACCOUNT_BALANCE
        defaultMt4AccountShouldNotBeFound("accountBalance.greaterThanOrEqual=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountBalance is less than or equal to DEFAULT_ACCOUNT_BALANCE
        defaultMt4AccountShouldBeFound("accountBalance.lessThanOrEqual=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the mt4AccountList where accountBalance is less than or equal to SMALLER_ACCOUNT_BALANCE
        defaultMt4AccountShouldNotBeFound("accountBalance.lessThanOrEqual=" + SMALLER_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountBalance is less than DEFAULT_ACCOUNT_BALANCE
        defaultMt4AccountShouldNotBeFound("accountBalance.lessThan=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the mt4AccountList where accountBalance is less than UPDATED_ACCOUNT_BALANCE
        defaultMt4AccountShouldBeFound("accountBalance.lessThan=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountBalance is greater than DEFAULT_ACCOUNT_BALANCE
        defaultMt4AccountShouldNotBeFound("accountBalance.greaterThan=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the mt4AccountList where accountBalance is greater than SMALLER_ACCOUNT_BALANCE
        defaultMt4AccountShouldBeFound("accountBalance.greaterThan=" + SMALLER_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountCreditIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountCredit equals to DEFAULT_ACCOUNT_CREDIT
        defaultMt4AccountShouldBeFound("accountCredit.equals=" + DEFAULT_ACCOUNT_CREDIT);

        // Get all the mt4AccountList where accountCredit equals to UPDATED_ACCOUNT_CREDIT
        defaultMt4AccountShouldNotBeFound("accountCredit.equals=" + UPDATED_ACCOUNT_CREDIT);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountCreditIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountCredit not equals to DEFAULT_ACCOUNT_CREDIT
        defaultMt4AccountShouldNotBeFound("accountCredit.notEquals=" + DEFAULT_ACCOUNT_CREDIT);

        // Get all the mt4AccountList where accountCredit not equals to UPDATED_ACCOUNT_CREDIT
        defaultMt4AccountShouldBeFound("accountCredit.notEquals=" + UPDATED_ACCOUNT_CREDIT);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountCreditIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountCredit in DEFAULT_ACCOUNT_CREDIT or UPDATED_ACCOUNT_CREDIT
        defaultMt4AccountShouldBeFound("accountCredit.in=" + DEFAULT_ACCOUNT_CREDIT + "," + UPDATED_ACCOUNT_CREDIT);

        // Get all the mt4AccountList where accountCredit equals to UPDATED_ACCOUNT_CREDIT
        defaultMt4AccountShouldNotBeFound("accountCredit.in=" + UPDATED_ACCOUNT_CREDIT);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountCreditIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountCredit is not null
        defaultMt4AccountShouldBeFound("accountCredit.specified=true");

        // Get all the mt4AccountList where accountCredit is null
        defaultMt4AccountShouldNotBeFound("accountCredit.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountCreditIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountCredit is greater than or equal to DEFAULT_ACCOUNT_CREDIT
        defaultMt4AccountShouldBeFound("accountCredit.greaterThanOrEqual=" + DEFAULT_ACCOUNT_CREDIT);

        // Get all the mt4AccountList where accountCredit is greater than or equal to UPDATED_ACCOUNT_CREDIT
        defaultMt4AccountShouldNotBeFound("accountCredit.greaterThanOrEqual=" + UPDATED_ACCOUNT_CREDIT);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountCreditIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountCredit is less than or equal to DEFAULT_ACCOUNT_CREDIT
        defaultMt4AccountShouldBeFound("accountCredit.lessThanOrEqual=" + DEFAULT_ACCOUNT_CREDIT);

        // Get all the mt4AccountList where accountCredit is less than or equal to SMALLER_ACCOUNT_CREDIT
        defaultMt4AccountShouldNotBeFound("accountCredit.lessThanOrEqual=" + SMALLER_ACCOUNT_CREDIT);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountCreditIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountCredit is less than DEFAULT_ACCOUNT_CREDIT
        defaultMt4AccountShouldNotBeFound("accountCredit.lessThan=" + DEFAULT_ACCOUNT_CREDIT);

        // Get all the mt4AccountList where accountCredit is less than UPDATED_ACCOUNT_CREDIT
        defaultMt4AccountShouldBeFound("accountCredit.lessThan=" + UPDATED_ACCOUNT_CREDIT);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountCreditIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountCredit is greater than DEFAULT_ACCOUNT_CREDIT
        defaultMt4AccountShouldNotBeFound("accountCredit.greaterThan=" + DEFAULT_ACCOUNT_CREDIT);

        // Get all the mt4AccountList where accountCredit is greater than SMALLER_ACCOUNT_CREDIT
        defaultMt4AccountShouldBeFound("accountCredit.greaterThan=" + SMALLER_ACCOUNT_CREDIT);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountCompany equals to DEFAULT_ACCOUNT_COMPANY
        defaultMt4AccountShouldBeFound("accountCompany.equals=" + DEFAULT_ACCOUNT_COMPANY);

        // Get all the mt4AccountList where accountCompany equals to UPDATED_ACCOUNT_COMPANY
        defaultMt4AccountShouldNotBeFound("accountCompany.equals=" + UPDATED_ACCOUNT_COMPANY);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountCompanyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountCompany not equals to DEFAULT_ACCOUNT_COMPANY
        defaultMt4AccountShouldNotBeFound("accountCompany.notEquals=" + DEFAULT_ACCOUNT_COMPANY);

        // Get all the mt4AccountList where accountCompany not equals to UPDATED_ACCOUNT_COMPANY
        defaultMt4AccountShouldBeFound("accountCompany.notEquals=" + UPDATED_ACCOUNT_COMPANY);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountCompanyIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountCompany in DEFAULT_ACCOUNT_COMPANY or UPDATED_ACCOUNT_COMPANY
        defaultMt4AccountShouldBeFound("accountCompany.in=" + DEFAULT_ACCOUNT_COMPANY + "," + UPDATED_ACCOUNT_COMPANY);

        // Get all the mt4AccountList where accountCompany equals to UPDATED_ACCOUNT_COMPANY
        defaultMt4AccountShouldNotBeFound("accountCompany.in=" + UPDATED_ACCOUNT_COMPANY);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountCompanyIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountCompany is not null
        defaultMt4AccountShouldBeFound("accountCompany.specified=true");

        // Get all the mt4AccountList where accountCompany is null
        defaultMt4AccountShouldNotBeFound("accountCompany.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountCompanyContainsSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountCompany contains DEFAULT_ACCOUNT_COMPANY
        defaultMt4AccountShouldBeFound("accountCompany.contains=" + DEFAULT_ACCOUNT_COMPANY);

        // Get all the mt4AccountList where accountCompany contains UPDATED_ACCOUNT_COMPANY
        defaultMt4AccountShouldNotBeFound("accountCompany.contains=" + UPDATED_ACCOUNT_COMPANY);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountCompanyNotContainsSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountCompany does not contain DEFAULT_ACCOUNT_COMPANY
        defaultMt4AccountShouldNotBeFound("accountCompany.doesNotContain=" + DEFAULT_ACCOUNT_COMPANY);

        // Get all the mt4AccountList where accountCompany does not contain UPDATED_ACCOUNT_COMPANY
        defaultMt4AccountShouldBeFound("accountCompany.doesNotContain=" + UPDATED_ACCOUNT_COMPANY);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountCurrency equals to DEFAULT_ACCOUNT_CURRENCY
        defaultMt4AccountShouldBeFound("accountCurrency.equals=" + DEFAULT_ACCOUNT_CURRENCY);

        // Get all the mt4AccountList where accountCurrency equals to UPDATED_ACCOUNT_CURRENCY
        defaultMt4AccountShouldNotBeFound("accountCurrency.equals=" + UPDATED_ACCOUNT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountCurrencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountCurrency not equals to DEFAULT_ACCOUNT_CURRENCY
        defaultMt4AccountShouldNotBeFound("accountCurrency.notEquals=" + DEFAULT_ACCOUNT_CURRENCY);

        // Get all the mt4AccountList where accountCurrency not equals to UPDATED_ACCOUNT_CURRENCY
        defaultMt4AccountShouldBeFound("accountCurrency.notEquals=" + UPDATED_ACCOUNT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountCurrency in DEFAULT_ACCOUNT_CURRENCY or UPDATED_ACCOUNT_CURRENCY
        defaultMt4AccountShouldBeFound("accountCurrency.in=" + DEFAULT_ACCOUNT_CURRENCY + "," + UPDATED_ACCOUNT_CURRENCY);

        // Get all the mt4AccountList where accountCurrency equals to UPDATED_ACCOUNT_CURRENCY
        defaultMt4AccountShouldNotBeFound("accountCurrency.in=" + UPDATED_ACCOUNT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountCurrency is not null
        defaultMt4AccountShouldBeFound("accountCurrency.specified=true");

        // Get all the mt4AccountList where accountCurrency is null
        defaultMt4AccountShouldNotBeFound("accountCurrency.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountCurrencyContainsSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountCurrency contains DEFAULT_ACCOUNT_CURRENCY
        defaultMt4AccountShouldBeFound("accountCurrency.contains=" + DEFAULT_ACCOUNT_CURRENCY);

        // Get all the mt4AccountList where accountCurrency contains UPDATED_ACCOUNT_CURRENCY
        defaultMt4AccountShouldNotBeFound("accountCurrency.contains=" + UPDATED_ACCOUNT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountCurrency does not contain DEFAULT_ACCOUNT_CURRENCY
        defaultMt4AccountShouldNotBeFound("accountCurrency.doesNotContain=" + DEFAULT_ACCOUNT_CURRENCY);

        // Get all the mt4AccountList where accountCurrency does not contain UPDATED_ACCOUNT_CURRENCY
        defaultMt4AccountShouldBeFound("accountCurrency.doesNotContain=" + UPDATED_ACCOUNT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountEquityIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountEquity equals to DEFAULT_ACCOUNT_EQUITY
        defaultMt4AccountShouldBeFound("accountEquity.equals=" + DEFAULT_ACCOUNT_EQUITY);

        // Get all the mt4AccountList where accountEquity equals to UPDATED_ACCOUNT_EQUITY
        defaultMt4AccountShouldNotBeFound("accountEquity.equals=" + UPDATED_ACCOUNT_EQUITY);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountEquityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountEquity not equals to DEFAULT_ACCOUNT_EQUITY
        defaultMt4AccountShouldNotBeFound("accountEquity.notEquals=" + DEFAULT_ACCOUNT_EQUITY);

        // Get all the mt4AccountList where accountEquity not equals to UPDATED_ACCOUNT_EQUITY
        defaultMt4AccountShouldBeFound("accountEquity.notEquals=" + UPDATED_ACCOUNT_EQUITY);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountEquityIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountEquity in DEFAULT_ACCOUNT_EQUITY or UPDATED_ACCOUNT_EQUITY
        defaultMt4AccountShouldBeFound("accountEquity.in=" + DEFAULT_ACCOUNT_EQUITY + "," + UPDATED_ACCOUNT_EQUITY);

        // Get all the mt4AccountList where accountEquity equals to UPDATED_ACCOUNT_EQUITY
        defaultMt4AccountShouldNotBeFound("accountEquity.in=" + UPDATED_ACCOUNT_EQUITY);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountEquityIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountEquity is not null
        defaultMt4AccountShouldBeFound("accountEquity.specified=true");

        // Get all the mt4AccountList where accountEquity is null
        defaultMt4AccountShouldNotBeFound("accountEquity.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountEquityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountEquity is greater than or equal to DEFAULT_ACCOUNT_EQUITY
        defaultMt4AccountShouldBeFound("accountEquity.greaterThanOrEqual=" + DEFAULT_ACCOUNT_EQUITY);

        // Get all the mt4AccountList where accountEquity is greater than or equal to UPDATED_ACCOUNT_EQUITY
        defaultMt4AccountShouldNotBeFound("accountEquity.greaterThanOrEqual=" + UPDATED_ACCOUNT_EQUITY);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountEquityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountEquity is less than or equal to DEFAULT_ACCOUNT_EQUITY
        defaultMt4AccountShouldBeFound("accountEquity.lessThanOrEqual=" + DEFAULT_ACCOUNT_EQUITY);

        // Get all the mt4AccountList where accountEquity is less than or equal to SMALLER_ACCOUNT_EQUITY
        defaultMt4AccountShouldNotBeFound("accountEquity.lessThanOrEqual=" + SMALLER_ACCOUNT_EQUITY);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountEquityIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountEquity is less than DEFAULT_ACCOUNT_EQUITY
        defaultMt4AccountShouldNotBeFound("accountEquity.lessThan=" + DEFAULT_ACCOUNT_EQUITY);

        // Get all the mt4AccountList where accountEquity is less than UPDATED_ACCOUNT_EQUITY
        defaultMt4AccountShouldBeFound("accountEquity.lessThan=" + UPDATED_ACCOUNT_EQUITY);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountEquityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountEquity is greater than DEFAULT_ACCOUNT_EQUITY
        defaultMt4AccountShouldNotBeFound("accountEquity.greaterThan=" + DEFAULT_ACCOUNT_EQUITY);

        // Get all the mt4AccountList where accountEquity is greater than SMALLER_ACCOUNT_EQUITY
        defaultMt4AccountShouldBeFound("accountEquity.greaterThan=" + SMALLER_ACCOUNT_EQUITY);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMargin equals to DEFAULT_ACCOUNT_FREE_MARGIN
        defaultMt4AccountShouldBeFound("accountFreeMargin.equals=" + DEFAULT_ACCOUNT_FREE_MARGIN);

        // Get all the mt4AccountList where accountFreeMargin equals to UPDATED_ACCOUNT_FREE_MARGIN
        defaultMt4AccountShouldNotBeFound("accountFreeMargin.equals=" + UPDATED_ACCOUNT_FREE_MARGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMargin not equals to DEFAULT_ACCOUNT_FREE_MARGIN
        defaultMt4AccountShouldNotBeFound("accountFreeMargin.notEquals=" + DEFAULT_ACCOUNT_FREE_MARGIN);

        // Get all the mt4AccountList where accountFreeMargin not equals to UPDATED_ACCOUNT_FREE_MARGIN
        defaultMt4AccountShouldBeFound("accountFreeMargin.notEquals=" + UPDATED_ACCOUNT_FREE_MARGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMargin in DEFAULT_ACCOUNT_FREE_MARGIN or UPDATED_ACCOUNT_FREE_MARGIN
        defaultMt4AccountShouldBeFound("accountFreeMargin.in=" + DEFAULT_ACCOUNT_FREE_MARGIN + "," + UPDATED_ACCOUNT_FREE_MARGIN);

        // Get all the mt4AccountList where accountFreeMargin equals to UPDATED_ACCOUNT_FREE_MARGIN
        defaultMt4AccountShouldNotBeFound("accountFreeMargin.in=" + UPDATED_ACCOUNT_FREE_MARGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMargin is not null
        defaultMt4AccountShouldBeFound("accountFreeMargin.specified=true");

        // Get all the mt4AccountList where accountFreeMargin is null
        defaultMt4AccountShouldNotBeFound("accountFreeMargin.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMargin is greater than or equal to DEFAULT_ACCOUNT_FREE_MARGIN
        defaultMt4AccountShouldBeFound("accountFreeMargin.greaterThanOrEqual=" + DEFAULT_ACCOUNT_FREE_MARGIN);

        // Get all the mt4AccountList where accountFreeMargin is greater than or equal to UPDATED_ACCOUNT_FREE_MARGIN
        defaultMt4AccountShouldNotBeFound("accountFreeMargin.greaterThanOrEqual=" + UPDATED_ACCOUNT_FREE_MARGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMargin is less than or equal to DEFAULT_ACCOUNT_FREE_MARGIN
        defaultMt4AccountShouldBeFound("accountFreeMargin.lessThanOrEqual=" + DEFAULT_ACCOUNT_FREE_MARGIN);

        // Get all the mt4AccountList where accountFreeMargin is less than or equal to SMALLER_ACCOUNT_FREE_MARGIN
        defaultMt4AccountShouldNotBeFound("accountFreeMargin.lessThanOrEqual=" + SMALLER_ACCOUNT_FREE_MARGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMargin is less than DEFAULT_ACCOUNT_FREE_MARGIN
        defaultMt4AccountShouldNotBeFound("accountFreeMargin.lessThan=" + DEFAULT_ACCOUNT_FREE_MARGIN);

        // Get all the mt4AccountList where accountFreeMargin is less than UPDATED_ACCOUNT_FREE_MARGIN
        defaultMt4AccountShouldBeFound("accountFreeMargin.lessThan=" + UPDATED_ACCOUNT_FREE_MARGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMargin is greater than DEFAULT_ACCOUNT_FREE_MARGIN
        defaultMt4AccountShouldNotBeFound("accountFreeMargin.greaterThan=" + DEFAULT_ACCOUNT_FREE_MARGIN);

        // Get all the mt4AccountList where accountFreeMargin is greater than SMALLER_ACCOUNT_FREE_MARGIN
        defaultMt4AccountShouldBeFound("accountFreeMargin.greaterThan=" + SMALLER_ACCOUNT_FREE_MARGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginCheckIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMarginCheck equals to DEFAULT_ACCOUNT_FREE_MARGIN_CHECK
        defaultMt4AccountShouldBeFound("accountFreeMarginCheck.equals=" + DEFAULT_ACCOUNT_FREE_MARGIN_CHECK);

        // Get all the mt4AccountList where accountFreeMarginCheck equals to UPDATED_ACCOUNT_FREE_MARGIN_CHECK
        defaultMt4AccountShouldNotBeFound("accountFreeMarginCheck.equals=" + UPDATED_ACCOUNT_FREE_MARGIN_CHECK);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginCheckIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMarginCheck not equals to DEFAULT_ACCOUNT_FREE_MARGIN_CHECK
        defaultMt4AccountShouldNotBeFound("accountFreeMarginCheck.notEquals=" + DEFAULT_ACCOUNT_FREE_MARGIN_CHECK);

        // Get all the mt4AccountList where accountFreeMarginCheck not equals to UPDATED_ACCOUNT_FREE_MARGIN_CHECK
        defaultMt4AccountShouldBeFound("accountFreeMarginCheck.notEquals=" + UPDATED_ACCOUNT_FREE_MARGIN_CHECK);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginCheckIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMarginCheck in DEFAULT_ACCOUNT_FREE_MARGIN_CHECK or UPDATED_ACCOUNT_FREE_MARGIN_CHECK
        defaultMt4AccountShouldBeFound(
            "accountFreeMarginCheck.in=" + DEFAULT_ACCOUNT_FREE_MARGIN_CHECK + "," + UPDATED_ACCOUNT_FREE_MARGIN_CHECK
        );

        // Get all the mt4AccountList where accountFreeMarginCheck equals to UPDATED_ACCOUNT_FREE_MARGIN_CHECK
        defaultMt4AccountShouldNotBeFound("accountFreeMarginCheck.in=" + UPDATED_ACCOUNT_FREE_MARGIN_CHECK);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginCheckIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMarginCheck is not null
        defaultMt4AccountShouldBeFound("accountFreeMarginCheck.specified=true");

        // Get all the mt4AccountList where accountFreeMarginCheck is null
        defaultMt4AccountShouldNotBeFound("accountFreeMarginCheck.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginCheckIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMarginCheck is greater than or equal to DEFAULT_ACCOUNT_FREE_MARGIN_CHECK
        defaultMt4AccountShouldBeFound("accountFreeMarginCheck.greaterThanOrEqual=" + DEFAULT_ACCOUNT_FREE_MARGIN_CHECK);

        // Get all the mt4AccountList where accountFreeMarginCheck is greater than or equal to UPDATED_ACCOUNT_FREE_MARGIN_CHECK
        defaultMt4AccountShouldNotBeFound("accountFreeMarginCheck.greaterThanOrEqual=" + UPDATED_ACCOUNT_FREE_MARGIN_CHECK);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginCheckIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMarginCheck is less than or equal to DEFAULT_ACCOUNT_FREE_MARGIN_CHECK
        defaultMt4AccountShouldBeFound("accountFreeMarginCheck.lessThanOrEqual=" + DEFAULT_ACCOUNT_FREE_MARGIN_CHECK);

        // Get all the mt4AccountList where accountFreeMarginCheck is less than or equal to SMALLER_ACCOUNT_FREE_MARGIN_CHECK
        defaultMt4AccountShouldNotBeFound("accountFreeMarginCheck.lessThanOrEqual=" + SMALLER_ACCOUNT_FREE_MARGIN_CHECK);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginCheckIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMarginCheck is less than DEFAULT_ACCOUNT_FREE_MARGIN_CHECK
        defaultMt4AccountShouldNotBeFound("accountFreeMarginCheck.lessThan=" + DEFAULT_ACCOUNT_FREE_MARGIN_CHECK);

        // Get all the mt4AccountList where accountFreeMarginCheck is less than UPDATED_ACCOUNT_FREE_MARGIN_CHECK
        defaultMt4AccountShouldBeFound("accountFreeMarginCheck.lessThan=" + UPDATED_ACCOUNT_FREE_MARGIN_CHECK);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginCheckIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMarginCheck is greater than DEFAULT_ACCOUNT_FREE_MARGIN_CHECK
        defaultMt4AccountShouldNotBeFound("accountFreeMarginCheck.greaterThan=" + DEFAULT_ACCOUNT_FREE_MARGIN_CHECK);

        // Get all the mt4AccountList where accountFreeMarginCheck is greater than SMALLER_ACCOUNT_FREE_MARGIN_CHECK
        defaultMt4AccountShouldBeFound("accountFreeMarginCheck.greaterThan=" + SMALLER_ACCOUNT_FREE_MARGIN_CHECK);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginModeIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMarginMode equals to DEFAULT_ACCOUNT_FREE_MARGIN_MODE
        defaultMt4AccountShouldBeFound("accountFreeMarginMode.equals=" + DEFAULT_ACCOUNT_FREE_MARGIN_MODE);

        // Get all the mt4AccountList where accountFreeMarginMode equals to UPDATED_ACCOUNT_FREE_MARGIN_MODE
        defaultMt4AccountShouldNotBeFound("accountFreeMarginMode.equals=" + UPDATED_ACCOUNT_FREE_MARGIN_MODE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginModeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMarginMode not equals to DEFAULT_ACCOUNT_FREE_MARGIN_MODE
        defaultMt4AccountShouldNotBeFound("accountFreeMarginMode.notEquals=" + DEFAULT_ACCOUNT_FREE_MARGIN_MODE);

        // Get all the mt4AccountList where accountFreeMarginMode not equals to UPDATED_ACCOUNT_FREE_MARGIN_MODE
        defaultMt4AccountShouldBeFound("accountFreeMarginMode.notEquals=" + UPDATED_ACCOUNT_FREE_MARGIN_MODE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginModeIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMarginMode in DEFAULT_ACCOUNT_FREE_MARGIN_MODE or UPDATED_ACCOUNT_FREE_MARGIN_MODE
        defaultMt4AccountShouldBeFound(
            "accountFreeMarginMode.in=" + DEFAULT_ACCOUNT_FREE_MARGIN_MODE + "," + UPDATED_ACCOUNT_FREE_MARGIN_MODE
        );

        // Get all the mt4AccountList where accountFreeMarginMode equals to UPDATED_ACCOUNT_FREE_MARGIN_MODE
        defaultMt4AccountShouldNotBeFound("accountFreeMarginMode.in=" + UPDATED_ACCOUNT_FREE_MARGIN_MODE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginModeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMarginMode is not null
        defaultMt4AccountShouldBeFound("accountFreeMarginMode.specified=true");

        // Get all the mt4AccountList where accountFreeMarginMode is null
        defaultMt4AccountShouldNotBeFound("accountFreeMarginMode.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginModeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMarginMode is greater than or equal to DEFAULT_ACCOUNT_FREE_MARGIN_MODE
        defaultMt4AccountShouldBeFound("accountFreeMarginMode.greaterThanOrEqual=" + DEFAULT_ACCOUNT_FREE_MARGIN_MODE);

        // Get all the mt4AccountList where accountFreeMarginMode is greater than or equal to UPDATED_ACCOUNT_FREE_MARGIN_MODE
        defaultMt4AccountShouldNotBeFound("accountFreeMarginMode.greaterThanOrEqual=" + UPDATED_ACCOUNT_FREE_MARGIN_MODE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginModeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMarginMode is less than or equal to DEFAULT_ACCOUNT_FREE_MARGIN_MODE
        defaultMt4AccountShouldBeFound("accountFreeMarginMode.lessThanOrEqual=" + DEFAULT_ACCOUNT_FREE_MARGIN_MODE);

        // Get all the mt4AccountList where accountFreeMarginMode is less than or equal to SMALLER_ACCOUNT_FREE_MARGIN_MODE
        defaultMt4AccountShouldNotBeFound("accountFreeMarginMode.lessThanOrEqual=" + SMALLER_ACCOUNT_FREE_MARGIN_MODE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginModeIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMarginMode is less than DEFAULT_ACCOUNT_FREE_MARGIN_MODE
        defaultMt4AccountShouldNotBeFound("accountFreeMarginMode.lessThan=" + DEFAULT_ACCOUNT_FREE_MARGIN_MODE);

        // Get all the mt4AccountList where accountFreeMarginMode is less than UPDATED_ACCOUNT_FREE_MARGIN_MODE
        defaultMt4AccountShouldBeFound("accountFreeMarginMode.lessThan=" + UPDATED_ACCOUNT_FREE_MARGIN_MODE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountFreeMarginModeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountFreeMarginMode is greater than DEFAULT_ACCOUNT_FREE_MARGIN_MODE
        defaultMt4AccountShouldNotBeFound("accountFreeMarginMode.greaterThan=" + DEFAULT_ACCOUNT_FREE_MARGIN_MODE);

        // Get all the mt4AccountList where accountFreeMarginMode is greater than SMALLER_ACCOUNT_FREE_MARGIN_MODE
        defaultMt4AccountShouldBeFound("accountFreeMarginMode.greaterThan=" + SMALLER_ACCOUNT_FREE_MARGIN_MODE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountLeverageIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountLeverage equals to DEFAULT_ACCOUNT_LEVERAGE
        defaultMt4AccountShouldBeFound("accountLeverage.equals=" + DEFAULT_ACCOUNT_LEVERAGE);

        // Get all the mt4AccountList where accountLeverage equals to UPDATED_ACCOUNT_LEVERAGE
        defaultMt4AccountShouldNotBeFound("accountLeverage.equals=" + UPDATED_ACCOUNT_LEVERAGE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountLeverageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountLeverage not equals to DEFAULT_ACCOUNT_LEVERAGE
        defaultMt4AccountShouldNotBeFound("accountLeverage.notEquals=" + DEFAULT_ACCOUNT_LEVERAGE);

        // Get all the mt4AccountList where accountLeverage not equals to UPDATED_ACCOUNT_LEVERAGE
        defaultMt4AccountShouldBeFound("accountLeverage.notEquals=" + UPDATED_ACCOUNT_LEVERAGE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountLeverageIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountLeverage in DEFAULT_ACCOUNT_LEVERAGE or UPDATED_ACCOUNT_LEVERAGE
        defaultMt4AccountShouldBeFound("accountLeverage.in=" + DEFAULT_ACCOUNT_LEVERAGE + "," + UPDATED_ACCOUNT_LEVERAGE);

        // Get all the mt4AccountList where accountLeverage equals to UPDATED_ACCOUNT_LEVERAGE
        defaultMt4AccountShouldNotBeFound("accountLeverage.in=" + UPDATED_ACCOUNT_LEVERAGE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountLeverageIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountLeverage is not null
        defaultMt4AccountShouldBeFound("accountLeverage.specified=true");

        // Get all the mt4AccountList where accountLeverage is null
        defaultMt4AccountShouldNotBeFound("accountLeverage.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountLeverageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountLeverage is greater than or equal to DEFAULT_ACCOUNT_LEVERAGE
        defaultMt4AccountShouldBeFound("accountLeverage.greaterThanOrEqual=" + DEFAULT_ACCOUNT_LEVERAGE);

        // Get all the mt4AccountList where accountLeverage is greater than or equal to UPDATED_ACCOUNT_LEVERAGE
        defaultMt4AccountShouldNotBeFound("accountLeverage.greaterThanOrEqual=" + UPDATED_ACCOUNT_LEVERAGE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountLeverageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountLeverage is less than or equal to DEFAULT_ACCOUNT_LEVERAGE
        defaultMt4AccountShouldBeFound("accountLeverage.lessThanOrEqual=" + DEFAULT_ACCOUNT_LEVERAGE);

        // Get all the mt4AccountList where accountLeverage is less than or equal to SMALLER_ACCOUNT_LEVERAGE
        defaultMt4AccountShouldNotBeFound("accountLeverage.lessThanOrEqual=" + SMALLER_ACCOUNT_LEVERAGE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountLeverageIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountLeverage is less than DEFAULT_ACCOUNT_LEVERAGE
        defaultMt4AccountShouldNotBeFound("accountLeverage.lessThan=" + DEFAULT_ACCOUNT_LEVERAGE);

        // Get all the mt4AccountList where accountLeverage is less than UPDATED_ACCOUNT_LEVERAGE
        defaultMt4AccountShouldBeFound("accountLeverage.lessThan=" + UPDATED_ACCOUNT_LEVERAGE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountLeverageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountLeverage is greater than DEFAULT_ACCOUNT_LEVERAGE
        defaultMt4AccountShouldNotBeFound("accountLeverage.greaterThan=" + DEFAULT_ACCOUNT_LEVERAGE);

        // Get all the mt4AccountList where accountLeverage is greater than SMALLER_ACCOUNT_LEVERAGE
        defaultMt4AccountShouldBeFound("accountLeverage.greaterThan=" + SMALLER_ACCOUNT_LEVERAGE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountMarginIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountMargin equals to DEFAULT_ACCOUNT_MARGIN
        defaultMt4AccountShouldBeFound("accountMargin.equals=" + DEFAULT_ACCOUNT_MARGIN);

        // Get all the mt4AccountList where accountMargin equals to UPDATED_ACCOUNT_MARGIN
        defaultMt4AccountShouldNotBeFound("accountMargin.equals=" + UPDATED_ACCOUNT_MARGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountMarginIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountMargin not equals to DEFAULT_ACCOUNT_MARGIN
        defaultMt4AccountShouldNotBeFound("accountMargin.notEquals=" + DEFAULT_ACCOUNT_MARGIN);

        // Get all the mt4AccountList where accountMargin not equals to UPDATED_ACCOUNT_MARGIN
        defaultMt4AccountShouldBeFound("accountMargin.notEquals=" + UPDATED_ACCOUNT_MARGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountMarginIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountMargin in DEFAULT_ACCOUNT_MARGIN or UPDATED_ACCOUNT_MARGIN
        defaultMt4AccountShouldBeFound("accountMargin.in=" + DEFAULT_ACCOUNT_MARGIN + "," + UPDATED_ACCOUNT_MARGIN);

        // Get all the mt4AccountList where accountMargin equals to UPDATED_ACCOUNT_MARGIN
        defaultMt4AccountShouldNotBeFound("accountMargin.in=" + UPDATED_ACCOUNT_MARGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountMarginIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountMargin is not null
        defaultMt4AccountShouldBeFound("accountMargin.specified=true");

        // Get all the mt4AccountList where accountMargin is null
        defaultMt4AccountShouldNotBeFound("accountMargin.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountMarginIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountMargin is greater than or equal to DEFAULT_ACCOUNT_MARGIN
        defaultMt4AccountShouldBeFound("accountMargin.greaterThanOrEqual=" + DEFAULT_ACCOUNT_MARGIN);

        // Get all the mt4AccountList where accountMargin is greater than or equal to UPDATED_ACCOUNT_MARGIN
        defaultMt4AccountShouldNotBeFound("accountMargin.greaterThanOrEqual=" + UPDATED_ACCOUNT_MARGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountMarginIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountMargin is less than or equal to DEFAULT_ACCOUNT_MARGIN
        defaultMt4AccountShouldBeFound("accountMargin.lessThanOrEqual=" + DEFAULT_ACCOUNT_MARGIN);

        // Get all the mt4AccountList where accountMargin is less than or equal to SMALLER_ACCOUNT_MARGIN
        defaultMt4AccountShouldNotBeFound("accountMargin.lessThanOrEqual=" + SMALLER_ACCOUNT_MARGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountMarginIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountMargin is less than DEFAULT_ACCOUNT_MARGIN
        defaultMt4AccountShouldNotBeFound("accountMargin.lessThan=" + DEFAULT_ACCOUNT_MARGIN);

        // Get all the mt4AccountList where accountMargin is less than UPDATED_ACCOUNT_MARGIN
        defaultMt4AccountShouldBeFound("accountMargin.lessThan=" + UPDATED_ACCOUNT_MARGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountMarginIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountMargin is greater than DEFAULT_ACCOUNT_MARGIN
        defaultMt4AccountShouldNotBeFound("accountMargin.greaterThan=" + DEFAULT_ACCOUNT_MARGIN);

        // Get all the mt4AccountList where accountMargin is greater than SMALLER_ACCOUNT_MARGIN
        defaultMt4AccountShouldBeFound("accountMargin.greaterThan=" + SMALLER_ACCOUNT_MARGIN);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountNameIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountName equals to DEFAULT_ACCOUNT_NAME
        defaultMt4AccountShouldBeFound("accountName.equals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the mt4AccountList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultMt4AccountShouldNotBeFound("accountName.equals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountName not equals to DEFAULT_ACCOUNT_NAME
        defaultMt4AccountShouldNotBeFound("accountName.notEquals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the mt4AccountList where accountName not equals to UPDATED_ACCOUNT_NAME
        defaultMt4AccountShouldBeFound("accountName.notEquals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountNameIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountName in DEFAULT_ACCOUNT_NAME or UPDATED_ACCOUNT_NAME
        defaultMt4AccountShouldBeFound("accountName.in=" + DEFAULT_ACCOUNT_NAME + "," + UPDATED_ACCOUNT_NAME);

        // Get all the mt4AccountList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultMt4AccountShouldNotBeFound("accountName.in=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountName is not null
        defaultMt4AccountShouldBeFound("accountName.specified=true");

        // Get all the mt4AccountList where accountName is null
        defaultMt4AccountShouldNotBeFound("accountName.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountNameContainsSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountName contains DEFAULT_ACCOUNT_NAME
        defaultMt4AccountShouldBeFound("accountName.contains=" + DEFAULT_ACCOUNT_NAME);

        // Get all the mt4AccountList where accountName contains UPDATED_ACCOUNT_NAME
        defaultMt4AccountShouldNotBeFound("accountName.contains=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountNameNotContainsSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountName does not contain DEFAULT_ACCOUNT_NAME
        defaultMt4AccountShouldNotBeFound("accountName.doesNotContain=" + DEFAULT_ACCOUNT_NAME);

        // Get all the mt4AccountList where accountName does not contain UPDATED_ACCOUNT_NAME
        defaultMt4AccountShouldBeFound("accountName.doesNotContain=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountNumber equals to DEFAULT_ACCOUNT_NUMBER
        defaultMt4AccountShouldBeFound("accountNumber.equals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the mt4AccountList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultMt4AccountShouldNotBeFound("accountNumber.equals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountNumber not equals to DEFAULT_ACCOUNT_NUMBER
        defaultMt4AccountShouldNotBeFound("accountNumber.notEquals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the mt4AccountList where accountNumber not equals to UPDATED_ACCOUNT_NUMBER
        defaultMt4AccountShouldBeFound("accountNumber.notEquals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountNumber in DEFAULT_ACCOUNT_NUMBER or UPDATED_ACCOUNT_NUMBER
        defaultMt4AccountShouldBeFound("accountNumber.in=" + DEFAULT_ACCOUNT_NUMBER + "," + UPDATED_ACCOUNT_NUMBER);

        // Get all the mt4AccountList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultMt4AccountShouldNotBeFound("accountNumber.in=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountNumber is not null
        defaultMt4AccountShouldBeFound("accountNumber.specified=true");

        // Get all the mt4AccountList where accountNumber is null
        defaultMt4AccountShouldNotBeFound("accountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountNumber is greater than or equal to DEFAULT_ACCOUNT_NUMBER
        defaultMt4AccountShouldBeFound("accountNumber.greaterThanOrEqual=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the mt4AccountList where accountNumber is greater than or equal to UPDATED_ACCOUNT_NUMBER
        defaultMt4AccountShouldNotBeFound("accountNumber.greaterThanOrEqual=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountNumber is less than or equal to DEFAULT_ACCOUNT_NUMBER
        defaultMt4AccountShouldBeFound("accountNumber.lessThanOrEqual=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the mt4AccountList where accountNumber is less than or equal to SMALLER_ACCOUNT_NUMBER
        defaultMt4AccountShouldNotBeFound("accountNumber.lessThanOrEqual=" + SMALLER_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountNumber is less than DEFAULT_ACCOUNT_NUMBER
        defaultMt4AccountShouldNotBeFound("accountNumber.lessThan=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the mt4AccountList where accountNumber is less than UPDATED_ACCOUNT_NUMBER
        defaultMt4AccountShouldBeFound("accountNumber.lessThan=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountNumber is greater than DEFAULT_ACCOUNT_NUMBER
        defaultMt4AccountShouldNotBeFound("accountNumber.greaterThan=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the mt4AccountList where accountNumber is greater than SMALLER_ACCOUNT_NUMBER
        defaultMt4AccountShouldBeFound("accountNumber.greaterThan=" + SMALLER_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountProfitIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountProfit equals to DEFAULT_ACCOUNT_PROFIT
        defaultMt4AccountShouldBeFound("accountProfit.equals=" + DEFAULT_ACCOUNT_PROFIT);

        // Get all the mt4AccountList where accountProfit equals to UPDATED_ACCOUNT_PROFIT
        defaultMt4AccountShouldNotBeFound("accountProfit.equals=" + UPDATED_ACCOUNT_PROFIT);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountProfitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountProfit not equals to DEFAULT_ACCOUNT_PROFIT
        defaultMt4AccountShouldNotBeFound("accountProfit.notEquals=" + DEFAULT_ACCOUNT_PROFIT);

        // Get all the mt4AccountList where accountProfit not equals to UPDATED_ACCOUNT_PROFIT
        defaultMt4AccountShouldBeFound("accountProfit.notEquals=" + UPDATED_ACCOUNT_PROFIT);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountProfitIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountProfit in DEFAULT_ACCOUNT_PROFIT or UPDATED_ACCOUNT_PROFIT
        defaultMt4AccountShouldBeFound("accountProfit.in=" + DEFAULT_ACCOUNT_PROFIT + "," + UPDATED_ACCOUNT_PROFIT);

        // Get all the mt4AccountList where accountProfit equals to UPDATED_ACCOUNT_PROFIT
        defaultMt4AccountShouldNotBeFound("accountProfit.in=" + UPDATED_ACCOUNT_PROFIT);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountProfitIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountProfit is not null
        defaultMt4AccountShouldBeFound("accountProfit.specified=true");

        // Get all the mt4AccountList where accountProfit is null
        defaultMt4AccountShouldNotBeFound("accountProfit.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountProfitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountProfit is greater than or equal to DEFAULT_ACCOUNT_PROFIT
        defaultMt4AccountShouldBeFound("accountProfit.greaterThanOrEqual=" + DEFAULT_ACCOUNT_PROFIT);

        // Get all the mt4AccountList where accountProfit is greater than or equal to UPDATED_ACCOUNT_PROFIT
        defaultMt4AccountShouldNotBeFound("accountProfit.greaterThanOrEqual=" + UPDATED_ACCOUNT_PROFIT);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountProfitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountProfit is less than or equal to DEFAULT_ACCOUNT_PROFIT
        defaultMt4AccountShouldBeFound("accountProfit.lessThanOrEqual=" + DEFAULT_ACCOUNT_PROFIT);

        // Get all the mt4AccountList where accountProfit is less than or equal to SMALLER_ACCOUNT_PROFIT
        defaultMt4AccountShouldNotBeFound("accountProfit.lessThanOrEqual=" + SMALLER_ACCOUNT_PROFIT);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountProfitIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountProfit is less than DEFAULT_ACCOUNT_PROFIT
        defaultMt4AccountShouldNotBeFound("accountProfit.lessThan=" + DEFAULT_ACCOUNT_PROFIT);

        // Get all the mt4AccountList where accountProfit is less than UPDATED_ACCOUNT_PROFIT
        defaultMt4AccountShouldBeFound("accountProfit.lessThan=" + UPDATED_ACCOUNT_PROFIT);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountProfitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountProfit is greater than DEFAULT_ACCOUNT_PROFIT
        defaultMt4AccountShouldNotBeFound("accountProfit.greaterThan=" + DEFAULT_ACCOUNT_PROFIT);

        // Get all the mt4AccountList where accountProfit is greater than SMALLER_ACCOUNT_PROFIT
        defaultMt4AccountShouldBeFound("accountProfit.greaterThan=" + SMALLER_ACCOUNT_PROFIT);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountServerIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountServer equals to DEFAULT_ACCOUNT_SERVER
        defaultMt4AccountShouldBeFound("accountServer.equals=" + DEFAULT_ACCOUNT_SERVER);

        // Get all the mt4AccountList where accountServer equals to UPDATED_ACCOUNT_SERVER
        defaultMt4AccountShouldNotBeFound("accountServer.equals=" + UPDATED_ACCOUNT_SERVER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountServerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountServer not equals to DEFAULT_ACCOUNT_SERVER
        defaultMt4AccountShouldNotBeFound("accountServer.notEquals=" + DEFAULT_ACCOUNT_SERVER);

        // Get all the mt4AccountList where accountServer not equals to UPDATED_ACCOUNT_SERVER
        defaultMt4AccountShouldBeFound("accountServer.notEquals=" + UPDATED_ACCOUNT_SERVER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountServerIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountServer in DEFAULT_ACCOUNT_SERVER or UPDATED_ACCOUNT_SERVER
        defaultMt4AccountShouldBeFound("accountServer.in=" + DEFAULT_ACCOUNT_SERVER + "," + UPDATED_ACCOUNT_SERVER);

        // Get all the mt4AccountList where accountServer equals to UPDATED_ACCOUNT_SERVER
        defaultMt4AccountShouldNotBeFound("accountServer.in=" + UPDATED_ACCOUNT_SERVER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountServerIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountServer is not null
        defaultMt4AccountShouldBeFound("accountServer.specified=true");

        // Get all the mt4AccountList where accountServer is null
        defaultMt4AccountShouldNotBeFound("accountServer.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountServerContainsSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountServer contains DEFAULT_ACCOUNT_SERVER
        defaultMt4AccountShouldBeFound("accountServer.contains=" + DEFAULT_ACCOUNT_SERVER);

        // Get all the mt4AccountList where accountServer contains UPDATED_ACCOUNT_SERVER
        defaultMt4AccountShouldNotBeFound("accountServer.contains=" + UPDATED_ACCOUNT_SERVER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountServerNotContainsSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountServer does not contain DEFAULT_ACCOUNT_SERVER
        defaultMt4AccountShouldNotBeFound("accountServer.doesNotContain=" + DEFAULT_ACCOUNT_SERVER);

        // Get all the mt4AccountList where accountServer does not contain UPDATED_ACCOUNT_SERVER
        defaultMt4AccountShouldBeFound("accountServer.doesNotContain=" + UPDATED_ACCOUNT_SERVER);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountStopoutLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountStopoutLevel equals to DEFAULT_ACCOUNT_STOPOUT_LEVEL
        defaultMt4AccountShouldBeFound("accountStopoutLevel.equals=" + DEFAULT_ACCOUNT_STOPOUT_LEVEL);

        // Get all the mt4AccountList where accountStopoutLevel equals to UPDATED_ACCOUNT_STOPOUT_LEVEL
        defaultMt4AccountShouldNotBeFound("accountStopoutLevel.equals=" + UPDATED_ACCOUNT_STOPOUT_LEVEL);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountStopoutLevelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountStopoutLevel not equals to DEFAULT_ACCOUNT_STOPOUT_LEVEL
        defaultMt4AccountShouldNotBeFound("accountStopoutLevel.notEquals=" + DEFAULT_ACCOUNT_STOPOUT_LEVEL);

        // Get all the mt4AccountList where accountStopoutLevel not equals to UPDATED_ACCOUNT_STOPOUT_LEVEL
        defaultMt4AccountShouldBeFound("accountStopoutLevel.notEquals=" + UPDATED_ACCOUNT_STOPOUT_LEVEL);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountStopoutLevelIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountStopoutLevel in DEFAULT_ACCOUNT_STOPOUT_LEVEL or UPDATED_ACCOUNT_STOPOUT_LEVEL
        defaultMt4AccountShouldBeFound("accountStopoutLevel.in=" + DEFAULT_ACCOUNT_STOPOUT_LEVEL + "," + UPDATED_ACCOUNT_STOPOUT_LEVEL);

        // Get all the mt4AccountList where accountStopoutLevel equals to UPDATED_ACCOUNT_STOPOUT_LEVEL
        defaultMt4AccountShouldNotBeFound("accountStopoutLevel.in=" + UPDATED_ACCOUNT_STOPOUT_LEVEL);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountStopoutLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountStopoutLevel is not null
        defaultMt4AccountShouldBeFound("accountStopoutLevel.specified=true");

        // Get all the mt4AccountList where accountStopoutLevel is null
        defaultMt4AccountShouldNotBeFound("accountStopoutLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountStopoutLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountStopoutLevel is greater than or equal to DEFAULT_ACCOUNT_STOPOUT_LEVEL
        defaultMt4AccountShouldBeFound("accountStopoutLevel.greaterThanOrEqual=" + DEFAULT_ACCOUNT_STOPOUT_LEVEL);

        // Get all the mt4AccountList where accountStopoutLevel is greater than or equal to UPDATED_ACCOUNT_STOPOUT_LEVEL
        defaultMt4AccountShouldNotBeFound("accountStopoutLevel.greaterThanOrEqual=" + UPDATED_ACCOUNT_STOPOUT_LEVEL);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountStopoutLevelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountStopoutLevel is less than or equal to DEFAULT_ACCOUNT_STOPOUT_LEVEL
        defaultMt4AccountShouldBeFound("accountStopoutLevel.lessThanOrEqual=" + DEFAULT_ACCOUNT_STOPOUT_LEVEL);

        // Get all the mt4AccountList where accountStopoutLevel is less than or equal to SMALLER_ACCOUNT_STOPOUT_LEVEL
        defaultMt4AccountShouldNotBeFound("accountStopoutLevel.lessThanOrEqual=" + SMALLER_ACCOUNT_STOPOUT_LEVEL);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountStopoutLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountStopoutLevel is less than DEFAULT_ACCOUNT_STOPOUT_LEVEL
        defaultMt4AccountShouldNotBeFound("accountStopoutLevel.lessThan=" + DEFAULT_ACCOUNT_STOPOUT_LEVEL);

        // Get all the mt4AccountList where accountStopoutLevel is less than UPDATED_ACCOUNT_STOPOUT_LEVEL
        defaultMt4AccountShouldBeFound("accountStopoutLevel.lessThan=" + UPDATED_ACCOUNT_STOPOUT_LEVEL);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountStopoutLevelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountStopoutLevel is greater than DEFAULT_ACCOUNT_STOPOUT_LEVEL
        defaultMt4AccountShouldNotBeFound("accountStopoutLevel.greaterThan=" + DEFAULT_ACCOUNT_STOPOUT_LEVEL);

        // Get all the mt4AccountList where accountStopoutLevel is greater than SMALLER_ACCOUNT_STOPOUT_LEVEL
        defaultMt4AccountShouldBeFound("accountStopoutLevel.greaterThan=" + SMALLER_ACCOUNT_STOPOUT_LEVEL);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountStopoutModeIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountStopoutMode equals to DEFAULT_ACCOUNT_STOPOUT_MODE
        defaultMt4AccountShouldBeFound("accountStopoutMode.equals=" + DEFAULT_ACCOUNT_STOPOUT_MODE);

        // Get all the mt4AccountList where accountStopoutMode equals to UPDATED_ACCOUNT_STOPOUT_MODE
        defaultMt4AccountShouldNotBeFound("accountStopoutMode.equals=" + UPDATED_ACCOUNT_STOPOUT_MODE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountStopoutModeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountStopoutMode not equals to DEFAULT_ACCOUNT_STOPOUT_MODE
        defaultMt4AccountShouldNotBeFound("accountStopoutMode.notEquals=" + DEFAULT_ACCOUNT_STOPOUT_MODE);

        // Get all the mt4AccountList where accountStopoutMode not equals to UPDATED_ACCOUNT_STOPOUT_MODE
        defaultMt4AccountShouldBeFound("accountStopoutMode.notEquals=" + UPDATED_ACCOUNT_STOPOUT_MODE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountStopoutModeIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountStopoutMode in DEFAULT_ACCOUNT_STOPOUT_MODE or UPDATED_ACCOUNT_STOPOUT_MODE
        defaultMt4AccountShouldBeFound("accountStopoutMode.in=" + DEFAULT_ACCOUNT_STOPOUT_MODE + "," + UPDATED_ACCOUNT_STOPOUT_MODE);

        // Get all the mt4AccountList where accountStopoutMode equals to UPDATED_ACCOUNT_STOPOUT_MODE
        defaultMt4AccountShouldNotBeFound("accountStopoutMode.in=" + UPDATED_ACCOUNT_STOPOUT_MODE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountStopoutModeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountStopoutMode is not null
        defaultMt4AccountShouldBeFound("accountStopoutMode.specified=true");

        // Get all the mt4AccountList where accountStopoutMode is null
        defaultMt4AccountShouldNotBeFound("accountStopoutMode.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountStopoutModeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountStopoutMode is greater than or equal to DEFAULT_ACCOUNT_STOPOUT_MODE
        defaultMt4AccountShouldBeFound("accountStopoutMode.greaterThanOrEqual=" + DEFAULT_ACCOUNT_STOPOUT_MODE);

        // Get all the mt4AccountList where accountStopoutMode is greater than or equal to UPDATED_ACCOUNT_STOPOUT_MODE
        defaultMt4AccountShouldNotBeFound("accountStopoutMode.greaterThanOrEqual=" + UPDATED_ACCOUNT_STOPOUT_MODE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountStopoutModeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountStopoutMode is less than or equal to DEFAULT_ACCOUNT_STOPOUT_MODE
        defaultMt4AccountShouldBeFound("accountStopoutMode.lessThanOrEqual=" + DEFAULT_ACCOUNT_STOPOUT_MODE);

        // Get all the mt4AccountList where accountStopoutMode is less than or equal to SMALLER_ACCOUNT_STOPOUT_MODE
        defaultMt4AccountShouldNotBeFound("accountStopoutMode.lessThanOrEqual=" + SMALLER_ACCOUNT_STOPOUT_MODE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountStopoutModeIsLessThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountStopoutMode is less than DEFAULT_ACCOUNT_STOPOUT_MODE
        defaultMt4AccountShouldNotBeFound("accountStopoutMode.lessThan=" + DEFAULT_ACCOUNT_STOPOUT_MODE);

        // Get all the mt4AccountList where accountStopoutMode is less than UPDATED_ACCOUNT_STOPOUT_MODE
        defaultMt4AccountShouldBeFound("accountStopoutMode.lessThan=" + UPDATED_ACCOUNT_STOPOUT_MODE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountStopoutModeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where accountStopoutMode is greater than DEFAULT_ACCOUNT_STOPOUT_MODE
        defaultMt4AccountShouldNotBeFound("accountStopoutMode.greaterThan=" + DEFAULT_ACCOUNT_STOPOUT_MODE);

        // Get all the mt4AccountList where accountStopoutMode is greater than SMALLER_ACCOUNT_STOPOUT_MODE
        defaultMt4AccountShouldBeFound("accountStopoutMode.greaterThan=" + SMALLER_ACCOUNT_STOPOUT_MODE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByInActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where inActive equals to DEFAULT_IN_ACTIVE
        defaultMt4AccountShouldBeFound("inActive.equals=" + DEFAULT_IN_ACTIVE);

        // Get all the mt4AccountList where inActive equals to UPDATED_IN_ACTIVE
        defaultMt4AccountShouldNotBeFound("inActive.equals=" + UPDATED_IN_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByInActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where inActive not equals to DEFAULT_IN_ACTIVE
        defaultMt4AccountShouldNotBeFound("inActive.notEquals=" + DEFAULT_IN_ACTIVE);

        // Get all the mt4AccountList where inActive not equals to UPDATED_IN_ACTIVE
        defaultMt4AccountShouldBeFound("inActive.notEquals=" + UPDATED_IN_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByInActiveIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where inActive in DEFAULT_IN_ACTIVE or UPDATED_IN_ACTIVE
        defaultMt4AccountShouldBeFound("inActive.in=" + DEFAULT_IN_ACTIVE + "," + UPDATED_IN_ACTIVE);

        // Get all the mt4AccountList where inActive equals to UPDATED_IN_ACTIVE
        defaultMt4AccountShouldNotBeFound("inActive.in=" + UPDATED_IN_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByInActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where inActive is not null
        defaultMt4AccountShouldBeFound("inActive.specified=true");

        // Get all the mt4AccountList where inActive is null
        defaultMt4AccountShouldNotBeFound("inActive.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByInActiveDateIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where inActiveDate equals to DEFAULT_IN_ACTIVE_DATE
        defaultMt4AccountShouldBeFound("inActiveDate.equals=" + DEFAULT_IN_ACTIVE_DATE);

        // Get all the mt4AccountList where inActiveDate equals to UPDATED_IN_ACTIVE_DATE
        defaultMt4AccountShouldNotBeFound("inActiveDate.equals=" + UPDATED_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByInActiveDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where inActiveDate not equals to DEFAULT_IN_ACTIVE_DATE
        defaultMt4AccountShouldNotBeFound("inActiveDate.notEquals=" + DEFAULT_IN_ACTIVE_DATE);

        // Get all the mt4AccountList where inActiveDate not equals to UPDATED_IN_ACTIVE_DATE
        defaultMt4AccountShouldBeFound("inActiveDate.notEquals=" + UPDATED_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByInActiveDateIsInShouldWork() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where inActiveDate in DEFAULT_IN_ACTIVE_DATE or UPDATED_IN_ACTIVE_DATE
        defaultMt4AccountShouldBeFound("inActiveDate.in=" + DEFAULT_IN_ACTIVE_DATE + "," + UPDATED_IN_ACTIVE_DATE);

        // Get all the mt4AccountList where inActiveDate equals to UPDATED_IN_ACTIVE_DATE
        defaultMt4AccountShouldNotBeFound("inActiveDate.in=" + UPDATED_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void getAllMt4AccountsByInActiveDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        // Get all the mt4AccountList where inActiveDate is not null
        defaultMt4AccountShouldBeFound("inActiveDate.specified=true");

        // Get all the mt4AccountList where inActiveDate is null
        defaultMt4AccountShouldNotBeFound("inActiveDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMt4AccountsByTradeChallengeIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);
        TradeChallenge tradeChallenge = TradeChallengeResourceIT.createEntity(em);
        em.persist(tradeChallenge);
        em.flush();
        mt4Account.setTradeChallenge(tradeChallenge);
        tradeChallenge.setMt4Account(mt4Account);
        mt4AccountRepository.saveAndFlush(mt4Account);
        Long tradeChallengeId = tradeChallenge.getId();

        // Get all the mt4AccountList where tradeChallenge equals to tradeChallengeId
        defaultMt4AccountShouldBeFound("tradeChallengeId.equals=" + tradeChallengeId);

        // Get all the mt4AccountList where tradeChallenge equals to (tradeChallengeId + 1)
        defaultMt4AccountShouldNotBeFound("tradeChallengeId.equals=" + (tradeChallengeId + 1));
    }

    @Test
    @Transactional
    void getAllMt4AccountsByMt4TradeIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);
        Mt4Trade mt4Trade = Mt4TradeResourceIT.createEntity(em);
        em.persist(mt4Trade);
        em.flush();
        mt4Account.addMt4Trade(mt4Trade);
        mt4AccountRepository.saveAndFlush(mt4Account);
        Long mt4TradeId = mt4Trade.getId();

        // Get all the mt4AccountList where mt4Trade equals to mt4TradeId
        defaultMt4AccountShouldBeFound("mt4TradeId.equals=" + mt4TradeId);

        // Get all the mt4AccountList where mt4Trade equals to (mt4TradeId + 1)
        defaultMt4AccountShouldNotBeFound("mt4TradeId.equals=" + (mt4TradeId + 1));
    }

    @Test
    @Transactional
    void getAllMt4AccountsByAccountDataTimeSeriesIsEqualToSomething() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);
        AccountDataTimeSeries accountDataTimeSeries = AccountDataTimeSeriesResourceIT.createEntity(em);
        em.persist(accountDataTimeSeries);
        em.flush();
        mt4Account.addAccountDataTimeSeries(accountDataTimeSeries);
        mt4AccountRepository.saveAndFlush(mt4Account);
        Long accountDataTimeSeriesId = accountDataTimeSeries.getId();

        // Get all the mt4AccountList where accountDataTimeSeries equals to accountDataTimeSeriesId
        defaultMt4AccountShouldBeFound("accountDataTimeSeriesId.equals=" + accountDataTimeSeriesId);

        // Get all the mt4AccountList where accountDataTimeSeries equals to (accountDataTimeSeriesId + 1)
        defaultMt4AccountShouldNotBeFound("accountDataTimeSeriesId.equals=" + (accountDataTimeSeriesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMt4AccountShouldBeFound(String filter) throws Exception {
        restMt4AccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mt4Account.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].accountBroker").value(hasItem(DEFAULT_ACCOUNT_BROKER.toString())))
            .andExpect(jsonPath("$.[*].accountLogin").value(hasItem(DEFAULT_ACCOUNT_LOGIN)))
            .andExpect(jsonPath("$.[*].accountPassword").value(hasItem(DEFAULT_ACCOUNT_PASSWORD)))
            .andExpect(jsonPath("$.[*].accountInvestorLogin").value(hasItem(DEFAULT_ACCOUNT_INVESTOR_LOGIN)))
            .andExpect(jsonPath("$.[*].accountInvestorPassword").value(hasItem(DEFAULT_ACCOUNT_INVESTOR_PASSWORD)))
            .andExpect(jsonPath("$.[*].accountReal").value(hasItem(DEFAULT_ACCOUNT_REAL.booleanValue())))
            .andExpect(jsonPath("$.[*].accountInfoDouble").value(hasItem(DEFAULT_ACCOUNT_INFO_DOUBLE.doubleValue())))
            .andExpect(jsonPath("$.[*].accountInfoInteger").value(hasItem(DEFAULT_ACCOUNT_INFO_INTEGER)))
            .andExpect(jsonPath("$.[*].accountInfoString").value(hasItem(DEFAULT_ACCOUNT_INFO_STRING)))
            .andExpect(jsonPath("$.[*].accountBalance").value(hasItem(DEFAULT_ACCOUNT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].accountCredit").value(hasItem(DEFAULT_ACCOUNT_CREDIT.doubleValue())))
            .andExpect(jsonPath("$.[*].accountCompany").value(hasItem(DEFAULT_ACCOUNT_COMPANY)))
            .andExpect(jsonPath("$.[*].accountCurrency").value(hasItem(DEFAULT_ACCOUNT_CURRENCY)))
            .andExpect(jsonPath("$.[*].accountEquity").value(hasItem(DEFAULT_ACCOUNT_EQUITY.doubleValue())))
            .andExpect(jsonPath("$.[*].accountFreeMargin").value(hasItem(DEFAULT_ACCOUNT_FREE_MARGIN.doubleValue())))
            .andExpect(jsonPath("$.[*].accountFreeMarginCheck").value(hasItem(DEFAULT_ACCOUNT_FREE_MARGIN_CHECK.doubleValue())))
            .andExpect(jsonPath("$.[*].accountFreeMarginMode").value(hasItem(DEFAULT_ACCOUNT_FREE_MARGIN_MODE.doubleValue())))
            .andExpect(jsonPath("$.[*].accountLeverage").value(hasItem(DEFAULT_ACCOUNT_LEVERAGE)))
            .andExpect(jsonPath("$.[*].accountMargin").value(hasItem(DEFAULT_ACCOUNT_MARGIN.doubleValue())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].accountProfit").value(hasItem(DEFAULT_ACCOUNT_PROFIT.doubleValue())))
            .andExpect(jsonPath("$.[*].accountServer").value(hasItem(DEFAULT_ACCOUNT_SERVER)))
            .andExpect(jsonPath("$.[*].accountStopoutLevel").value(hasItem(DEFAULT_ACCOUNT_STOPOUT_LEVEL)))
            .andExpect(jsonPath("$.[*].accountStopoutMode").value(hasItem(DEFAULT_ACCOUNT_STOPOUT_MODE)))
            .andExpect(jsonPath("$.[*].inActive").value(hasItem(DEFAULT_IN_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].inActiveDate").value(hasItem(DEFAULT_IN_ACTIVE_DATE.toString())));

        // Check, that the count call also returns 1
        restMt4AccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMt4AccountShouldNotBeFound(String filter) throws Exception {
        restMt4AccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMt4AccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMt4Account() throws Exception {
        // Get the mt4Account
        restMt4AccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMt4Account() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        int databaseSizeBeforeUpdate = mt4AccountRepository.findAll().size();

        // Update the mt4Account
        Mt4Account updatedMt4Account = mt4AccountRepository.findById(mt4Account.getId()).get();
        // Disconnect from session so that the updates on updatedMt4Account are not directly saved in db
        em.detach(updatedMt4Account);
        updatedMt4Account
            .accountType(UPDATED_ACCOUNT_TYPE)
            .accountBroker(UPDATED_ACCOUNT_BROKER)
            .accountLogin(UPDATED_ACCOUNT_LOGIN)
            .accountPassword(UPDATED_ACCOUNT_PASSWORD)
            .accountInvestorLogin(UPDATED_ACCOUNT_INVESTOR_LOGIN)
            .accountInvestorPassword(UPDATED_ACCOUNT_INVESTOR_PASSWORD)
            .accountReal(UPDATED_ACCOUNT_REAL)
            .accountInfoDouble(UPDATED_ACCOUNT_INFO_DOUBLE)
            .accountInfoInteger(UPDATED_ACCOUNT_INFO_INTEGER)
            .accountInfoString(UPDATED_ACCOUNT_INFO_STRING)
            .accountBalance(UPDATED_ACCOUNT_BALANCE)
            .accountCredit(UPDATED_ACCOUNT_CREDIT)
            .accountCompany(UPDATED_ACCOUNT_COMPANY)
            .accountCurrency(UPDATED_ACCOUNT_CURRENCY)
            .accountEquity(UPDATED_ACCOUNT_EQUITY)
            .accountFreeMargin(UPDATED_ACCOUNT_FREE_MARGIN)
            .accountFreeMarginCheck(UPDATED_ACCOUNT_FREE_MARGIN_CHECK)
            .accountFreeMarginMode(UPDATED_ACCOUNT_FREE_MARGIN_MODE)
            .accountLeverage(UPDATED_ACCOUNT_LEVERAGE)
            .accountMargin(UPDATED_ACCOUNT_MARGIN)
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .accountProfit(UPDATED_ACCOUNT_PROFIT)
            .accountServer(UPDATED_ACCOUNT_SERVER)
            .accountStopoutLevel(UPDATED_ACCOUNT_STOPOUT_LEVEL)
            .accountStopoutMode(UPDATED_ACCOUNT_STOPOUT_MODE)
            .inActive(UPDATED_IN_ACTIVE)
            .inActiveDate(UPDATED_IN_ACTIVE_DATE);

        restMt4AccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMt4Account.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMt4Account))
            )
            .andExpect(status().isOk());

        // Validate the Mt4Account in the database
        List<Mt4Account> mt4AccountList = mt4AccountRepository.findAll();
        assertThat(mt4AccountList).hasSize(databaseSizeBeforeUpdate);
        Mt4Account testMt4Account = mt4AccountList.get(mt4AccountList.size() - 1);
        assertThat(testMt4Account.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
        assertThat(testMt4Account.getAccountBroker()).isEqualTo(UPDATED_ACCOUNT_BROKER);
        assertThat(testMt4Account.getAccountLogin()).isEqualTo(UPDATED_ACCOUNT_LOGIN);
        assertThat(testMt4Account.getAccountPassword()).isEqualTo(UPDATED_ACCOUNT_PASSWORD);
        assertThat(testMt4Account.getAccountInvestorLogin()).isEqualTo(UPDATED_ACCOUNT_INVESTOR_LOGIN);
        assertThat(testMt4Account.getAccountInvestorPassword()).isEqualTo(UPDATED_ACCOUNT_INVESTOR_PASSWORD);
        assertThat(testMt4Account.getAccountReal()).isEqualTo(UPDATED_ACCOUNT_REAL);
        assertThat(testMt4Account.getAccountInfoDouble()).isEqualTo(UPDATED_ACCOUNT_INFO_DOUBLE);
        assertThat(testMt4Account.getAccountInfoInteger()).isEqualTo(UPDATED_ACCOUNT_INFO_INTEGER);
        assertThat(testMt4Account.getAccountInfoString()).isEqualTo(UPDATED_ACCOUNT_INFO_STRING);
        assertThat(testMt4Account.getAccountBalance()).isEqualTo(UPDATED_ACCOUNT_BALANCE);
        assertThat(testMt4Account.getAccountCredit()).isEqualTo(UPDATED_ACCOUNT_CREDIT);
        assertThat(testMt4Account.getAccountCompany()).isEqualTo(UPDATED_ACCOUNT_COMPANY);
        assertThat(testMt4Account.getAccountCurrency()).isEqualTo(UPDATED_ACCOUNT_CURRENCY);
        assertThat(testMt4Account.getAccountEquity()).isEqualTo(UPDATED_ACCOUNT_EQUITY);
        assertThat(testMt4Account.getAccountFreeMargin()).isEqualTo(UPDATED_ACCOUNT_FREE_MARGIN);
        assertThat(testMt4Account.getAccountFreeMarginCheck()).isEqualTo(UPDATED_ACCOUNT_FREE_MARGIN_CHECK);
        assertThat(testMt4Account.getAccountFreeMarginMode()).isEqualTo(UPDATED_ACCOUNT_FREE_MARGIN_MODE);
        assertThat(testMt4Account.getAccountLeverage()).isEqualTo(UPDATED_ACCOUNT_LEVERAGE);
        assertThat(testMt4Account.getAccountMargin()).isEqualTo(UPDATED_ACCOUNT_MARGIN);
        assertThat(testMt4Account.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testMt4Account.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testMt4Account.getAccountProfit()).isEqualTo(UPDATED_ACCOUNT_PROFIT);
        assertThat(testMt4Account.getAccountServer()).isEqualTo(UPDATED_ACCOUNT_SERVER);
        assertThat(testMt4Account.getAccountStopoutLevel()).isEqualTo(UPDATED_ACCOUNT_STOPOUT_LEVEL);
        assertThat(testMt4Account.getAccountStopoutMode()).isEqualTo(UPDATED_ACCOUNT_STOPOUT_MODE);
        assertThat(testMt4Account.getInActive()).isEqualTo(UPDATED_IN_ACTIVE);
        assertThat(testMt4Account.getInActiveDate()).isEqualTo(UPDATED_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void putNonExistingMt4Account() throws Exception {
        int databaseSizeBeforeUpdate = mt4AccountRepository.findAll().size();
        mt4Account.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMt4AccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mt4Account.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mt4Account))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mt4Account in the database
        List<Mt4Account> mt4AccountList = mt4AccountRepository.findAll();
        assertThat(mt4AccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMt4Account() throws Exception {
        int databaseSizeBeforeUpdate = mt4AccountRepository.findAll().size();
        mt4Account.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMt4AccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mt4Account))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mt4Account in the database
        List<Mt4Account> mt4AccountList = mt4AccountRepository.findAll();
        assertThat(mt4AccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMt4Account() throws Exception {
        int databaseSizeBeforeUpdate = mt4AccountRepository.findAll().size();
        mt4Account.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMt4AccountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mt4Account)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mt4Account in the database
        List<Mt4Account> mt4AccountList = mt4AccountRepository.findAll();
        assertThat(mt4AccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMt4AccountWithPatch() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        int databaseSizeBeforeUpdate = mt4AccountRepository.findAll().size();

        // Update the mt4Account using partial update
        Mt4Account partialUpdatedMt4Account = new Mt4Account();
        partialUpdatedMt4Account.setId(mt4Account.getId());

        partialUpdatedMt4Account
            .accountType(UPDATED_ACCOUNT_TYPE)
            .accountBroker(UPDATED_ACCOUNT_BROKER)
            .accountPassword(UPDATED_ACCOUNT_PASSWORD)
            .accountInvestorPassword(UPDATED_ACCOUNT_INVESTOR_PASSWORD)
            .accountInfoInteger(UPDATED_ACCOUNT_INFO_INTEGER)
            .accountBalance(UPDATED_ACCOUNT_BALANCE)
            .accountCredit(UPDATED_ACCOUNT_CREDIT)
            .accountCompany(UPDATED_ACCOUNT_COMPANY)
            .accountProfit(UPDATED_ACCOUNT_PROFIT)
            .accountStopoutLevel(UPDATED_ACCOUNT_STOPOUT_LEVEL)
            .inActive(UPDATED_IN_ACTIVE);

        restMt4AccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMt4Account.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMt4Account))
            )
            .andExpect(status().isOk());

        // Validate the Mt4Account in the database
        List<Mt4Account> mt4AccountList = mt4AccountRepository.findAll();
        assertThat(mt4AccountList).hasSize(databaseSizeBeforeUpdate);
        Mt4Account testMt4Account = mt4AccountList.get(mt4AccountList.size() - 1);
        assertThat(testMt4Account.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
        assertThat(testMt4Account.getAccountBroker()).isEqualTo(UPDATED_ACCOUNT_BROKER);
        assertThat(testMt4Account.getAccountLogin()).isEqualTo(DEFAULT_ACCOUNT_LOGIN);
        assertThat(testMt4Account.getAccountPassword()).isEqualTo(UPDATED_ACCOUNT_PASSWORD);
        assertThat(testMt4Account.getAccountInvestorLogin()).isEqualTo(DEFAULT_ACCOUNT_INVESTOR_LOGIN);
        assertThat(testMt4Account.getAccountInvestorPassword()).isEqualTo(UPDATED_ACCOUNT_INVESTOR_PASSWORD);
        assertThat(testMt4Account.getAccountReal()).isEqualTo(DEFAULT_ACCOUNT_REAL);
        assertThat(testMt4Account.getAccountInfoDouble()).isEqualTo(DEFAULT_ACCOUNT_INFO_DOUBLE);
        assertThat(testMt4Account.getAccountInfoInteger()).isEqualTo(UPDATED_ACCOUNT_INFO_INTEGER);
        assertThat(testMt4Account.getAccountInfoString()).isEqualTo(DEFAULT_ACCOUNT_INFO_STRING);
        assertThat(testMt4Account.getAccountBalance()).isEqualTo(UPDATED_ACCOUNT_BALANCE);
        assertThat(testMt4Account.getAccountCredit()).isEqualTo(UPDATED_ACCOUNT_CREDIT);
        assertThat(testMt4Account.getAccountCompany()).isEqualTo(UPDATED_ACCOUNT_COMPANY);
        assertThat(testMt4Account.getAccountCurrency()).isEqualTo(DEFAULT_ACCOUNT_CURRENCY);
        assertThat(testMt4Account.getAccountEquity()).isEqualTo(DEFAULT_ACCOUNT_EQUITY);
        assertThat(testMt4Account.getAccountFreeMargin()).isEqualTo(DEFAULT_ACCOUNT_FREE_MARGIN);
        assertThat(testMt4Account.getAccountFreeMarginCheck()).isEqualTo(DEFAULT_ACCOUNT_FREE_MARGIN_CHECK);
        assertThat(testMt4Account.getAccountFreeMarginMode()).isEqualTo(DEFAULT_ACCOUNT_FREE_MARGIN_MODE);
        assertThat(testMt4Account.getAccountLeverage()).isEqualTo(DEFAULT_ACCOUNT_LEVERAGE);
        assertThat(testMt4Account.getAccountMargin()).isEqualTo(DEFAULT_ACCOUNT_MARGIN);
        assertThat(testMt4Account.getAccountName()).isEqualTo(DEFAULT_ACCOUNT_NAME);
        assertThat(testMt4Account.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testMt4Account.getAccountProfit()).isEqualTo(UPDATED_ACCOUNT_PROFIT);
        assertThat(testMt4Account.getAccountServer()).isEqualTo(DEFAULT_ACCOUNT_SERVER);
        assertThat(testMt4Account.getAccountStopoutLevel()).isEqualTo(UPDATED_ACCOUNT_STOPOUT_LEVEL);
        assertThat(testMt4Account.getAccountStopoutMode()).isEqualTo(DEFAULT_ACCOUNT_STOPOUT_MODE);
        assertThat(testMt4Account.getInActive()).isEqualTo(UPDATED_IN_ACTIVE);
        assertThat(testMt4Account.getInActiveDate()).isEqualTo(DEFAULT_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void fullUpdateMt4AccountWithPatch() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        int databaseSizeBeforeUpdate = mt4AccountRepository.findAll().size();

        // Update the mt4Account using partial update
        Mt4Account partialUpdatedMt4Account = new Mt4Account();
        partialUpdatedMt4Account.setId(mt4Account.getId());

        partialUpdatedMt4Account
            .accountType(UPDATED_ACCOUNT_TYPE)
            .accountBroker(UPDATED_ACCOUNT_BROKER)
            .accountLogin(UPDATED_ACCOUNT_LOGIN)
            .accountPassword(UPDATED_ACCOUNT_PASSWORD)
            .accountInvestorLogin(UPDATED_ACCOUNT_INVESTOR_LOGIN)
            .accountInvestorPassword(UPDATED_ACCOUNT_INVESTOR_PASSWORD)
            .accountReal(UPDATED_ACCOUNT_REAL)
            .accountInfoDouble(UPDATED_ACCOUNT_INFO_DOUBLE)
            .accountInfoInteger(UPDATED_ACCOUNT_INFO_INTEGER)
            .accountInfoString(UPDATED_ACCOUNT_INFO_STRING)
            .accountBalance(UPDATED_ACCOUNT_BALANCE)
            .accountCredit(UPDATED_ACCOUNT_CREDIT)
            .accountCompany(UPDATED_ACCOUNT_COMPANY)
            .accountCurrency(UPDATED_ACCOUNT_CURRENCY)
            .accountEquity(UPDATED_ACCOUNT_EQUITY)
            .accountFreeMargin(UPDATED_ACCOUNT_FREE_MARGIN)
            .accountFreeMarginCheck(UPDATED_ACCOUNT_FREE_MARGIN_CHECK)
            .accountFreeMarginMode(UPDATED_ACCOUNT_FREE_MARGIN_MODE)
            .accountLeverage(UPDATED_ACCOUNT_LEVERAGE)
            .accountMargin(UPDATED_ACCOUNT_MARGIN)
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .accountProfit(UPDATED_ACCOUNT_PROFIT)
            .accountServer(UPDATED_ACCOUNT_SERVER)
            .accountStopoutLevel(UPDATED_ACCOUNT_STOPOUT_LEVEL)
            .accountStopoutMode(UPDATED_ACCOUNT_STOPOUT_MODE)
            .inActive(UPDATED_IN_ACTIVE)
            .inActiveDate(UPDATED_IN_ACTIVE_DATE);

        restMt4AccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMt4Account.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMt4Account))
            )
            .andExpect(status().isOk());

        // Validate the Mt4Account in the database
        List<Mt4Account> mt4AccountList = mt4AccountRepository.findAll();
        assertThat(mt4AccountList).hasSize(databaseSizeBeforeUpdate);
        Mt4Account testMt4Account = mt4AccountList.get(mt4AccountList.size() - 1);
        assertThat(testMt4Account.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
        assertThat(testMt4Account.getAccountBroker()).isEqualTo(UPDATED_ACCOUNT_BROKER);
        assertThat(testMt4Account.getAccountLogin()).isEqualTo(UPDATED_ACCOUNT_LOGIN);
        assertThat(testMt4Account.getAccountPassword()).isEqualTo(UPDATED_ACCOUNT_PASSWORD);
        assertThat(testMt4Account.getAccountInvestorLogin()).isEqualTo(UPDATED_ACCOUNT_INVESTOR_LOGIN);
        assertThat(testMt4Account.getAccountInvestorPassword()).isEqualTo(UPDATED_ACCOUNT_INVESTOR_PASSWORD);
        assertThat(testMt4Account.getAccountReal()).isEqualTo(UPDATED_ACCOUNT_REAL);
        assertThat(testMt4Account.getAccountInfoDouble()).isEqualTo(UPDATED_ACCOUNT_INFO_DOUBLE);
        assertThat(testMt4Account.getAccountInfoInteger()).isEqualTo(UPDATED_ACCOUNT_INFO_INTEGER);
        assertThat(testMt4Account.getAccountInfoString()).isEqualTo(UPDATED_ACCOUNT_INFO_STRING);
        assertThat(testMt4Account.getAccountBalance()).isEqualTo(UPDATED_ACCOUNT_BALANCE);
        assertThat(testMt4Account.getAccountCredit()).isEqualTo(UPDATED_ACCOUNT_CREDIT);
        assertThat(testMt4Account.getAccountCompany()).isEqualTo(UPDATED_ACCOUNT_COMPANY);
        assertThat(testMt4Account.getAccountCurrency()).isEqualTo(UPDATED_ACCOUNT_CURRENCY);
        assertThat(testMt4Account.getAccountEquity()).isEqualTo(UPDATED_ACCOUNT_EQUITY);
        assertThat(testMt4Account.getAccountFreeMargin()).isEqualTo(UPDATED_ACCOUNT_FREE_MARGIN);
        assertThat(testMt4Account.getAccountFreeMarginCheck()).isEqualTo(UPDATED_ACCOUNT_FREE_MARGIN_CHECK);
        assertThat(testMt4Account.getAccountFreeMarginMode()).isEqualTo(UPDATED_ACCOUNT_FREE_MARGIN_MODE);
        assertThat(testMt4Account.getAccountLeverage()).isEqualTo(UPDATED_ACCOUNT_LEVERAGE);
        assertThat(testMt4Account.getAccountMargin()).isEqualTo(UPDATED_ACCOUNT_MARGIN);
        assertThat(testMt4Account.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testMt4Account.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testMt4Account.getAccountProfit()).isEqualTo(UPDATED_ACCOUNT_PROFIT);
        assertThat(testMt4Account.getAccountServer()).isEqualTo(UPDATED_ACCOUNT_SERVER);
        assertThat(testMt4Account.getAccountStopoutLevel()).isEqualTo(UPDATED_ACCOUNT_STOPOUT_LEVEL);
        assertThat(testMt4Account.getAccountStopoutMode()).isEqualTo(UPDATED_ACCOUNT_STOPOUT_MODE);
        assertThat(testMt4Account.getInActive()).isEqualTo(UPDATED_IN_ACTIVE);
        assertThat(testMt4Account.getInActiveDate()).isEqualTo(UPDATED_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingMt4Account() throws Exception {
        int databaseSizeBeforeUpdate = mt4AccountRepository.findAll().size();
        mt4Account.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMt4AccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mt4Account.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mt4Account))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mt4Account in the database
        List<Mt4Account> mt4AccountList = mt4AccountRepository.findAll();
        assertThat(mt4AccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMt4Account() throws Exception {
        int databaseSizeBeforeUpdate = mt4AccountRepository.findAll().size();
        mt4Account.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMt4AccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mt4Account))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mt4Account in the database
        List<Mt4Account> mt4AccountList = mt4AccountRepository.findAll();
        assertThat(mt4AccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMt4Account() throws Exception {
        int databaseSizeBeforeUpdate = mt4AccountRepository.findAll().size();
        mt4Account.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMt4AccountMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mt4Account))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mt4Account in the database
        List<Mt4Account> mt4AccountList = mt4AccountRepository.findAll();
        assertThat(mt4AccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMt4Account() throws Exception {
        // Initialize the database
        mt4AccountRepository.saveAndFlush(mt4Account);

        int databaseSizeBeforeDelete = mt4AccountRepository.findAll().size();

        // Delete the mt4Account
        restMt4AccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, mt4Account.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mt4Account> mt4AccountList = mt4AccountRepository.findAll();
        assertThat(mt4AccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
