package com.gracefl.propfirm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gracefl.propfirm.IntegrationTest;
import com.gracefl.propfirm.domain.TradeChallenge;
import com.gracefl.propfirm.repository.TradeChallengeRepository;
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
 * Integration tests for the {@link TradeChallengeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TradeChallengeResourceIT {

    private static final String DEFAULT_TRADE_CHALLENGE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TRADE_CHALLENGE_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_RUNNING_MAX_TOTAL_DRAWDOWN = 1D;
    private static final Double UPDATED_RUNNING_MAX_TOTAL_DRAWDOWN = 2D;

    private static final Double DEFAULT_RUNNING_MAX_DAILY_DRAWDOWN = 1D;
    private static final Double UPDATED_RUNNING_MAX_DAILY_DRAWDOWN = 2D;

    private static final Boolean DEFAULT_RULES_VIOLATED = false;
    private static final Boolean UPDATED_RULES_VIOLATED = true;

    private static final String DEFAULT_RULE_VIOLATED = "AAAAAAAAAA";
    private static final String UPDATED_RULE_VIOLATED = "BBBBBBBBBB";

    private static final Instant DEFAULT_RULE_VIOLATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RULE_VIOLATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/trade-challenges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TradeChallengeRepository tradeChallengeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTradeChallengeMockMvc;

    private TradeChallenge tradeChallenge;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TradeChallenge createEntity(EntityManager em) {
        TradeChallenge tradeChallenge = new TradeChallenge()
            .tradeChallengeName(DEFAULT_TRADE_CHALLENGE_NAME)
            .startDate(DEFAULT_START_DATE)
            .runningMaxTotalDrawdown(DEFAULT_RUNNING_MAX_TOTAL_DRAWDOWN)
            .runningMaxDailyDrawdown(DEFAULT_RUNNING_MAX_DAILY_DRAWDOWN)
            .rulesViolated(DEFAULT_RULES_VIOLATED)
            .ruleViolated(DEFAULT_RULE_VIOLATED)
            .ruleViolatedDate(DEFAULT_RULE_VIOLATED_DATE);
        return tradeChallenge;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TradeChallenge createUpdatedEntity(EntityManager em) {
        TradeChallenge tradeChallenge = new TradeChallenge()
            .tradeChallengeName(UPDATED_TRADE_CHALLENGE_NAME)
            .startDate(UPDATED_START_DATE)
            .runningMaxTotalDrawdown(UPDATED_RUNNING_MAX_TOTAL_DRAWDOWN)
            .runningMaxDailyDrawdown(UPDATED_RUNNING_MAX_DAILY_DRAWDOWN)
            .rulesViolated(UPDATED_RULES_VIOLATED)
            .ruleViolated(UPDATED_RULE_VIOLATED)
            .ruleViolatedDate(UPDATED_RULE_VIOLATED_DATE);
        return tradeChallenge;
    }

    @BeforeEach
    public void initTest() {
        tradeChallenge = createEntity(em);
    }

    @Test
    @Transactional
    void createTradeChallenge() throws Exception {
        int databaseSizeBeforeCreate = tradeChallengeRepository.findAll().size();
        // Create the TradeChallenge
        restTradeChallengeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeChallenge))
            )
            .andExpect(status().isCreated());

        // Validate the TradeChallenge in the database
        List<TradeChallenge> tradeChallengeList = tradeChallengeRepository.findAll();
        assertThat(tradeChallengeList).hasSize(databaseSizeBeforeCreate + 1);
        TradeChallenge testTradeChallenge = tradeChallengeList.get(tradeChallengeList.size() - 1);
        assertThat(testTradeChallenge.getTradeChallengeName()).isEqualTo(DEFAULT_TRADE_CHALLENGE_NAME);
        assertThat(testTradeChallenge.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTradeChallenge.getRunningMaxTotalDrawdown()).isEqualTo(DEFAULT_RUNNING_MAX_TOTAL_DRAWDOWN);
        assertThat(testTradeChallenge.getRunningMaxDailyDrawdown()).isEqualTo(DEFAULT_RUNNING_MAX_DAILY_DRAWDOWN);
        assertThat(testTradeChallenge.getRulesViolated()).isEqualTo(DEFAULT_RULES_VIOLATED);
        assertThat(testTradeChallenge.getRuleViolated()).isEqualTo(DEFAULT_RULE_VIOLATED);
        assertThat(testTradeChallenge.getRuleViolatedDate()).isEqualTo(DEFAULT_RULE_VIOLATED_DATE);
    }

    @Test
    @Transactional
    void createTradeChallengeWithExistingId() throws Exception {
        // Create the TradeChallenge with an existing ID
        tradeChallenge.setId(1L);

        int databaseSizeBeforeCreate = tradeChallengeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTradeChallengeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeChallenge))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeChallenge in the database
        List<TradeChallenge> tradeChallengeList = tradeChallengeRepository.findAll();
        assertThat(tradeChallengeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTradeChallenges() throws Exception {
        // Initialize the database
        tradeChallengeRepository.saveAndFlush(tradeChallenge);

        // Get all the tradeChallengeList
        restTradeChallengeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tradeChallenge.getId().intValue())))
            .andExpect(jsonPath("$.[*].tradeChallengeName").value(hasItem(DEFAULT_TRADE_CHALLENGE_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].runningMaxTotalDrawdown").value(hasItem(DEFAULT_RUNNING_MAX_TOTAL_DRAWDOWN.doubleValue())))
            .andExpect(jsonPath("$.[*].runningMaxDailyDrawdown").value(hasItem(DEFAULT_RUNNING_MAX_DAILY_DRAWDOWN.doubleValue())))
            .andExpect(jsonPath("$.[*].rulesViolated").value(hasItem(DEFAULT_RULES_VIOLATED.booleanValue())))
            .andExpect(jsonPath("$.[*].ruleViolated").value(hasItem(DEFAULT_RULE_VIOLATED)))
            .andExpect(jsonPath("$.[*].ruleViolatedDate").value(hasItem(DEFAULT_RULE_VIOLATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getTradeChallenge() throws Exception {
        // Initialize the database
        tradeChallengeRepository.saveAndFlush(tradeChallenge);

        // Get the tradeChallenge
        restTradeChallengeMockMvc
            .perform(get(ENTITY_API_URL_ID, tradeChallenge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tradeChallenge.getId().intValue()))
            .andExpect(jsonPath("$.tradeChallengeName").value(DEFAULT_TRADE_CHALLENGE_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.runningMaxTotalDrawdown").value(DEFAULT_RUNNING_MAX_TOTAL_DRAWDOWN.doubleValue()))
            .andExpect(jsonPath("$.runningMaxDailyDrawdown").value(DEFAULT_RUNNING_MAX_DAILY_DRAWDOWN.doubleValue()))
            .andExpect(jsonPath("$.rulesViolated").value(DEFAULT_RULES_VIOLATED.booleanValue()))
            .andExpect(jsonPath("$.ruleViolated").value(DEFAULT_RULE_VIOLATED))
            .andExpect(jsonPath("$.ruleViolatedDate").value(DEFAULT_RULE_VIOLATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTradeChallenge() throws Exception {
        // Get the tradeChallenge
        restTradeChallengeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTradeChallenge() throws Exception {
        // Initialize the database
        tradeChallengeRepository.saveAndFlush(tradeChallenge);

        int databaseSizeBeforeUpdate = tradeChallengeRepository.findAll().size();

        // Update the tradeChallenge
        TradeChallenge updatedTradeChallenge = tradeChallengeRepository.findById(tradeChallenge.getId()).get();
        // Disconnect from session so that the updates on updatedTradeChallenge are not directly saved in db
        em.detach(updatedTradeChallenge);
        updatedTradeChallenge
            .tradeChallengeName(UPDATED_TRADE_CHALLENGE_NAME)
            .startDate(UPDATED_START_DATE)
            .runningMaxTotalDrawdown(UPDATED_RUNNING_MAX_TOTAL_DRAWDOWN)
            .runningMaxDailyDrawdown(UPDATED_RUNNING_MAX_DAILY_DRAWDOWN)
            .rulesViolated(UPDATED_RULES_VIOLATED)
            .ruleViolated(UPDATED_RULE_VIOLATED)
            .ruleViolatedDate(UPDATED_RULE_VIOLATED_DATE);

        restTradeChallengeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTradeChallenge.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTradeChallenge))
            )
            .andExpect(status().isOk());

        // Validate the TradeChallenge in the database
        List<TradeChallenge> tradeChallengeList = tradeChallengeRepository.findAll();
        assertThat(tradeChallengeList).hasSize(databaseSizeBeforeUpdate);
        TradeChallenge testTradeChallenge = tradeChallengeList.get(tradeChallengeList.size() - 1);
        assertThat(testTradeChallenge.getTradeChallengeName()).isEqualTo(UPDATED_TRADE_CHALLENGE_NAME);
        assertThat(testTradeChallenge.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTradeChallenge.getRunningMaxTotalDrawdown()).isEqualTo(UPDATED_RUNNING_MAX_TOTAL_DRAWDOWN);
        assertThat(testTradeChallenge.getRunningMaxDailyDrawdown()).isEqualTo(UPDATED_RUNNING_MAX_DAILY_DRAWDOWN);
        assertThat(testTradeChallenge.getRulesViolated()).isEqualTo(UPDATED_RULES_VIOLATED);
        assertThat(testTradeChallenge.getRuleViolated()).isEqualTo(UPDATED_RULE_VIOLATED);
        assertThat(testTradeChallenge.getRuleViolatedDate()).isEqualTo(UPDATED_RULE_VIOLATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTradeChallenge() throws Exception {
        int databaseSizeBeforeUpdate = tradeChallengeRepository.findAll().size();
        tradeChallenge.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTradeChallengeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tradeChallenge.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tradeChallenge))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeChallenge in the database
        List<TradeChallenge> tradeChallengeList = tradeChallengeRepository.findAll();
        assertThat(tradeChallengeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTradeChallenge() throws Exception {
        int databaseSizeBeforeUpdate = tradeChallengeRepository.findAll().size();
        tradeChallenge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTradeChallengeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tradeChallenge))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeChallenge in the database
        List<TradeChallenge> tradeChallengeList = tradeChallengeRepository.findAll();
        assertThat(tradeChallengeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTradeChallenge() throws Exception {
        int databaseSizeBeforeUpdate = tradeChallengeRepository.findAll().size();
        tradeChallenge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTradeChallengeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeChallenge)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TradeChallenge in the database
        List<TradeChallenge> tradeChallengeList = tradeChallengeRepository.findAll();
        assertThat(tradeChallengeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTradeChallengeWithPatch() throws Exception {
        // Initialize the database
        tradeChallengeRepository.saveAndFlush(tradeChallenge);

        int databaseSizeBeforeUpdate = tradeChallengeRepository.findAll().size();

        // Update the tradeChallenge using partial update
        TradeChallenge partialUpdatedTradeChallenge = new TradeChallenge();
        partialUpdatedTradeChallenge.setId(tradeChallenge.getId());

        partialUpdatedTradeChallenge
            .tradeChallengeName(UPDATED_TRADE_CHALLENGE_NAME)
            .runningMaxDailyDrawdown(UPDATED_RUNNING_MAX_DAILY_DRAWDOWN)
            .rulesViolated(UPDATED_RULES_VIOLATED)
            .ruleViolated(UPDATED_RULE_VIOLATED);

        restTradeChallengeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTradeChallenge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTradeChallenge))
            )
            .andExpect(status().isOk());

        // Validate the TradeChallenge in the database
        List<TradeChallenge> tradeChallengeList = tradeChallengeRepository.findAll();
        assertThat(tradeChallengeList).hasSize(databaseSizeBeforeUpdate);
        TradeChallenge testTradeChallenge = tradeChallengeList.get(tradeChallengeList.size() - 1);
        assertThat(testTradeChallenge.getTradeChallengeName()).isEqualTo(UPDATED_TRADE_CHALLENGE_NAME);
        assertThat(testTradeChallenge.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTradeChallenge.getRunningMaxTotalDrawdown()).isEqualTo(DEFAULT_RUNNING_MAX_TOTAL_DRAWDOWN);
        assertThat(testTradeChallenge.getRunningMaxDailyDrawdown()).isEqualTo(UPDATED_RUNNING_MAX_DAILY_DRAWDOWN);
        assertThat(testTradeChallenge.getRulesViolated()).isEqualTo(UPDATED_RULES_VIOLATED);
        assertThat(testTradeChallenge.getRuleViolated()).isEqualTo(UPDATED_RULE_VIOLATED);
        assertThat(testTradeChallenge.getRuleViolatedDate()).isEqualTo(DEFAULT_RULE_VIOLATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTradeChallengeWithPatch() throws Exception {
        // Initialize the database
        tradeChallengeRepository.saveAndFlush(tradeChallenge);

        int databaseSizeBeforeUpdate = tradeChallengeRepository.findAll().size();

        // Update the tradeChallenge using partial update
        TradeChallenge partialUpdatedTradeChallenge = new TradeChallenge();
        partialUpdatedTradeChallenge.setId(tradeChallenge.getId());

        partialUpdatedTradeChallenge
            .tradeChallengeName(UPDATED_TRADE_CHALLENGE_NAME)
            .startDate(UPDATED_START_DATE)
            .runningMaxTotalDrawdown(UPDATED_RUNNING_MAX_TOTAL_DRAWDOWN)
            .runningMaxDailyDrawdown(UPDATED_RUNNING_MAX_DAILY_DRAWDOWN)
            .rulesViolated(UPDATED_RULES_VIOLATED)
            .ruleViolated(UPDATED_RULE_VIOLATED)
            .ruleViolatedDate(UPDATED_RULE_VIOLATED_DATE);

        restTradeChallengeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTradeChallenge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTradeChallenge))
            )
            .andExpect(status().isOk());

        // Validate the TradeChallenge in the database
        List<TradeChallenge> tradeChallengeList = tradeChallengeRepository.findAll();
        assertThat(tradeChallengeList).hasSize(databaseSizeBeforeUpdate);
        TradeChallenge testTradeChallenge = tradeChallengeList.get(tradeChallengeList.size() - 1);
        assertThat(testTradeChallenge.getTradeChallengeName()).isEqualTo(UPDATED_TRADE_CHALLENGE_NAME);
        assertThat(testTradeChallenge.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTradeChallenge.getRunningMaxTotalDrawdown()).isEqualTo(UPDATED_RUNNING_MAX_TOTAL_DRAWDOWN);
        assertThat(testTradeChallenge.getRunningMaxDailyDrawdown()).isEqualTo(UPDATED_RUNNING_MAX_DAILY_DRAWDOWN);
        assertThat(testTradeChallenge.getRulesViolated()).isEqualTo(UPDATED_RULES_VIOLATED);
        assertThat(testTradeChallenge.getRuleViolated()).isEqualTo(UPDATED_RULE_VIOLATED);
        assertThat(testTradeChallenge.getRuleViolatedDate()).isEqualTo(UPDATED_RULE_VIOLATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTradeChallenge() throws Exception {
        int databaseSizeBeforeUpdate = tradeChallengeRepository.findAll().size();
        tradeChallenge.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTradeChallengeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tradeChallenge.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tradeChallenge))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeChallenge in the database
        List<TradeChallenge> tradeChallengeList = tradeChallengeRepository.findAll();
        assertThat(tradeChallengeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTradeChallenge() throws Exception {
        int databaseSizeBeforeUpdate = tradeChallengeRepository.findAll().size();
        tradeChallenge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTradeChallengeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tradeChallenge))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeChallenge in the database
        List<TradeChallenge> tradeChallengeList = tradeChallengeRepository.findAll();
        assertThat(tradeChallengeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTradeChallenge() throws Exception {
        int databaseSizeBeforeUpdate = tradeChallengeRepository.findAll().size();
        tradeChallenge.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTradeChallengeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tradeChallenge))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TradeChallenge in the database
        List<TradeChallenge> tradeChallengeList = tradeChallengeRepository.findAll();
        assertThat(tradeChallengeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTradeChallenge() throws Exception {
        // Initialize the database
        tradeChallengeRepository.saveAndFlush(tradeChallenge);

        int databaseSizeBeforeDelete = tradeChallengeRepository.findAll().size();

        // Delete the tradeChallenge
        restTradeChallengeMockMvc
            .perform(delete(ENTITY_API_URL_ID, tradeChallenge.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TradeChallenge> tradeChallengeList = tradeChallengeRepository.findAll();
        assertThat(tradeChallengeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
