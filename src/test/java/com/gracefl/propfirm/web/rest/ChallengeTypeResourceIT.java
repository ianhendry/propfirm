package com.gracefl.propfirm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gracefl.propfirm.IntegrationTest;
import com.gracefl.propfirm.domain.ChallengeType;
import com.gracefl.propfirm.repository.ChallengeTypeRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ChallengeTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChallengeTypeResourceIT {

    private static final String DEFAULT_CHALLENGE_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CHALLENGE_TYPE_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PRICE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PRICE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PRICE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PRICE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_REFUND_AFTER_COMPLETE = false;
    private static final Boolean UPDATED_REFUND_AFTER_COMPLETE = true;

    private static final Integer DEFAULT_ACCOUNT_SIZE = 1;
    private static final Integer UPDATED_ACCOUNT_SIZE = 2;

    private static final Integer DEFAULT_PROFIT_TARGET_PHASE_ONE = 1;
    private static final Integer UPDATED_PROFIT_TARGET_PHASE_ONE = 2;

    private static final Integer DEFAULT_PROFIT_TARGET_PHASE_TWO = 1;
    private static final Integer UPDATED_PROFIT_TARGET_PHASE_TWO = 2;

    private static final Integer DEFAULT_DURATION_DAYS_PHASE_ONE = 1;
    private static final Integer UPDATED_DURATION_DAYS_PHASE_ONE = 2;

    private static final Integer DEFAULT_DURATION_DAYS_PHASE_TWO = 1;
    private static final Integer UPDATED_DURATION_DAYS_PHASE_TWO = 2;

    private static final Integer DEFAULT_MAX_DAILY_DRAWDOWN = 1;
    private static final Integer UPDATED_MAX_DAILY_DRAWDOWN = 2;

    private static final Integer DEFAULT_MAX_TOTAL_DRAW_DOWN = 1;
    private static final Integer UPDATED_MAX_TOTAL_DRAW_DOWN = 2;

    private static final Integer DEFAULT_PROFIT_SPLIT_RATIO = 1;
    private static final Integer UPDATED_PROFIT_SPLIT_RATIO = 2;

    private static final Boolean DEFAULT_FREE_RETRY = false;
    private static final Boolean UPDATED_FREE_RETRY = true;

    private static final String DEFAULT_USER_BIO = "AAAAAAAAAA";
    private static final String UPDATED_USER_BIO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IN_ACTIVE = false;
    private static final Boolean UPDATED_IN_ACTIVE = true;

    private static final Instant DEFAULT_IN_ACTIVE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_IN_ACTIVE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/challenge-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChallengeTypeRepository challengeTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChallengeTypeMockMvc;

    private ChallengeType challengeType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChallengeType createEntity(EntityManager em) {
        ChallengeType challengeType = new ChallengeType()
            .challengeTypeName(DEFAULT_CHALLENGE_TYPE_NAME)
            .price(DEFAULT_PRICE)
            .priceContentType(DEFAULT_PRICE_CONTENT_TYPE)
            .refundAfterComplete(DEFAULT_REFUND_AFTER_COMPLETE)
            .accountSize(DEFAULT_ACCOUNT_SIZE)
            .profitTargetPhaseOne(DEFAULT_PROFIT_TARGET_PHASE_ONE)
            .profitTargetPhaseTwo(DEFAULT_PROFIT_TARGET_PHASE_TWO)
            .durationDaysPhaseOne(DEFAULT_DURATION_DAYS_PHASE_ONE)
            .durationDaysPhaseTwo(DEFAULT_DURATION_DAYS_PHASE_TWO)
            .maxDailyDrawdown(DEFAULT_MAX_DAILY_DRAWDOWN)
            .maxTotalDrawDown(DEFAULT_MAX_TOTAL_DRAW_DOWN)
            .profitSplitRatio(DEFAULT_PROFIT_SPLIT_RATIO)
            .freeRetry(DEFAULT_FREE_RETRY)
            .userBio(DEFAULT_USER_BIO)
            .inActive(DEFAULT_IN_ACTIVE)
            .inActiveDate(DEFAULT_IN_ACTIVE_DATE);
        return challengeType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChallengeType createUpdatedEntity(EntityManager em) {
        ChallengeType challengeType = new ChallengeType()
            .challengeTypeName(UPDATED_CHALLENGE_TYPE_NAME)
            .price(UPDATED_PRICE)
            .priceContentType(UPDATED_PRICE_CONTENT_TYPE)
            .refundAfterComplete(UPDATED_REFUND_AFTER_COMPLETE)
            .accountSize(UPDATED_ACCOUNT_SIZE)
            .profitTargetPhaseOne(UPDATED_PROFIT_TARGET_PHASE_ONE)
            .profitTargetPhaseTwo(UPDATED_PROFIT_TARGET_PHASE_TWO)
            .durationDaysPhaseOne(UPDATED_DURATION_DAYS_PHASE_ONE)
            .durationDaysPhaseTwo(UPDATED_DURATION_DAYS_PHASE_TWO)
            .maxDailyDrawdown(UPDATED_MAX_DAILY_DRAWDOWN)
            .maxTotalDrawDown(UPDATED_MAX_TOTAL_DRAW_DOWN)
            .profitSplitRatio(UPDATED_PROFIT_SPLIT_RATIO)
            .freeRetry(UPDATED_FREE_RETRY)
            .userBio(UPDATED_USER_BIO)
            .inActive(UPDATED_IN_ACTIVE)
            .inActiveDate(UPDATED_IN_ACTIVE_DATE);
        return challengeType;
    }

    @BeforeEach
    public void initTest() {
        challengeType = createEntity(em);
    }

    @Test
    @Transactional
    void createChallengeType() throws Exception {
        int databaseSizeBeforeCreate = challengeTypeRepository.findAll().size();
        // Create the ChallengeType
        restChallengeTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(challengeType)))
            .andExpect(status().isCreated());

        // Validate the ChallengeType in the database
        List<ChallengeType> challengeTypeList = challengeTypeRepository.findAll();
        assertThat(challengeTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ChallengeType testChallengeType = challengeTypeList.get(challengeTypeList.size() - 1);
        assertThat(testChallengeType.getChallengeTypeName()).isEqualTo(DEFAULT_CHALLENGE_TYPE_NAME);
        assertThat(testChallengeType.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testChallengeType.getPriceContentType()).isEqualTo(DEFAULT_PRICE_CONTENT_TYPE);
        assertThat(testChallengeType.getRefundAfterComplete()).isEqualTo(DEFAULT_REFUND_AFTER_COMPLETE);
        assertThat(testChallengeType.getAccountSize()).isEqualTo(DEFAULT_ACCOUNT_SIZE);
        assertThat(testChallengeType.getProfitTargetPhaseOne()).isEqualTo(DEFAULT_PROFIT_TARGET_PHASE_ONE);
        assertThat(testChallengeType.getProfitTargetPhaseTwo()).isEqualTo(DEFAULT_PROFIT_TARGET_PHASE_TWO);
        assertThat(testChallengeType.getDurationDaysPhaseOne()).isEqualTo(DEFAULT_DURATION_DAYS_PHASE_ONE);
        assertThat(testChallengeType.getDurationDaysPhaseTwo()).isEqualTo(DEFAULT_DURATION_DAYS_PHASE_TWO);
        assertThat(testChallengeType.getMaxDailyDrawdown()).isEqualTo(DEFAULT_MAX_DAILY_DRAWDOWN);
        assertThat(testChallengeType.getMaxTotalDrawDown()).isEqualTo(DEFAULT_MAX_TOTAL_DRAW_DOWN);
        assertThat(testChallengeType.getProfitSplitRatio()).isEqualTo(DEFAULT_PROFIT_SPLIT_RATIO);
        assertThat(testChallengeType.getFreeRetry()).isEqualTo(DEFAULT_FREE_RETRY);
        assertThat(testChallengeType.getUserBio()).isEqualTo(DEFAULT_USER_BIO);
        assertThat(testChallengeType.getInActive()).isEqualTo(DEFAULT_IN_ACTIVE);
        assertThat(testChallengeType.getInActiveDate()).isEqualTo(DEFAULT_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void createChallengeTypeWithExistingId() throws Exception {
        // Create the ChallengeType with an existing ID
        challengeType.setId(1L);

        int databaseSizeBeforeCreate = challengeTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChallengeTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(challengeType)))
            .andExpect(status().isBadRequest());

        // Validate the ChallengeType in the database
        List<ChallengeType> challengeTypeList = challengeTypeRepository.findAll();
        assertThat(challengeTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllChallengeTypes() throws Exception {
        // Initialize the database
        challengeTypeRepository.saveAndFlush(challengeType);

        // Get all the challengeTypeList
        restChallengeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(challengeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].challengeTypeName").value(hasItem(DEFAULT_CHALLENGE_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].priceContentType").value(hasItem(DEFAULT_PRICE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(Base64Utils.encodeToString(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].refundAfterComplete").value(hasItem(DEFAULT_REFUND_AFTER_COMPLETE.booleanValue())))
            .andExpect(jsonPath("$.[*].accountSize").value(hasItem(DEFAULT_ACCOUNT_SIZE)))
            .andExpect(jsonPath("$.[*].profitTargetPhaseOne").value(hasItem(DEFAULT_PROFIT_TARGET_PHASE_ONE)))
            .andExpect(jsonPath("$.[*].profitTargetPhaseTwo").value(hasItem(DEFAULT_PROFIT_TARGET_PHASE_TWO)))
            .andExpect(jsonPath("$.[*].durationDaysPhaseOne").value(hasItem(DEFAULT_DURATION_DAYS_PHASE_ONE)))
            .andExpect(jsonPath("$.[*].durationDaysPhaseTwo").value(hasItem(DEFAULT_DURATION_DAYS_PHASE_TWO)))
            .andExpect(jsonPath("$.[*].maxDailyDrawdown").value(hasItem(DEFAULT_MAX_DAILY_DRAWDOWN)))
            .andExpect(jsonPath("$.[*].maxTotalDrawDown").value(hasItem(DEFAULT_MAX_TOTAL_DRAW_DOWN)))
            .andExpect(jsonPath("$.[*].profitSplitRatio").value(hasItem(DEFAULT_PROFIT_SPLIT_RATIO)))
            .andExpect(jsonPath("$.[*].freeRetry").value(hasItem(DEFAULT_FREE_RETRY.booleanValue())))
            .andExpect(jsonPath("$.[*].userBio").value(hasItem(DEFAULT_USER_BIO.toString())))
            .andExpect(jsonPath("$.[*].inActive").value(hasItem(DEFAULT_IN_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].inActiveDate").value(hasItem(DEFAULT_IN_ACTIVE_DATE.toString())));
    }

    @Test
    @Transactional
    void getChallengeType() throws Exception {
        // Initialize the database
        challengeTypeRepository.saveAndFlush(challengeType);

        // Get the challengeType
        restChallengeTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, challengeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(challengeType.getId().intValue()))
            .andExpect(jsonPath("$.challengeTypeName").value(DEFAULT_CHALLENGE_TYPE_NAME))
            .andExpect(jsonPath("$.priceContentType").value(DEFAULT_PRICE_CONTENT_TYPE))
            .andExpect(jsonPath("$.price").value(Base64Utils.encodeToString(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.refundAfterComplete").value(DEFAULT_REFUND_AFTER_COMPLETE.booleanValue()))
            .andExpect(jsonPath("$.accountSize").value(DEFAULT_ACCOUNT_SIZE))
            .andExpect(jsonPath("$.profitTargetPhaseOne").value(DEFAULT_PROFIT_TARGET_PHASE_ONE))
            .andExpect(jsonPath("$.profitTargetPhaseTwo").value(DEFAULT_PROFIT_TARGET_PHASE_TWO))
            .andExpect(jsonPath("$.durationDaysPhaseOne").value(DEFAULT_DURATION_DAYS_PHASE_ONE))
            .andExpect(jsonPath("$.durationDaysPhaseTwo").value(DEFAULT_DURATION_DAYS_PHASE_TWO))
            .andExpect(jsonPath("$.maxDailyDrawdown").value(DEFAULT_MAX_DAILY_DRAWDOWN))
            .andExpect(jsonPath("$.maxTotalDrawDown").value(DEFAULT_MAX_TOTAL_DRAW_DOWN))
            .andExpect(jsonPath("$.profitSplitRatio").value(DEFAULT_PROFIT_SPLIT_RATIO))
            .andExpect(jsonPath("$.freeRetry").value(DEFAULT_FREE_RETRY.booleanValue()))
            .andExpect(jsonPath("$.userBio").value(DEFAULT_USER_BIO.toString()))
            .andExpect(jsonPath("$.inActive").value(DEFAULT_IN_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.inActiveDate").value(DEFAULT_IN_ACTIVE_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingChallengeType() throws Exception {
        // Get the challengeType
        restChallengeTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewChallengeType() throws Exception {
        // Initialize the database
        challengeTypeRepository.saveAndFlush(challengeType);

        int databaseSizeBeforeUpdate = challengeTypeRepository.findAll().size();

        // Update the challengeType
        ChallengeType updatedChallengeType = challengeTypeRepository.findById(challengeType.getId()).get();
        // Disconnect from session so that the updates on updatedChallengeType are not directly saved in db
        em.detach(updatedChallengeType);
        updatedChallengeType
            .challengeTypeName(UPDATED_CHALLENGE_TYPE_NAME)
            .price(UPDATED_PRICE)
            .priceContentType(UPDATED_PRICE_CONTENT_TYPE)
            .refundAfterComplete(UPDATED_REFUND_AFTER_COMPLETE)
            .accountSize(UPDATED_ACCOUNT_SIZE)
            .profitTargetPhaseOne(UPDATED_PROFIT_TARGET_PHASE_ONE)
            .profitTargetPhaseTwo(UPDATED_PROFIT_TARGET_PHASE_TWO)
            .durationDaysPhaseOne(UPDATED_DURATION_DAYS_PHASE_ONE)
            .durationDaysPhaseTwo(UPDATED_DURATION_DAYS_PHASE_TWO)
            .maxDailyDrawdown(UPDATED_MAX_DAILY_DRAWDOWN)
            .maxTotalDrawDown(UPDATED_MAX_TOTAL_DRAW_DOWN)
            .profitSplitRatio(UPDATED_PROFIT_SPLIT_RATIO)
            .freeRetry(UPDATED_FREE_RETRY)
            .userBio(UPDATED_USER_BIO)
            .inActive(UPDATED_IN_ACTIVE)
            .inActiveDate(UPDATED_IN_ACTIVE_DATE);

        restChallengeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChallengeType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedChallengeType))
            )
            .andExpect(status().isOk());

        // Validate the ChallengeType in the database
        List<ChallengeType> challengeTypeList = challengeTypeRepository.findAll();
        assertThat(challengeTypeList).hasSize(databaseSizeBeforeUpdate);
        ChallengeType testChallengeType = challengeTypeList.get(challengeTypeList.size() - 1);
        assertThat(testChallengeType.getChallengeTypeName()).isEqualTo(UPDATED_CHALLENGE_TYPE_NAME);
        assertThat(testChallengeType.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testChallengeType.getPriceContentType()).isEqualTo(UPDATED_PRICE_CONTENT_TYPE);
        assertThat(testChallengeType.getRefundAfterComplete()).isEqualTo(UPDATED_REFUND_AFTER_COMPLETE);
        assertThat(testChallengeType.getAccountSize()).isEqualTo(UPDATED_ACCOUNT_SIZE);
        assertThat(testChallengeType.getProfitTargetPhaseOne()).isEqualTo(UPDATED_PROFIT_TARGET_PHASE_ONE);
        assertThat(testChallengeType.getProfitTargetPhaseTwo()).isEqualTo(UPDATED_PROFIT_TARGET_PHASE_TWO);
        assertThat(testChallengeType.getDurationDaysPhaseOne()).isEqualTo(UPDATED_DURATION_DAYS_PHASE_ONE);
        assertThat(testChallengeType.getDurationDaysPhaseTwo()).isEqualTo(UPDATED_DURATION_DAYS_PHASE_TWO);
        assertThat(testChallengeType.getMaxDailyDrawdown()).isEqualTo(UPDATED_MAX_DAILY_DRAWDOWN);
        assertThat(testChallengeType.getMaxTotalDrawDown()).isEqualTo(UPDATED_MAX_TOTAL_DRAW_DOWN);
        assertThat(testChallengeType.getProfitSplitRatio()).isEqualTo(UPDATED_PROFIT_SPLIT_RATIO);
        assertThat(testChallengeType.getFreeRetry()).isEqualTo(UPDATED_FREE_RETRY);
        assertThat(testChallengeType.getUserBio()).isEqualTo(UPDATED_USER_BIO);
        assertThat(testChallengeType.getInActive()).isEqualTo(UPDATED_IN_ACTIVE);
        assertThat(testChallengeType.getInActiveDate()).isEqualTo(UPDATED_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void putNonExistingChallengeType() throws Exception {
        int databaseSizeBeforeUpdate = challengeTypeRepository.findAll().size();
        challengeType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChallengeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, challengeType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(challengeType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChallengeType in the database
        List<ChallengeType> challengeTypeList = challengeTypeRepository.findAll();
        assertThat(challengeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChallengeType() throws Exception {
        int databaseSizeBeforeUpdate = challengeTypeRepository.findAll().size();
        challengeType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChallengeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(challengeType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChallengeType in the database
        List<ChallengeType> challengeTypeList = challengeTypeRepository.findAll();
        assertThat(challengeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChallengeType() throws Exception {
        int databaseSizeBeforeUpdate = challengeTypeRepository.findAll().size();
        challengeType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChallengeTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(challengeType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChallengeType in the database
        List<ChallengeType> challengeTypeList = challengeTypeRepository.findAll();
        assertThat(challengeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChallengeTypeWithPatch() throws Exception {
        // Initialize the database
        challengeTypeRepository.saveAndFlush(challengeType);

        int databaseSizeBeforeUpdate = challengeTypeRepository.findAll().size();

        // Update the challengeType using partial update
        ChallengeType partialUpdatedChallengeType = new ChallengeType();
        partialUpdatedChallengeType.setId(challengeType.getId());

        partialUpdatedChallengeType
            .price(UPDATED_PRICE)
            .priceContentType(UPDATED_PRICE_CONTENT_TYPE)
            .accountSize(UPDATED_ACCOUNT_SIZE)
            .profitTargetPhaseOne(UPDATED_PROFIT_TARGET_PHASE_ONE)
            .profitTargetPhaseTwo(UPDATED_PROFIT_TARGET_PHASE_TWO)
            .durationDaysPhaseOne(UPDATED_DURATION_DAYS_PHASE_ONE)
            .maxDailyDrawdown(UPDATED_MAX_DAILY_DRAWDOWN)
            .maxTotalDrawDown(UPDATED_MAX_TOTAL_DRAW_DOWN)
            .userBio(UPDATED_USER_BIO)
            .inActive(UPDATED_IN_ACTIVE)
            .inActiveDate(UPDATED_IN_ACTIVE_DATE);

        restChallengeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChallengeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChallengeType))
            )
            .andExpect(status().isOk());

        // Validate the ChallengeType in the database
        List<ChallengeType> challengeTypeList = challengeTypeRepository.findAll();
        assertThat(challengeTypeList).hasSize(databaseSizeBeforeUpdate);
        ChallengeType testChallengeType = challengeTypeList.get(challengeTypeList.size() - 1);
        assertThat(testChallengeType.getChallengeTypeName()).isEqualTo(DEFAULT_CHALLENGE_TYPE_NAME);
        assertThat(testChallengeType.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testChallengeType.getPriceContentType()).isEqualTo(UPDATED_PRICE_CONTENT_TYPE);
        assertThat(testChallengeType.getRefundAfterComplete()).isEqualTo(DEFAULT_REFUND_AFTER_COMPLETE);
        assertThat(testChallengeType.getAccountSize()).isEqualTo(UPDATED_ACCOUNT_SIZE);
        assertThat(testChallengeType.getProfitTargetPhaseOne()).isEqualTo(UPDATED_PROFIT_TARGET_PHASE_ONE);
        assertThat(testChallengeType.getProfitTargetPhaseTwo()).isEqualTo(UPDATED_PROFIT_TARGET_PHASE_TWO);
        assertThat(testChallengeType.getDurationDaysPhaseOne()).isEqualTo(UPDATED_DURATION_DAYS_PHASE_ONE);
        assertThat(testChallengeType.getDurationDaysPhaseTwo()).isEqualTo(DEFAULT_DURATION_DAYS_PHASE_TWO);
        assertThat(testChallengeType.getMaxDailyDrawdown()).isEqualTo(UPDATED_MAX_DAILY_DRAWDOWN);
        assertThat(testChallengeType.getMaxTotalDrawDown()).isEqualTo(UPDATED_MAX_TOTAL_DRAW_DOWN);
        assertThat(testChallengeType.getProfitSplitRatio()).isEqualTo(DEFAULT_PROFIT_SPLIT_RATIO);
        assertThat(testChallengeType.getFreeRetry()).isEqualTo(DEFAULT_FREE_RETRY);
        assertThat(testChallengeType.getUserBio()).isEqualTo(UPDATED_USER_BIO);
        assertThat(testChallengeType.getInActive()).isEqualTo(UPDATED_IN_ACTIVE);
        assertThat(testChallengeType.getInActiveDate()).isEqualTo(UPDATED_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void fullUpdateChallengeTypeWithPatch() throws Exception {
        // Initialize the database
        challengeTypeRepository.saveAndFlush(challengeType);

        int databaseSizeBeforeUpdate = challengeTypeRepository.findAll().size();

        // Update the challengeType using partial update
        ChallengeType partialUpdatedChallengeType = new ChallengeType();
        partialUpdatedChallengeType.setId(challengeType.getId());

        partialUpdatedChallengeType
            .challengeTypeName(UPDATED_CHALLENGE_TYPE_NAME)
            .price(UPDATED_PRICE)
            .priceContentType(UPDATED_PRICE_CONTENT_TYPE)
            .refundAfterComplete(UPDATED_REFUND_AFTER_COMPLETE)
            .accountSize(UPDATED_ACCOUNT_SIZE)
            .profitTargetPhaseOne(UPDATED_PROFIT_TARGET_PHASE_ONE)
            .profitTargetPhaseTwo(UPDATED_PROFIT_TARGET_PHASE_TWO)
            .durationDaysPhaseOne(UPDATED_DURATION_DAYS_PHASE_ONE)
            .durationDaysPhaseTwo(UPDATED_DURATION_DAYS_PHASE_TWO)
            .maxDailyDrawdown(UPDATED_MAX_DAILY_DRAWDOWN)
            .maxTotalDrawDown(UPDATED_MAX_TOTAL_DRAW_DOWN)
            .profitSplitRatio(UPDATED_PROFIT_SPLIT_RATIO)
            .freeRetry(UPDATED_FREE_RETRY)
            .userBio(UPDATED_USER_BIO)
            .inActive(UPDATED_IN_ACTIVE)
            .inActiveDate(UPDATED_IN_ACTIVE_DATE);

        restChallengeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChallengeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChallengeType))
            )
            .andExpect(status().isOk());

        // Validate the ChallengeType in the database
        List<ChallengeType> challengeTypeList = challengeTypeRepository.findAll();
        assertThat(challengeTypeList).hasSize(databaseSizeBeforeUpdate);
        ChallengeType testChallengeType = challengeTypeList.get(challengeTypeList.size() - 1);
        assertThat(testChallengeType.getChallengeTypeName()).isEqualTo(UPDATED_CHALLENGE_TYPE_NAME);
        assertThat(testChallengeType.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testChallengeType.getPriceContentType()).isEqualTo(UPDATED_PRICE_CONTENT_TYPE);
        assertThat(testChallengeType.getRefundAfterComplete()).isEqualTo(UPDATED_REFUND_AFTER_COMPLETE);
        assertThat(testChallengeType.getAccountSize()).isEqualTo(UPDATED_ACCOUNT_SIZE);
        assertThat(testChallengeType.getProfitTargetPhaseOne()).isEqualTo(UPDATED_PROFIT_TARGET_PHASE_ONE);
        assertThat(testChallengeType.getProfitTargetPhaseTwo()).isEqualTo(UPDATED_PROFIT_TARGET_PHASE_TWO);
        assertThat(testChallengeType.getDurationDaysPhaseOne()).isEqualTo(UPDATED_DURATION_DAYS_PHASE_ONE);
        assertThat(testChallengeType.getDurationDaysPhaseTwo()).isEqualTo(UPDATED_DURATION_DAYS_PHASE_TWO);
        assertThat(testChallengeType.getMaxDailyDrawdown()).isEqualTo(UPDATED_MAX_DAILY_DRAWDOWN);
        assertThat(testChallengeType.getMaxTotalDrawDown()).isEqualTo(UPDATED_MAX_TOTAL_DRAW_DOWN);
        assertThat(testChallengeType.getProfitSplitRatio()).isEqualTo(UPDATED_PROFIT_SPLIT_RATIO);
        assertThat(testChallengeType.getFreeRetry()).isEqualTo(UPDATED_FREE_RETRY);
        assertThat(testChallengeType.getUserBio()).isEqualTo(UPDATED_USER_BIO);
        assertThat(testChallengeType.getInActive()).isEqualTo(UPDATED_IN_ACTIVE);
        assertThat(testChallengeType.getInActiveDate()).isEqualTo(UPDATED_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingChallengeType() throws Exception {
        int databaseSizeBeforeUpdate = challengeTypeRepository.findAll().size();
        challengeType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChallengeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, challengeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(challengeType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChallengeType in the database
        List<ChallengeType> challengeTypeList = challengeTypeRepository.findAll();
        assertThat(challengeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChallengeType() throws Exception {
        int databaseSizeBeforeUpdate = challengeTypeRepository.findAll().size();
        challengeType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChallengeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(challengeType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChallengeType in the database
        List<ChallengeType> challengeTypeList = challengeTypeRepository.findAll();
        assertThat(challengeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChallengeType() throws Exception {
        int databaseSizeBeforeUpdate = challengeTypeRepository.findAll().size();
        challengeType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChallengeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(challengeType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChallengeType in the database
        List<ChallengeType> challengeTypeList = challengeTypeRepository.findAll();
        assertThat(challengeTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChallengeType() throws Exception {
        // Initialize the database
        challengeTypeRepository.saveAndFlush(challengeType);

        int databaseSizeBeforeDelete = challengeTypeRepository.findAll().size();

        // Delete the challengeType
        restChallengeTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, challengeType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChallengeType> challengeTypeList = challengeTypeRepository.findAll();
        assertThat(challengeTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
