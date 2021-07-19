package com.gracefl.propfirm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gracefl.propfirm.IntegrationTest;
import com.gracefl.propfirm.domain.AccountDataTimeSeries;
import com.gracefl.propfirm.repository.AccountDataTimeSeriesRepository;
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

    private static final Double DEFAULT_ACCOUNT_EQUITY = 1D;
    private static final Double UPDATED_ACCOUNT_EQUITY = 2D;

    private static final Double DEFAULT_ACCOUNT_CREDIT = 1D;
    private static final Double UPDATED_ACCOUNT_CREDIT = 2D;

    private static final Double DEFAULT_ACCOUNT_FREE_MARGIN = 1D;
    private static final Double UPDATED_ACCOUNT_FREE_MARGIN = 2D;

    private static final Integer DEFAULT_ACCOUNT_STOPOUT_LEVEL = 1;
    private static final Integer UPDATED_ACCOUNT_STOPOUT_LEVEL = 2;

    private static final Double DEFAULT_OPEN_LOTS = 1D;
    private static final Double UPDATED_OPEN_LOTS = 2D;

    private static final Integer DEFAULT_OPEN_NUMBER_OF_TRADES = 1;
    private static final Integer UPDATED_OPEN_NUMBER_OF_TRADES = 2;

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
