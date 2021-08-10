package com.gracefl.propfirm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gracefl.propfirm.IntegrationTest;
import com.gracefl.propfirm.domain.AccountDataTimeSeries;
import com.gracefl.propfirm.domain.Mt4Account;
import com.gracefl.propfirm.repository.AccountDataTimeSeriesRepository;
import com.gracefl.propfirm.service.criteria.AccountDataTimeSeriesCriteria;
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
 * Integration tests for the {@link AccountDataTimeSeriesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountDataTimeSeriesResourceIT {

    private static final Instant DEFAULT_DATE_STAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_STAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_ACCOUNT_BALANCE = 1D;
    private static final Double UPDATED_ACCOUNT_BALANCE = 2D;
    private static final Double SMALLER_ACCOUNT_BALANCE = 1D - 1D;

    private static final Double DEFAULT_ACCOUNT_EQUITY = 1D;
    private static final Double UPDATED_ACCOUNT_EQUITY = 2D;
    private static final Double SMALLER_ACCOUNT_EQUITY = 1D - 1D;

    private static final Double DEFAULT_ACCOUNT_CREDIT = 1D;
    private static final Double UPDATED_ACCOUNT_CREDIT = 2D;
    private static final Double SMALLER_ACCOUNT_CREDIT = 1D - 1D;

    private static final Double DEFAULT_ACCOUNT_FREE_MARGIN = 1D;
    private static final Double UPDATED_ACCOUNT_FREE_MARGIN = 2D;
    private static final Double SMALLER_ACCOUNT_FREE_MARGIN = 1D - 1D;

    private static final Integer DEFAULT_ACCOUNT_STOPOUT_LEVEL = 1;
    private static final Integer UPDATED_ACCOUNT_STOPOUT_LEVEL = 2;
    private static final Integer SMALLER_ACCOUNT_STOPOUT_LEVEL = 1 - 1;

    private static final Double DEFAULT_OPEN_LOTS = 1D;
    private static final Double UPDATED_OPEN_LOTS = 2D;
    private static final Double SMALLER_OPEN_LOTS = 1D - 1D;

    private static final Integer DEFAULT_OPEN_NUMBER_OF_TRADES = 1;
    private static final Integer UPDATED_OPEN_NUMBER_OF_TRADES = 2;
    private static final Integer SMALLER_OPEN_NUMBER_OF_TRADES = 1 - 1;

    private static final String ENTITY_API_URL = "/api/account-data-time-series";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccountDataTimeSeriesRepository accountDataTimeSeriesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountDataTimeSeriesMockMvc;

    private AccountDataTimeSeries accountDataTimeSeries;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountDataTimeSeries createEntity(EntityManager em) {
        AccountDataTimeSeries accountDataTimeSeries = new AccountDataTimeSeries()
            .dateStamp(DEFAULT_DATE_STAMP)
            .accountBalance(DEFAULT_ACCOUNT_BALANCE)
            .accountEquity(DEFAULT_ACCOUNT_EQUITY)
            .accountCredit(DEFAULT_ACCOUNT_CREDIT)
            .accountFreeMargin(DEFAULT_ACCOUNT_FREE_MARGIN)
            .accountStopoutLevel(DEFAULT_ACCOUNT_STOPOUT_LEVEL)
            .openLots(DEFAULT_OPEN_LOTS)
            .openNumberOfTrades(DEFAULT_OPEN_NUMBER_OF_TRADES);
        return accountDataTimeSeries;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountDataTimeSeries createUpdatedEntity(EntityManager em) {
        AccountDataTimeSeries accountDataTimeSeries = new AccountDataTimeSeries()
            .dateStamp(UPDATED_DATE_STAMP)
            .accountBalance(UPDATED_ACCOUNT_BALANCE)
            .accountEquity(UPDATED_ACCOUNT_EQUITY)
            .accountCredit(UPDATED_ACCOUNT_CREDIT)
            .accountFreeMargin(UPDATED_ACCOUNT_FREE_MARGIN)
            .accountStopoutLevel(UPDATED_ACCOUNT_STOPOUT_LEVEL)
            .openLots(UPDATED_OPEN_LOTS)
            .openNumberOfTrades(UPDATED_OPEN_NUMBER_OF_TRADES);
        return accountDataTimeSeries;
    }

    @BeforeEach
    public void initTest() {
        accountDataTimeSeries = createEntity(em);
    }

    @Test
    @Transactional
    void createAccountDataTimeSeries() throws Exception {
        int databaseSizeBeforeCreate = accountDataTimeSeriesRepository.findAll().size();
        // Create the AccountDataTimeSeries
        restAccountDataTimeSeriesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountDataTimeSeries))
            )
            .andExpect(status().isCreated());

        // Validate the AccountDataTimeSeries in the database
        List<AccountDataTimeSeries> accountDataTimeSeriesList = accountDataTimeSeriesRepository.findAll();
        assertThat(accountDataTimeSeriesList).hasSize(databaseSizeBeforeCreate + 1);
        AccountDataTimeSeries testAccountDataTimeSeries = accountDataTimeSeriesList.get(accountDataTimeSeriesList.size() - 1);
        assertThat(testAccountDataTimeSeries.getDateStamp()).isEqualTo(DEFAULT_DATE_STAMP);
        assertThat(testAccountDataTimeSeries.getAccountBalance()).isEqualTo(DEFAULT_ACCOUNT_BALANCE);
        assertThat(testAccountDataTimeSeries.getAccountEquity()).isEqualTo(DEFAULT_ACCOUNT_EQUITY);
        assertThat(testAccountDataTimeSeries.getAccountCredit()).isEqualTo(DEFAULT_ACCOUNT_CREDIT);
        assertThat(testAccountDataTimeSeries.getAccountFreeMargin()).isEqualTo(DEFAULT_ACCOUNT_FREE_MARGIN);
        assertThat(testAccountDataTimeSeries.getAccountStopoutLevel()).isEqualTo(DEFAULT_ACCOUNT_STOPOUT_LEVEL);
        assertThat(testAccountDataTimeSeries.getOpenLots()).isEqualTo(DEFAULT_OPEN_LOTS);
        assertThat(testAccountDataTimeSeries.getOpenNumberOfTrades()).isEqualTo(DEFAULT_OPEN_NUMBER_OF_TRADES);
    }

    @Test
    @Transactional
    void createAccountDataTimeSeriesWithExistingId() throws Exception {
        // Create the AccountDataTimeSeries with an existing ID
        accountDataTimeSeries.setId(1L);

        int databaseSizeBeforeCreate = accountDataTimeSeriesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountDataTimeSeriesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountDataTimeSeries))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountDataTimeSeries in the database
        List<AccountDataTimeSeries> accountDataTimeSeriesList = accountDataTimeSeriesRepository.findAll();
        assertThat(accountDataTimeSeriesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeries() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList
        restAccountDataTimeSeriesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountDataTimeSeries.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateStamp").value(hasItem(DEFAULT_DATE_STAMP.toString())))
            .andExpect(jsonPath("$.[*].accountBalance").value(hasItem(DEFAULT_ACCOUNT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].accountEquity").value(hasItem(DEFAULT_ACCOUNT_EQUITY.doubleValue())))
            .andExpect(jsonPath("$.[*].accountCredit").value(hasItem(DEFAULT_ACCOUNT_CREDIT.doubleValue())))
            .andExpect(jsonPath("$.[*].accountFreeMargin").value(hasItem(DEFAULT_ACCOUNT_FREE_MARGIN.doubleValue())))
            .andExpect(jsonPath("$.[*].accountStopoutLevel").value(hasItem(DEFAULT_ACCOUNT_STOPOUT_LEVEL)))
            .andExpect(jsonPath("$.[*].openLots").value(hasItem(DEFAULT_OPEN_LOTS.doubleValue())))
            .andExpect(jsonPath("$.[*].openNumberOfTrades").value(hasItem(DEFAULT_OPEN_NUMBER_OF_TRADES)));
    }

    @Test
    @Transactional
    void getAccountDataTimeSeries() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get the accountDataTimeSeries
        restAccountDataTimeSeriesMockMvc
            .perform(get(ENTITY_API_URL_ID, accountDataTimeSeries.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountDataTimeSeries.getId().intValue()))
            .andExpect(jsonPath("$.dateStamp").value(DEFAULT_DATE_STAMP.toString()))
            .andExpect(jsonPath("$.accountBalance").value(DEFAULT_ACCOUNT_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.accountEquity").value(DEFAULT_ACCOUNT_EQUITY.doubleValue()))
            .andExpect(jsonPath("$.accountCredit").value(DEFAULT_ACCOUNT_CREDIT.doubleValue()))
            .andExpect(jsonPath("$.accountFreeMargin").value(DEFAULT_ACCOUNT_FREE_MARGIN.doubleValue()))
            .andExpect(jsonPath("$.accountStopoutLevel").value(DEFAULT_ACCOUNT_STOPOUT_LEVEL))
            .andExpect(jsonPath("$.openLots").value(DEFAULT_OPEN_LOTS.doubleValue()))
            .andExpect(jsonPath("$.openNumberOfTrades").value(DEFAULT_OPEN_NUMBER_OF_TRADES));
    }

    @Test
    @Transactional
    void getAccountDataTimeSeriesByIdFiltering() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        Long id = accountDataTimeSeries.getId();

        defaultAccountDataTimeSeriesShouldBeFound("id.equals=" + id);
        defaultAccountDataTimeSeriesShouldNotBeFound("id.notEquals=" + id);

        defaultAccountDataTimeSeriesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAccountDataTimeSeriesShouldNotBeFound("id.greaterThan=" + id);

        defaultAccountDataTimeSeriesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAccountDataTimeSeriesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByDateStampIsEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where dateStamp equals to DEFAULT_DATE_STAMP
        defaultAccountDataTimeSeriesShouldBeFound("dateStamp.equals=" + DEFAULT_DATE_STAMP);

        // Get all the accountDataTimeSeriesList where dateStamp equals to UPDATED_DATE_STAMP
        defaultAccountDataTimeSeriesShouldNotBeFound("dateStamp.equals=" + UPDATED_DATE_STAMP);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByDateStampIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where dateStamp not equals to DEFAULT_DATE_STAMP
        defaultAccountDataTimeSeriesShouldNotBeFound("dateStamp.notEquals=" + DEFAULT_DATE_STAMP);

        // Get all the accountDataTimeSeriesList where dateStamp not equals to UPDATED_DATE_STAMP
        defaultAccountDataTimeSeriesShouldBeFound("dateStamp.notEquals=" + UPDATED_DATE_STAMP);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByDateStampIsInShouldWork() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where dateStamp in DEFAULT_DATE_STAMP or UPDATED_DATE_STAMP
        defaultAccountDataTimeSeriesShouldBeFound("dateStamp.in=" + DEFAULT_DATE_STAMP + "," + UPDATED_DATE_STAMP);

        // Get all the accountDataTimeSeriesList where dateStamp equals to UPDATED_DATE_STAMP
        defaultAccountDataTimeSeriesShouldNotBeFound("dateStamp.in=" + UPDATED_DATE_STAMP);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByDateStampIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where dateStamp is not null
        defaultAccountDataTimeSeriesShouldBeFound("dateStamp.specified=true");

        // Get all the accountDataTimeSeriesList where dateStamp is null
        defaultAccountDataTimeSeriesShouldNotBeFound("dateStamp.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountBalance equals to DEFAULT_ACCOUNT_BALANCE
        defaultAccountDataTimeSeriesShouldBeFound("accountBalance.equals=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the accountDataTimeSeriesList where accountBalance equals to UPDATED_ACCOUNT_BALANCE
        defaultAccountDataTimeSeriesShouldNotBeFound("accountBalance.equals=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountBalance not equals to DEFAULT_ACCOUNT_BALANCE
        defaultAccountDataTimeSeriesShouldNotBeFound("accountBalance.notEquals=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the accountDataTimeSeriesList where accountBalance not equals to UPDATED_ACCOUNT_BALANCE
        defaultAccountDataTimeSeriesShouldBeFound("accountBalance.notEquals=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountBalance in DEFAULT_ACCOUNT_BALANCE or UPDATED_ACCOUNT_BALANCE
        defaultAccountDataTimeSeriesShouldBeFound("accountBalance.in=" + DEFAULT_ACCOUNT_BALANCE + "," + UPDATED_ACCOUNT_BALANCE);

        // Get all the accountDataTimeSeriesList where accountBalance equals to UPDATED_ACCOUNT_BALANCE
        defaultAccountDataTimeSeriesShouldNotBeFound("accountBalance.in=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountBalance is not null
        defaultAccountDataTimeSeriesShouldBeFound("accountBalance.specified=true");

        // Get all the accountDataTimeSeriesList where accountBalance is null
        defaultAccountDataTimeSeriesShouldNotBeFound("accountBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountBalance is greater than or equal to DEFAULT_ACCOUNT_BALANCE
        defaultAccountDataTimeSeriesShouldBeFound("accountBalance.greaterThanOrEqual=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the accountDataTimeSeriesList where accountBalance is greater than or equal to UPDATED_ACCOUNT_BALANCE
        defaultAccountDataTimeSeriesShouldNotBeFound("accountBalance.greaterThanOrEqual=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountBalance is less than or equal to DEFAULT_ACCOUNT_BALANCE
        defaultAccountDataTimeSeriesShouldBeFound("accountBalance.lessThanOrEqual=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the accountDataTimeSeriesList where accountBalance is less than or equal to SMALLER_ACCOUNT_BALANCE
        defaultAccountDataTimeSeriesShouldNotBeFound("accountBalance.lessThanOrEqual=" + SMALLER_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountBalance is less than DEFAULT_ACCOUNT_BALANCE
        defaultAccountDataTimeSeriesShouldNotBeFound("accountBalance.lessThan=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the accountDataTimeSeriesList where accountBalance is less than UPDATED_ACCOUNT_BALANCE
        defaultAccountDataTimeSeriesShouldBeFound("accountBalance.lessThan=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountBalance is greater than DEFAULT_ACCOUNT_BALANCE
        defaultAccountDataTimeSeriesShouldNotBeFound("accountBalance.greaterThan=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the accountDataTimeSeriesList where accountBalance is greater than SMALLER_ACCOUNT_BALANCE
        defaultAccountDataTimeSeriesShouldBeFound("accountBalance.greaterThan=" + SMALLER_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountEquityIsEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountEquity equals to DEFAULT_ACCOUNT_EQUITY
        defaultAccountDataTimeSeriesShouldBeFound("accountEquity.equals=" + DEFAULT_ACCOUNT_EQUITY);

        // Get all the accountDataTimeSeriesList where accountEquity equals to UPDATED_ACCOUNT_EQUITY
        defaultAccountDataTimeSeriesShouldNotBeFound("accountEquity.equals=" + UPDATED_ACCOUNT_EQUITY);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountEquityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountEquity not equals to DEFAULT_ACCOUNT_EQUITY
        defaultAccountDataTimeSeriesShouldNotBeFound("accountEquity.notEquals=" + DEFAULT_ACCOUNT_EQUITY);

        // Get all the accountDataTimeSeriesList where accountEquity not equals to UPDATED_ACCOUNT_EQUITY
        defaultAccountDataTimeSeriesShouldBeFound("accountEquity.notEquals=" + UPDATED_ACCOUNT_EQUITY);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountEquityIsInShouldWork() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountEquity in DEFAULT_ACCOUNT_EQUITY or UPDATED_ACCOUNT_EQUITY
        defaultAccountDataTimeSeriesShouldBeFound("accountEquity.in=" + DEFAULT_ACCOUNT_EQUITY + "," + UPDATED_ACCOUNT_EQUITY);

        // Get all the accountDataTimeSeriesList where accountEquity equals to UPDATED_ACCOUNT_EQUITY
        defaultAccountDataTimeSeriesShouldNotBeFound("accountEquity.in=" + UPDATED_ACCOUNT_EQUITY);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountEquityIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountEquity is not null
        defaultAccountDataTimeSeriesShouldBeFound("accountEquity.specified=true");

        // Get all the accountDataTimeSeriesList where accountEquity is null
        defaultAccountDataTimeSeriesShouldNotBeFound("accountEquity.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountEquityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountEquity is greater than or equal to DEFAULT_ACCOUNT_EQUITY
        defaultAccountDataTimeSeriesShouldBeFound("accountEquity.greaterThanOrEqual=" + DEFAULT_ACCOUNT_EQUITY);

        // Get all the accountDataTimeSeriesList where accountEquity is greater than or equal to UPDATED_ACCOUNT_EQUITY
        defaultAccountDataTimeSeriesShouldNotBeFound("accountEquity.greaterThanOrEqual=" + UPDATED_ACCOUNT_EQUITY);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountEquityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountEquity is less than or equal to DEFAULT_ACCOUNT_EQUITY
        defaultAccountDataTimeSeriesShouldBeFound("accountEquity.lessThanOrEqual=" + DEFAULT_ACCOUNT_EQUITY);

        // Get all the accountDataTimeSeriesList where accountEquity is less than or equal to SMALLER_ACCOUNT_EQUITY
        defaultAccountDataTimeSeriesShouldNotBeFound("accountEquity.lessThanOrEqual=" + SMALLER_ACCOUNT_EQUITY);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountEquityIsLessThanSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountEquity is less than DEFAULT_ACCOUNT_EQUITY
        defaultAccountDataTimeSeriesShouldNotBeFound("accountEquity.lessThan=" + DEFAULT_ACCOUNT_EQUITY);

        // Get all the accountDataTimeSeriesList where accountEquity is less than UPDATED_ACCOUNT_EQUITY
        defaultAccountDataTimeSeriesShouldBeFound("accountEquity.lessThan=" + UPDATED_ACCOUNT_EQUITY);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountEquityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountEquity is greater than DEFAULT_ACCOUNT_EQUITY
        defaultAccountDataTimeSeriesShouldNotBeFound("accountEquity.greaterThan=" + DEFAULT_ACCOUNT_EQUITY);

        // Get all the accountDataTimeSeriesList where accountEquity is greater than SMALLER_ACCOUNT_EQUITY
        defaultAccountDataTimeSeriesShouldBeFound("accountEquity.greaterThan=" + SMALLER_ACCOUNT_EQUITY);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountCreditIsEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountCredit equals to DEFAULT_ACCOUNT_CREDIT
        defaultAccountDataTimeSeriesShouldBeFound("accountCredit.equals=" + DEFAULT_ACCOUNT_CREDIT);

        // Get all the accountDataTimeSeriesList where accountCredit equals to UPDATED_ACCOUNT_CREDIT
        defaultAccountDataTimeSeriesShouldNotBeFound("accountCredit.equals=" + UPDATED_ACCOUNT_CREDIT);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountCreditIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountCredit not equals to DEFAULT_ACCOUNT_CREDIT
        defaultAccountDataTimeSeriesShouldNotBeFound("accountCredit.notEquals=" + DEFAULT_ACCOUNT_CREDIT);

        // Get all the accountDataTimeSeriesList where accountCredit not equals to UPDATED_ACCOUNT_CREDIT
        defaultAccountDataTimeSeriesShouldBeFound("accountCredit.notEquals=" + UPDATED_ACCOUNT_CREDIT);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountCreditIsInShouldWork() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountCredit in DEFAULT_ACCOUNT_CREDIT or UPDATED_ACCOUNT_CREDIT
        defaultAccountDataTimeSeriesShouldBeFound("accountCredit.in=" + DEFAULT_ACCOUNT_CREDIT + "," + UPDATED_ACCOUNT_CREDIT);

        // Get all the accountDataTimeSeriesList where accountCredit equals to UPDATED_ACCOUNT_CREDIT
        defaultAccountDataTimeSeriesShouldNotBeFound("accountCredit.in=" + UPDATED_ACCOUNT_CREDIT);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountCreditIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountCredit is not null
        defaultAccountDataTimeSeriesShouldBeFound("accountCredit.specified=true");

        // Get all the accountDataTimeSeriesList where accountCredit is null
        defaultAccountDataTimeSeriesShouldNotBeFound("accountCredit.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountCreditIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountCredit is greater than or equal to DEFAULT_ACCOUNT_CREDIT
        defaultAccountDataTimeSeriesShouldBeFound("accountCredit.greaterThanOrEqual=" + DEFAULT_ACCOUNT_CREDIT);

        // Get all the accountDataTimeSeriesList where accountCredit is greater than or equal to UPDATED_ACCOUNT_CREDIT
        defaultAccountDataTimeSeriesShouldNotBeFound("accountCredit.greaterThanOrEqual=" + UPDATED_ACCOUNT_CREDIT);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountCreditIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountCredit is less than or equal to DEFAULT_ACCOUNT_CREDIT
        defaultAccountDataTimeSeriesShouldBeFound("accountCredit.lessThanOrEqual=" + DEFAULT_ACCOUNT_CREDIT);

        // Get all the accountDataTimeSeriesList where accountCredit is less than or equal to SMALLER_ACCOUNT_CREDIT
        defaultAccountDataTimeSeriesShouldNotBeFound("accountCredit.lessThanOrEqual=" + SMALLER_ACCOUNT_CREDIT);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountCreditIsLessThanSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountCredit is less than DEFAULT_ACCOUNT_CREDIT
        defaultAccountDataTimeSeriesShouldNotBeFound("accountCredit.lessThan=" + DEFAULT_ACCOUNT_CREDIT);

        // Get all the accountDataTimeSeriesList where accountCredit is less than UPDATED_ACCOUNT_CREDIT
        defaultAccountDataTimeSeriesShouldBeFound("accountCredit.lessThan=" + UPDATED_ACCOUNT_CREDIT);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountCreditIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountCredit is greater than DEFAULT_ACCOUNT_CREDIT
        defaultAccountDataTimeSeriesShouldNotBeFound("accountCredit.greaterThan=" + DEFAULT_ACCOUNT_CREDIT);

        // Get all the accountDataTimeSeriesList where accountCredit is greater than SMALLER_ACCOUNT_CREDIT
        defaultAccountDataTimeSeriesShouldBeFound("accountCredit.greaterThan=" + SMALLER_ACCOUNT_CREDIT);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountFreeMarginIsEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountFreeMargin equals to DEFAULT_ACCOUNT_FREE_MARGIN
        defaultAccountDataTimeSeriesShouldBeFound("accountFreeMargin.equals=" + DEFAULT_ACCOUNT_FREE_MARGIN);

        // Get all the accountDataTimeSeriesList where accountFreeMargin equals to UPDATED_ACCOUNT_FREE_MARGIN
        defaultAccountDataTimeSeriesShouldNotBeFound("accountFreeMargin.equals=" + UPDATED_ACCOUNT_FREE_MARGIN);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountFreeMarginIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountFreeMargin not equals to DEFAULT_ACCOUNT_FREE_MARGIN
        defaultAccountDataTimeSeriesShouldNotBeFound("accountFreeMargin.notEquals=" + DEFAULT_ACCOUNT_FREE_MARGIN);

        // Get all the accountDataTimeSeriesList where accountFreeMargin not equals to UPDATED_ACCOUNT_FREE_MARGIN
        defaultAccountDataTimeSeriesShouldBeFound("accountFreeMargin.notEquals=" + UPDATED_ACCOUNT_FREE_MARGIN);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountFreeMarginIsInShouldWork() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountFreeMargin in DEFAULT_ACCOUNT_FREE_MARGIN or UPDATED_ACCOUNT_FREE_MARGIN
        defaultAccountDataTimeSeriesShouldBeFound(
            "accountFreeMargin.in=" + DEFAULT_ACCOUNT_FREE_MARGIN + "," + UPDATED_ACCOUNT_FREE_MARGIN
        );

        // Get all the accountDataTimeSeriesList where accountFreeMargin equals to UPDATED_ACCOUNT_FREE_MARGIN
        defaultAccountDataTimeSeriesShouldNotBeFound("accountFreeMargin.in=" + UPDATED_ACCOUNT_FREE_MARGIN);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountFreeMarginIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountFreeMargin is not null
        defaultAccountDataTimeSeriesShouldBeFound("accountFreeMargin.specified=true");

        // Get all the accountDataTimeSeriesList where accountFreeMargin is null
        defaultAccountDataTimeSeriesShouldNotBeFound("accountFreeMargin.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountFreeMarginIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountFreeMargin is greater than or equal to DEFAULT_ACCOUNT_FREE_MARGIN
        defaultAccountDataTimeSeriesShouldBeFound("accountFreeMargin.greaterThanOrEqual=" + DEFAULT_ACCOUNT_FREE_MARGIN);

        // Get all the accountDataTimeSeriesList where accountFreeMargin is greater than or equal to UPDATED_ACCOUNT_FREE_MARGIN
        defaultAccountDataTimeSeriesShouldNotBeFound("accountFreeMargin.greaterThanOrEqual=" + UPDATED_ACCOUNT_FREE_MARGIN);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountFreeMarginIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountFreeMargin is less than or equal to DEFAULT_ACCOUNT_FREE_MARGIN
        defaultAccountDataTimeSeriesShouldBeFound("accountFreeMargin.lessThanOrEqual=" + DEFAULT_ACCOUNT_FREE_MARGIN);

        // Get all the accountDataTimeSeriesList where accountFreeMargin is less than or equal to SMALLER_ACCOUNT_FREE_MARGIN
        defaultAccountDataTimeSeriesShouldNotBeFound("accountFreeMargin.lessThanOrEqual=" + SMALLER_ACCOUNT_FREE_MARGIN);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountFreeMarginIsLessThanSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountFreeMargin is less than DEFAULT_ACCOUNT_FREE_MARGIN
        defaultAccountDataTimeSeriesShouldNotBeFound("accountFreeMargin.lessThan=" + DEFAULT_ACCOUNT_FREE_MARGIN);

        // Get all the accountDataTimeSeriesList where accountFreeMargin is less than UPDATED_ACCOUNT_FREE_MARGIN
        defaultAccountDataTimeSeriesShouldBeFound("accountFreeMargin.lessThan=" + UPDATED_ACCOUNT_FREE_MARGIN);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountFreeMarginIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountFreeMargin is greater than DEFAULT_ACCOUNT_FREE_MARGIN
        defaultAccountDataTimeSeriesShouldNotBeFound("accountFreeMargin.greaterThan=" + DEFAULT_ACCOUNT_FREE_MARGIN);

        // Get all the accountDataTimeSeriesList where accountFreeMargin is greater than SMALLER_ACCOUNT_FREE_MARGIN
        defaultAccountDataTimeSeriesShouldBeFound("accountFreeMargin.greaterThan=" + SMALLER_ACCOUNT_FREE_MARGIN);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountStopoutLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountStopoutLevel equals to DEFAULT_ACCOUNT_STOPOUT_LEVEL
        defaultAccountDataTimeSeriesShouldBeFound("accountStopoutLevel.equals=" + DEFAULT_ACCOUNT_STOPOUT_LEVEL);

        // Get all the accountDataTimeSeriesList where accountStopoutLevel equals to UPDATED_ACCOUNT_STOPOUT_LEVEL
        defaultAccountDataTimeSeriesShouldNotBeFound("accountStopoutLevel.equals=" + UPDATED_ACCOUNT_STOPOUT_LEVEL);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountStopoutLevelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountStopoutLevel not equals to DEFAULT_ACCOUNT_STOPOUT_LEVEL
        defaultAccountDataTimeSeriesShouldNotBeFound("accountStopoutLevel.notEquals=" + DEFAULT_ACCOUNT_STOPOUT_LEVEL);

        // Get all the accountDataTimeSeriesList where accountStopoutLevel not equals to UPDATED_ACCOUNT_STOPOUT_LEVEL
        defaultAccountDataTimeSeriesShouldBeFound("accountStopoutLevel.notEquals=" + UPDATED_ACCOUNT_STOPOUT_LEVEL);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountStopoutLevelIsInShouldWork() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountStopoutLevel in DEFAULT_ACCOUNT_STOPOUT_LEVEL or UPDATED_ACCOUNT_STOPOUT_LEVEL
        defaultAccountDataTimeSeriesShouldBeFound(
            "accountStopoutLevel.in=" + DEFAULT_ACCOUNT_STOPOUT_LEVEL + "," + UPDATED_ACCOUNT_STOPOUT_LEVEL
        );

        // Get all the accountDataTimeSeriesList where accountStopoutLevel equals to UPDATED_ACCOUNT_STOPOUT_LEVEL
        defaultAccountDataTimeSeriesShouldNotBeFound("accountStopoutLevel.in=" + UPDATED_ACCOUNT_STOPOUT_LEVEL);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountStopoutLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountStopoutLevel is not null
        defaultAccountDataTimeSeriesShouldBeFound("accountStopoutLevel.specified=true");

        // Get all the accountDataTimeSeriesList where accountStopoutLevel is null
        defaultAccountDataTimeSeriesShouldNotBeFound("accountStopoutLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountStopoutLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountStopoutLevel is greater than or equal to DEFAULT_ACCOUNT_STOPOUT_LEVEL
        defaultAccountDataTimeSeriesShouldBeFound("accountStopoutLevel.greaterThanOrEqual=" + DEFAULT_ACCOUNT_STOPOUT_LEVEL);

        // Get all the accountDataTimeSeriesList where accountStopoutLevel is greater than or equal to UPDATED_ACCOUNT_STOPOUT_LEVEL
        defaultAccountDataTimeSeriesShouldNotBeFound("accountStopoutLevel.greaterThanOrEqual=" + UPDATED_ACCOUNT_STOPOUT_LEVEL);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountStopoutLevelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountStopoutLevel is less than or equal to DEFAULT_ACCOUNT_STOPOUT_LEVEL
        defaultAccountDataTimeSeriesShouldBeFound("accountStopoutLevel.lessThanOrEqual=" + DEFAULT_ACCOUNT_STOPOUT_LEVEL);

        // Get all the accountDataTimeSeriesList where accountStopoutLevel is less than or equal to SMALLER_ACCOUNT_STOPOUT_LEVEL
        defaultAccountDataTimeSeriesShouldNotBeFound("accountStopoutLevel.lessThanOrEqual=" + SMALLER_ACCOUNT_STOPOUT_LEVEL);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountStopoutLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountStopoutLevel is less than DEFAULT_ACCOUNT_STOPOUT_LEVEL
        defaultAccountDataTimeSeriesShouldNotBeFound("accountStopoutLevel.lessThan=" + DEFAULT_ACCOUNT_STOPOUT_LEVEL);

        // Get all the accountDataTimeSeriesList where accountStopoutLevel is less than UPDATED_ACCOUNT_STOPOUT_LEVEL
        defaultAccountDataTimeSeriesShouldBeFound("accountStopoutLevel.lessThan=" + UPDATED_ACCOUNT_STOPOUT_LEVEL);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByAccountStopoutLevelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where accountStopoutLevel is greater than DEFAULT_ACCOUNT_STOPOUT_LEVEL
        defaultAccountDataTimeSeriesShouldNotBeFound("accountStopoutLevel.greaterThan=" + DEFAULT_ACCOUNT_STOPOUT_LEVEL);

        // Get all the accountDataTimeSeriesList where accountStopoutLevel is greater than SMALLER_ACCOUNT_STOPOUT_LEVEL
        defaultAccountDataTimeSeriesShouldBeFound("accountStopoutLevel.greaterThan=" + SMALLER_ACCOUNT_STOPOUT_LEVEL);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByOpenLotsIsEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where openLots equals to DEFAULT_OPEN_LOTS
        defaultAccountDataTimeSeriesShouldBeFound("openLots.equals=" + DEFAULT_OPEN_LOTS);

        // Get all the accountDataTimeSeriesList where openLots equals to UPDATED_OPEN_LOTS
        defaultAccountDataTimeSeriesShouldNotBeFound("openLots.equals=" + UPDATED_OPEN_LOTS);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByOpenLotsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where openLots not equals to DEFAULT_OPEN_LOTS
        defaultAccountDataTimeSeriesShouldNotBeFound("openLots.notEquals=" + DEFAULT_OPEN_LOTS);

        // Get all the accountDataTimeSeriesList where openLots not equals to UPDATED_OPEN_LOTS
        defaultAccountDataTimeSeriesShouldBeFound("openLots.notEquals=" + UPDATED_OPEN_LOTS);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByOpenLotsIsInShouldWork() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where openLots in DEFAULT_OPEN_LOTS or UPDATED_OPEN_LOTS
        defaultAccountDataTimeSeriesShouldBeFound("openLots.in=" + DEFAULT_OPEN_LOTS + "," + UPDATED_OPEN_LOTS);

        // Get all the accountDataTimeSeriesList where openLots equals to UPDATED_OPEN_LOTS
        defaultAccountDataTimeSeriesShouldNotBeFound("openLots.in=" + UPDATED_OPEN_LOTS);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByOpenLotsIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where openLots is not null
        defaultAccountDataTimeSeriesShouldBeFound("openLots.specified=true");

        // Get all the accountDataTimeSeriesList where openLots is null
        defaultAccountDataTimeSeriesShouldNotBeFound("openLots.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByOpenLotsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where openLots is greater than or equal to DEFAULT_OPEN_LOTS
        defaultAccountDataTimeSeriesShouldBeFound("openLots.greaterThanOrEqual=" + DEFAULT_OPEN_LOTS);

        // Get all the accountDataTimeSeriesList where openLots is greater than or equal to UPDATED_OPEN_LOTS
        defaultAccountDataTimeSeriesShouldNotBeFound("openLots.greaterThanOrEqual=" + UPDATED_OPEN_LOTS);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByOpenLotsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where openLots is less than or equal to DEFAULT_OPEN_LOTS
        defaultAccountDataTimeSeriesShouldBeFound("openLots.lessThanOrEqual=" + DEFAULT_OPEN_LOTS);

        // Get all the accountDataTimeSeriesList where openLots is less than or equal to SMALLER_OPEN_LOTS
        defaultAccountDataTimeSeriesShouldNotBeFound("openLots.lessThanOrEqual=" + SMALLER_OPEN_LOTS);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByOpenLotsIsLessThanSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where openLots is less than DEFAULT_OPEN_LOTS
        defaultAccountDataTimeSeriesShouldNotBeFound("openLots.lessThan=" + DEFAULT_OPEN_LOTS);

        // Get all the accountDataTimeSeriesList where openLots is less than UPDATED_OPEN_LOTS
        defaultAccountDataTimeSeriesShouldBeFound("openLots.lessThan=" + UPDATED_OPEN_LOTS);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByOpenLotsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where openLots is greater than DEFAULT_OPEN_LOTS
        defaultAccountDataTimeSeriesShouldNotBeFound("openLots.greaterThan=" + DEFAULT_OPEN_LOTS);

        // Get all the accountDataTimeSeriesList where openLots is greater than SMALLER_OPEN_LOTS
        defaultAccountDataTimeSeriesShouldBeFound("openLots.greaterThan=" + SMALLER_OPEN_LOTS);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByOpenNumberOfTradesIsEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where openNumberOfTrades equals to DEFAULT_OPEN_NUMBER_OF_TRADES
        defaultAccountDataTimeSeriesShouldBeFound("openNumberOfTrades.equals=" + DEFAULT_OPEN_NUMBER_OF_TRADES);

        // Get all the accountDataTimeSeriesList where openNumberOfTrades equals to UPDATED_OPEN_NUMBER_OF_TRADES
        defaultAccountDataTimeSeriesShouldNotBeFound("openNumberOfTrades.equals=" + UPDATED_OPEN_NUMBER_OF_TRADES);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByOpenNumberOfTradesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where openNumberOfTrades not equals to DEFAULT_OPEN_NUMBER_OF_TRADES
        defaultAccountDataTimeSeriesShouldNotBeFound("openNumberOfTrades.notEquals=" + DEFAULT_OPEN_NUMBER_OF_TRADES);

        // Get all the accountDataTimeSeriesList where openNumberOfTrades not equals to UPDATED_OPEN_NUMBER_OF_TRADES
        defaultAccountDataTimeSeriesShouldBeFound("openNumberOfTrades.notEquals=" + UPDATED_OPEN_NUMBER_OF_TRADES);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByOpenNumberOfTradesIsInShouldWork() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where openNumberOfTrades in DEFAULT_OPEN_NUMBER_OF_TRADES or UPDATED_OPEN_NUMBER_OF_TRADES
        defaultAccountDataTimeSeriesShouldBeFound(
            "openNumberOfTrades.in=" + DEFAULT_OPEN_NUMBER_OF_TRADES + "," + UPDATED_OPEN_NUMBER_OF_TRADES
        );

        // Get all the accountDataTimeSeriesList where openNumberOfTrades equals to UPDATED_OPEN_NUMBER_OF_TRADES
        defaultAccountDataTimeSeriesShouldNotBeFound("openNumberOfTrades.in=" + UPDATED_OPEN_NUMBER_OF_TRADES);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByOpenNumberOfTradesIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where openNumberOfTrades is not null
        defaultAccountDataTimeSeriesShouldBeFound("openNumberOfTrades.specified=true");

        // Get all the accountDataTimeSeriesList where openNumberOfTrades is null
        defaultAccountDataTimeSeriesShouldNotBeFound("openNumberOfTrades.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByOpenNumberOfTradesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where openNumberOfTrades is greater than or equal to DEFAULT_OPEN_NUMBER_OF_TRADES
        defaultAccountDataTimeSeriesShouldBeFound("openNumberOfTrades.greaterThanOrEqual=" + DEFAULT_OPEN_NUMBER_OF_TRADES);

        // Get all the accountDataTimeSeriesList where openNumberOfTrades is greater than or equal to UPDATED_OPEN_NUMBER_OF_TRADES
        defaultAccountDataTimeSeriesShouldNotBeFound("openNumberOfTrades.greaterThanOrEqual=" + UPDATED_OPEN_NUMBER_OF_TRADES);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByOpenNumberOfTradesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where openNumberOfTrades is less than or equal to DEFAULT_OPEN_NUMBER_OF_TRADES
        defaultAccountDataTimeSeriesShouldBeFound("openNumberOfTrades.lessThanOrEqual=" + DEFAULT_OPEN_NUMBER_OF_TRADES);

        // Get all the accountDataTimeSeriesList where openNumberOfTrades is less than or equal to SMALLER_OPEN_NUMBER_OF_TRADES
        defaultAccountDataTimeSeriesShouldNotBeFound("openNumberOfTrades.lessThanOrEqual=" + SMALLER_OPEN_NUMBER_OF_TRADES);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByOpenNumberOfTradesIsLessThanSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where openNumberOfTrades is less than DEFAULT_OPEN_NUMBER_OF_TRADES
        defaultAccountDataTimeSeriesShouldNotBeFound("openNumberOfTrades.lessThan=" + DEFAULT_OPEN_NUMBER_OF_TRADES);

        // Get all the accountDataTimeSeriesList where openNumberOfTrades is less than UPDATED_OPEN_NUMBER_OF_TRADES
        defaultAccountDataTimeSeriesShouldBeFound("openNumberOfTrades.lessThan=" + UPDATED_OPEN_NUMBER_OF_TRADES);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByOpenNumberOfTradesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        // Get all the accountDataTimeSeriesList where openNumberOfTrades is greater than DEFAULT_OPEN_NUMBER_OF_TRADES
        defaultAccountDataTimeSeriesShouldNotBeFound("openNumberOfTrades.greaterThan=" + DEFAULT_OPEN_NUMBER_OF_TRADES);

        // Get all the accountDataTimeSeriesList where openNumberOfTrades is greater than SMALLER_OPEN_NUMBER_OF_TRADES
        defaultAccountDataTimeSeriesShouldBeFound("openNumberOfTrades.greaterThan=" + SMALLER_OPEN_NUMBER_OF_TRADES);
    }

    @Test
    @Transactional
    void getAllAccountDataTimeSeriesByMt4AccountIsEqualToSomething() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);
        Mt4Account mt4Account = Mt4AccountResourceIT.createEntity(em);
        em.persist(mt4Account);
        em.flush();
        accountDataTimeSeries.setMt4Account(mt4Account);
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);
        Long mt4AccountId = mt4Account.getId();

        // Get all the accountDataTimeSeriesList where mt4Account equals to mt4AccountId
        defaultAccountDataTimeSeriesShouldBeFound("mt4AccountId.equals=" + mt4AccountId);

        // Get all the accountDataTimeSeriesList where mt4Account equals to (mt4AccountId + 1)
        defaultAccountDataTimeSeriesShouldNotBeFound("mt4AccountId.equals=" + (mt4AccountId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAccountDataTimeSeriesShouldBeFound(String filter) throws Exception {
        restAccountDataTimeSeriesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountDataTimeSeries.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateStamp").value(hasItem(DEFAULT_DATE_STAMP.toString())))
            .andExpect(jsonPath("$.[*].accountBalance").value(hasItem(DEFAULT_ACCOUNT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].accountEquity").value(hasItem(DEFAULT_ACCOUNT_EQUITY.doubleValue())))
            .andExpect(jsonPath("$.[*].accountCredit").value(hasItem(DEFAULT_ACCOUNT_CREDIT.doubleValue())))
            .andExpect(jsonPath("$.[*].accountFreeMargin").value(hasItem(DEFAULT_ACCOUNT_FREE_MARGIN.doubleValue())))
            .andExpect(jsonPath("$.[*].accountStopoutLevel").value(hasItem(DEFAULT_ACCOUNT_STOPOUT_LEVEL)))
            .andExpect(jsonPath("$.[*].openLots").value(hasItem(DEFAULT_OPEN_LOTS.doubleValue())))
            .andExpect(jsonPath("$.[*].openNumberOfTrades").value(hasItem(DEFAULT_OPEN_NUMBER_OF_TRADES)));

        // Check, that the count call also returns 1
        restAccountDataTimeSeriesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAccountDataTimeSeriesShouldNotBeFound(String filter) throws Exception {
        restAccountDataTimeSeriesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAccountDataTimeSeriesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAccountDataTimeSeries() throws Exception {
        // Get the accountDataTimeSeries
        restAccountDataTimeSeriesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAccountDataTimeSeries() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        int databaseSizeBeforeUpdate = accountDataTimeSeriesRepository.findAll().size();

        // Update the accountDataTimeSeries
        AccountDataTimeSeries updatedAccountDataTimeSeries = accountDataTimeSeriesRepository.findById(accountDataTimeSeries.getId()).get();
        // Disconnect from session so that the updates on updatedAccountDataTimeSeries are not directly saved in db
        em.detach(updatedAccountDataTimeSeries);
        updatedAccountDataTimeSeries
            .dateStamp(UPDATED_DATE_STAMP)
            .accountBalance(UPDATED_ACCOUNT_BALANCE)
            .accountEquity(UPDATED_ACCOUNT_EQUITY)
            .accountCredit(UPDATED_ACCOUNT_CREDIT)
            .accountFreeMargin(UPDATED_ACCOUNT_FREE_MARGIN)
            .accountStopoutLevel(UPDATED_ACCOUNT_STOPOUT_LEVEL)
            .openLots(UPDATED_OPEN_LOTS)
            .openNumberOfTrades(UPDATED_OPEN_NUMBER_OF_TRADES);

        restAccountDataTimeSeriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccountDataTimeSeries.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAccountDataTimeSeries))
            )
            .andExpect(status().isOk());

        // Validate the AccountDataTimeSeries in the database
        List<AccountDataTimeSeries> accountDataTimeSeriesList = accountDataTimeSeriesRepository.findAll();
        assertThat(accountDataTimeSeriesList).hasSize(databaseSizeBeforeUpdate);
        AccountDataTimeSeries testAccountDataTimeSeries = accountDataTimeSeriesList.get(accountDataTimeSeriesList.size() - 1);
        assertThat(testAccountDataTimeSeries.getDateStamp()).isEqualTo(UPDATED_DATE_STAMP);
        assertThat(testAccountDataTimeSeries.getAccountBalance()).isEqualTo(UPDATED_ACCOUNT_BALANCE);
        assertThat(testAccountDataTimeSeries.getAccountEquity()).isEqualTo(UPDATED_ACCOUNT_EQUITY);
        assertThat(testAccountDataTimeSeries.getAccountCredit()).isEqualTo(UPDATED_ACCOUNT_CREDIT);
        assertThat(testAccountDataTimeSeries.getAccountFreeMargin()).isEqualTo(UPDATED_ACCOUNT_FREE_MARGIN);
        assertThat(testAccountDataTimeSeries.getAccountStopoutLevel()).isEqualTo(UPDATED_ACCOUNT_STOPOUT_LEVEL);
        assertThat(testAccountDataTimeSeries.getOpenLots()).isEqualTo(UPDATED_OPEN_LOTS);
        assertThat(testAccountDataTimeSeries.getOpenNumberOfTrades()).isEqualTo(UPDATED_OPEN_NUMBER_OF_TRADES);
    }

    @Test
    @Transactional
    void putNonExistingAccountDataTimeSeries() throws Exception {
        int databaseSizeBeforeUpdate = accountDataTimeSeriesRepository.findAll().size();
        accountDataTimeSeries.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountDataTimeSeriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountDataTimeSeries.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountDataTimeSeries))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountDataTimeSeries in the database
        List<AccountDataTimeSeries> accountDataTimeSeriesList = accountDataTimeSeriesRepository.findAll();
        assertThat(accountDataTimeSeriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountDataTimeSeries() throws Exception {
        int databaseSizeBeforeUpdate = accountDataTimeSeriesRepository.findAll().size();
        accountDataTimeSeries.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountDataTimeSeriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountDataTimeSeries))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountDataTimeSeries in the database
        List<AccountDataTimeSeries> accountDataTimeSeriesList = accountDataTimeSeriesRepository.findAll();
        assertThat(accountDataTimeSeriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountDataTimeSeries() throws Exception {
        int databaseSizeBeforeUpdate = accountDataTimeSeriesRepository.findAll().size();
        accountDataTimeSeries.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountDataTimeSeriesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountDataTimeSeries))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountDataTimeSeries in the database
        List<AccountDataTimeSeries> accountDataTimeSeriesList = accountDataTimeSeriesRepository.findAll();
        assertThat(accountDataTimeSeriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccountDataTimeSeriesWithPatch() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        int databaseSizeBeforeUpdate = accountDataTimeSeriesRepository.findAll().size();

        // Update the accountDataTimeSeries using partial update
        AccountDataTimeSeries partialUpdatedAccountDataTimeSeries = new AccountDataTimeSeries();
        partialUpdatedAccountDataTimeSeries.setId(accountDataTimeSeries.getId());

        partialUpdatedAccountDataTimeSeries
            .dateStamp(UPDATED_DATE_STAMP)
            .accountEquity(UPDATED_ACCOUNT_EQUITY)
            .openNumberOfTrades(UPDATED_OPEN_NUMBER_OF_TRADES);

        restAccountDataTimeSeriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountDataTimeSeries.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountDataTimeSeries))
            )
            .andExpect(status().isOk());

        // Validate the AccountDataTimeSeries in the database
        List<AccountDataTimeSeries> accountDataTimeSeriesList = accountDataTimeSeriesRepository.findAll();
        assertThat(accountDataTimeSeriesList).hasSize(databaseSizeBeforeUpdate);
        AccountDataTimeSeries testAccountDataTimeSeries = accountDataTimeSeriesList.get(accountDataTimeSeriesList.size() - 1);
        assertThat(testAccountDataTimeSeries.getDateStamp()).isEqualTo(UPDATED_DATE_STAMP);
        assertThat(testAccountDataTimeSeries.getAccountBalance()).isEqualTo(DEFAULT_ACCOUNT_BALANCE);
        assertThat(testAccountDataTimeSeries.getAccountEquity()).isEqualTo(UPDATED_ACCOUNT_EQUITY);
        assertThat(testAccountDataTimeSeries.getAccountCredit()).isEqualTo(DEFAULT_ACCOUNT_CREDIT);
        assertThat(testAccountDataTimeSeries.getAccountFreeMargin()).isEqualTo(DEFAULT_ACCOUNT_FREE_MARGIN);
        assertThat(testAccountDataTimeSeries.getAccountStopoutLevel()).isEqualTo(DEFAULT_ACCOUNT_STOPOUT_LEVEL);
        assertThat(testAccountDataTimeSeries.getOpenLots()).isEqualTo(DEFAULT_OPEN_LOTS);
        assertThat(testAccountDataTimeSeries.getOpenNumberOfTrades()).isEqualTo(UPDATED_OPEN_NUMBER_OF_TRADES);
    }

    @Test
    @Transactional
    void fullUpdateAccountDataTimeSeriesWithPatch() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        int databaseSizeBeforeUpdate = accountDataTimeSeriesRepository.findAll().size();

        // Update the accountDataTimeSeries using partial update
        AccountDataTimeSeries partialUpdatedAccountDataTimeSeries = new AccountDataTimeSeries();
        partialUpdatedAccountDataTimeSeries.setId(accountDataTimeSeries.getId());

        partialUpdatedAccountDataTimeSeries
            .dateStamp(UPDATED_DATE_STAMP)
            .accountBalance(UPDATED_ACCOUNT_BALANCE)
            .accountEquity(UPDATED_ACCOUNT_EQUITY)
            .accountCredit(UPDATED_ACCOUNT_CREDIT)
            .accountFreeMargin(UPDATED_ACCOUNT_FREE_MARGIN)
            .accountStopoutLevel(UPDATED_ACCOUNT_STOPOUT_LEVEL)
            .openLots(UPDATED_OPEN_LOTS)
            .openNumberOfTrades(UPDATED_OPEN_NUMBER_OF_TRADES);

        restAccountDataTimeSeriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountDataTimeSeries.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountDataTimeSeries))
            )
            .andExpect(status().isOk());

        // Validate the AccountDataTimeSeries in the database
        List<AccountDataTimeSeries> accountDataTimeSeriesList = accountDataTimeSeriesRepository.findAll();
        assertThat(accountDataTimeSeriesList).hasSize(databaseSizeBeforeUpdate);
        AccountDataTimeSeries testAccountDataTimeSeries = accountDataTimeSeriesList.get(accountDataTimeSeriesList.size() - 1);
        assertThat(testAccountDataTimeSeries.getDateStamp()).isEqualTo(UPDATED_DATE_STAMP);
        assertThat(testAccountDataTimeSeries.getAccountBalance()).isEqualTo(UPDATED_ACCOUNT_BALANCE);
        assertThat(testAccountDataTimeSeries.getAccountEquity()).isEqualTo(UPDATED_ACCOUNT_EQUITY);
        assertThat(testAccountDataTimeSeries.getAccountCredit()).isEqualTo(UPDATED_ACCOUNT_CREDIT);
        assertThat(testAccountDataTimeSeries.getAccountFreeMargin()).isEqualTo(UPDATED_ACCOUNT_FREE_MARGIN);
        assertThat(testAccountDataTimeSeries.getAccountStopoutLevel()).isEqualTo(UPDATED_ACCOUNT_STOPOUT_LEVEL);
        assertThat(testAccountDataTimeSeries.getOpenLots()).isEqualTo(UPDATED_OPEN_LOTS);
        assertThat(testAccountDataTimeSeries.getOpenNumberOfTrades()).isEqualTo(UPDATED_OPEN_NUMBER_OF_TRADES);
    }

    @Test
    @Transactional
    void patchNonExistingAccountDataTimeSeries() throws Exception {
        int databaseSizeBeforeUpdate = accountDataTimeSeriesRepository.findAll().size();
        accountDataTimeSeries.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountDataTimeSeriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountDataTimeSeries.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountDataTimeSeries))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountDataTimeSeries in the database
        List<AccountDataTimeSeries> accountDataTimeSeriesList = accountDataTimeSeriesRepository.findAll();
        assertThat(accountDataTimeSeriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountDataTimeSeries() throws Exception {
        int databaseSizeBeforeUpdate = accountDataTimeSeriesRepository.findAll().size();
        accountDataTimeSeries.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountDataTimeSeriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountDataTimeSeries))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountDataTimeSeries in the database
        List<AccountDataTimeSeries> accountDataTimeSeriesList = accountDataTimeSeriesRepository.findAll();
        assertThat(accountDataTimeSeriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountDataTimeSeries() throws Exception {
        int databaseSizeBeforeUpdate = accountDataTimeSeriesRepository.findAll().size();
        accountDataTimeSeries.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountDataTimeSeriesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountDataTimeSeries))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountDataTimeSeries in the database
        List<AccountDataTimeSeries> accountDataTimeSeriesList = accountDataTimeSeriesRepository.findAll();
        assertThat(accountDataTimeSeriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccountDataTimeSeries() throws Exception {
        // Initialize the database
        accountDataTimeSeriesRepository.saveAndFlush(accountDataTimeSeries);

        int databaseSizeBeforeDelete = accountDataTimeSeriesRepository.findAll().size();

        // Delete the accountDataTimeSeries
        restAccountDataTimeSeriesMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountDataTimeSeries.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccountDataTimeSeries> accountDataTimeSeriesList = accountDataTimeSeriesRepository.findAll();
        assertThat(accountDataTimeSeriesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
