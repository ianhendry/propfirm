package com.gracefl.propfirm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gracefl.propfirm.IntegrationTest;
import com.gracefl.propfirm.domain.AddressDetails;
import com.gracefl.propfirm.domain.SiteAccount;
import com.gracefl.propfirm.domain.TradeChallenge;
import com.gracefl.propfirm.domain.User;
import com.gracefl.propfirm.repository.SiteAccountRepository;
import com.gracefl.propfirm.service.criteria.SiteAccountCriteria;
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
 * Integration tests for the {@link SiteAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SiteAccountResourceIT {

    private static final String DEFAULT_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_USER_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_USER_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_USER_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_USER_PICTURE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_USER_BIO = "AAAAAAAAAA";
    private static final String UPDATED_USER_BIO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IN_ACTIVE = false;
    private static final Boolean UPDATED_IN_ACTIVE = true;

    private static final Instant DEFAULT_IN_ACTIVE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_IN_ACTIVE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/site-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SiteAccountRepository siteAccountRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSiteAccountMockMvc;

    private SiteAccount siteAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SiteAccount createEntity(EntityManager em) {
        SiteAccount siteAccount = new SiteAccount()
            .accountName(DEFAULT_ACCOUNT_NAME)
            .userPicture(DEFAULT_USER_PICTURE)
            .userPictureContentType(DEFAULT_USER_PICTURE_CONTENT_TYPE)
            .userBio(DEFAULT_USER_BIO)
            .inActive(DEFAULT_IN_ACTIVE)
            .inActiveDate(DEFAULT_IN_ACTIVE_DATE);
        return siteAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SiteAccount createUpdatedEntity(EntityManager em) {
        SiteAccount siteAccount = new SiteAccount()
            .accountName(UPDATED_ACCOUNT_NAME)
            .userPicture(UPDATED_USER_PICTURE)
            .userPictureContentType(UPDATED_USER_PICTURE_CONTENT_TYPE)
            .userBio(UPDATED_USER_BIO)
            .inActive(UPDATED_IN_ACTIVE)
            .inActiveDate(UPDATED_IN_ACTIVE_DATE);
        return siteAccount;
    }

    @BeforeEach
    public void initTest() {
        siteAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createSiteAccount() throws Exception {
        int databaseSizeBeforeCreate = siteAccountRepository.findAll().size();
        // Create the SiteAccount
        restSiteAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteAccount)))
            .andExpect(status().isCreated());

        // Validate the SiteAccount in the database
        List<SiteAccount> siteAccountList = siteAccountRepository.findAll();
        assertThat(siteAccountList).hasSize(databaseSizeBeforeCreate + 1);
        SiteAccount testSiteAccount = siteAccountList.get(siteAccountList.size() - 1);
        assertThat(testSiteAccount.getAccountName()).isEqualTo(DEFAULT_ACCOUNT_NAME);
        assertThat(testSiteAccount.getUserPicture()).isEqualTo(DEFAULT_USER_PICTURE);
        assertThat(testSiteAccount.getUserPictureContentType()).isEqualTo(DEFAULT_USER_PICTURE_CONTENT_TYPE);
        assertThat(testSiteAccount.getUserBio()).isEqualTo(DEFAULT_USER_BIO);
        assertThat(testSiteAccount.getInActive()).isEqualTo(DEFAULT_IN_ACTIVE);
        assertThat(testSiteAccount.getInActiveDate()).isEqualTo(DEFAULT_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void createSiteAccountWithExistingId() throws Exception {
        // Create the SiteAccount with an existing ID
        siteAccount.setId(1L);

        int databaseSizeBeforeCreate = siteAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSiteAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteAccount)))
            .andExpect(status().isBadRequest());

        // Validate the SiteAccount in the database
        List<SiteAccount> siteAccountList = siteAccountRepository.findAll();
        assertThat(siteAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSiteAccounts() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        // Get all the siteAccountList
        restSiteAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(siteAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].userPictureContentType").value(hasItem(DEFAULT_USER_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].userPicture").value(hasItem(Base64Utils.encodeToString(DEFAULT_USER_PICTURE))))
            .andExpect(jsonPath("$.[*].userBio").value(hasItem(DEFAULT_USER_BIO.toString())))
            .andExpect(jsonPath("$.[*].inActive").value(hasItem(DEFAULT_IN_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].inActiveDate").value(hasItem(DEFAULT_IN_ACTIVE_DATE.toString())));
    }

    @Test
    @Transactional
    void getSiteAccount() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        // Get the siteAccount
        restSiteAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, siteAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(siteAccount.getId().intValue()))
            .andExpect(jsonPath("$.accountName").value(DEFAULT_ACCOUNT_NAME))
            .andExpect(jsonPath("$.userPictureContentType").value(DEFAULT_USER_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.userPicture").value(Base64Utils.encodeToString(DEFAULT_USER_PICTURE)))
            .andExpect(jsonPath("$.userBio").value(DEFAULT_USER_BIO.toString()))
            .andExpect(jsonPath("$.inActive").value(DEFAULT_IN_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.inActiveDate").value(DEFAULT_IN_ACTIVE_DATE.toString()));
    }

    @Test
    @Transactional
    void getSiteAccountsByIdFiltering() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        Long id = siteAccount.getId();

        defaultSiteAccountShouldBeFound("id.equals=" + id);
        defaultSiteAccountShouldNotBeFound("id.notEquals=" + id);

        defaultSiteAccountShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSiteAccountShouldNotBeFound("id.greaterThan=" + id);

        defaultSiteAccountShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSiteAccountShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSiteAccountsByAccountNameIsEqualToSomething() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        // Get all the siteAccountList where accountName equals to DEFAULT_ACCOUNT_NAME
        defaultSiteAccountShouldBeFound("accountName.equals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the siteAccountList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultSiteAccountShouldNotBeFound("accountName.equals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllSiteAccountsByAccountNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        // Get all the siteAccountList where accountName not equals to DEFAULT_ACCOUNT_NAME
        defaultSiteAccountShouldNotBeFound("accountName.notEquals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the siteAccountList where accountName not equals to UPDATED_ACCOUNT_NAME
        defaultSiteAccountShouldBeFound("accountName.notEquals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllSiteAccountsByAccountNameIsInShouldWork() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        // Get all the siteAccountList where accountName in DEFAULT_ACCOUNT_NAME or UPDATED_ACCOUNT_NAME
        defaultSiteAccountShouldBeFound("accountName.in=" + DEFAULT_ACCOUNT_NAME + "," + UPDATED_ACCOUNT_NAME);

        // Get all the siteAccountList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultSiteAccountShouldNotBeFound("accountName.in=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllSiteAccountsByAccountNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        // Get all the siteAccountList where accountName is not null
        defaultSiteAccountShouldBeFound("accountName.specified=true");

        // Get all the siteAccountList where accountName is null
        defaultSiteAccountShouldNotBeFound("accountName.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteAccountsByAccountNameContainsSomething() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        // Get all the siteAccountList where accountName contains DEFAULT_ACCOUNT_NAME
        defaultSiteAccountShouldBeFound("accountName.contains=" + DEFAULT_ACCOUNT_NAME);

        // Get all the siteAccountList where accountName contains UPDATED_ACCOUNT_NAME
        defaultSiteAccountShouldNotBeFound("accountName.contains=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllSiteAccountsByAccountNameNotContainsSomething() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        // Get all the siteAccountList where accountName does not contain DEFAULT_ACCOUNT_NAME
        defaultSiteAccountShouldNotBeFound("accountName.doesNotContain=" + DEFAULT_ACCOUNT_NAME);

        // Get all the siteAccountList where accountName does not contain UPDATED_ACCOUNT_NAME
        defaultSiteAccountShouldBeFound("accountName.doesNotContain=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllSiteAccountsByInActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        // Get all the siteAccountList where inActive equals to DEFAULT_IN_ACTIVE
        defaultSiteAccountShouldBeFound("inActive.equals=" + DEFAULT_IN_ACTIVE);

        // Get all the siteAccountList where inActive equals to UPDATED_IN_ACTIVE
        defaultSiteAccountShouldNotBeFound("inActive.equals=" + UPDATED_IN_ACTIVE);
    }

    @Test
    @Transactional
    void getAllSiteAccountsByInActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        // Get all the siteAccountList where inActive not equals to DEFAULT_IN_ACTIVE
        defaultSiteAccountShouldNotBeFound("inActive.notEquals=" + DEFAULT_IN_ACTIVE);

        // Get all the siteAccountList where inActive not equals to UPDATED_IN_ACTIVE
        defaultSiteAccountShouldBeFound("inActive.notEquals=" + UPDATED_IN_ACTIVE);
    }

    @Test
    @Transactional
    void getAllSiteAccountsByInActiveIsInShouldWork() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        // Get all the siteAccountList where inActive in DEFAULT_IN_ACTIVE or UPDATED_IN_ACTIVE
        defaultSiteAccountShouldBeFound("inActive.in=" + DEFAULT_IN_ACTIVE + "," + UPDATED_IN_ACTIVE);

        // Get all the siteAccountList where inActive equals to UPDATED_IN_ACTIVE
        defaultSiteAccountShouldNotBeFound("inActive.in=" + UPDATED_IN_ACTIVE);
    }

    @Test
    @Transactional
    void getAllSiteAccountsByInActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        // Get all the siteAccountList where inActive is not null
        defaultSiteAccountShouldBeFound("inActive.specified=true");

        // Get all the siteAccountList where inActive is null
        defaultSiteAccountShouldNotBeFound("inActive.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteAccountsByInActiveDateIsEqualToSomething() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        // Get all the siteAccountList where inActiveDate equals to DEFAULT_IN_ACTIVE_DATE
        defaultSiteAccountShouldBeFound("inActiveDate.equals=" + DEFAULT_IN_ACTIVE_DATE);

        // Get all the siteAccountList where inActiveDate equals to UPDATED_IN_ACTIVE_DATE
        defaultSiteAccountShouldNotBeFound("inActiveDate.equals=" + UPDATED_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void getAllSiteAccountsByInActiveDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        // Get all the siteAccountList where inActiveDate not equals to DEFAULT_IN_ACTIVE_DATE
        defaultSiteAccountShouldNotBeFound("inActiveDate.notEquals=" + DEFAULT_IN_ACTIVE_DATE);

        // Get all the siteAccountList where inActiveDate not equals to UPDATED_IN_ACTIVE_DATE
        defaultSiteAccountShouldBeFound("inActiveDate.notEquals=" + UPDATED_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void getAllSiteAccountsByInActiveDateIsInShouldWork() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        // Get all the siteAccountList where inActiveDate in DEFAULT_IN_ACTIVE_DATE or UPDATED_IN_ACTIVE_DATE
        defaultSiteAccountShouldBeFound("inActiveDate.in=" + DEFAULT_IN_ACTIVE_DATE + "," + UPDATED_IN_ACTIVE_DATE);

        // Get all the siteAccountList where inActiveDate equals to UPDATED_IN_ACTIVE_DATE
        defaultSiteAccountShouldNotBeFound("inActiveDate.in=" + UPDATED_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void getAllSiteAccountsByInActiveDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        // Get all the siteAccountList where inActiveDate is not null
        defaultSiteAccountShouldBeFound("inActiveDate.specified=true");

        // Get all the siteAccountList where inActiveDate is null
        defaultSiteAccountShouldNotBeFound("inActiveDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSiteAccountsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        siteAccount.setUser(user);
        siteAccountRepository.saveAndFlush(siteAccount);
        Long userId = user.getId();

        // Get all the siteAccountList where user equals to userId
        defaultSiteAccountShouldBeFound("userId.equals=" + userId);

        // Get all the siteAccountList where user equals to (userId + 1)
        defaultSiteAccountShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllSiteAccountsByAddressDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);
        AddressDetails addressDetails = AddressDetailsResourceIT.createEntity(em);
        em.persist(addressDetails);
        em.flush();
        siteAccount.setAddressDetails(addressDetails);
        siteAccountRepository.saveAndFlush(siteAccount);
        Long addressDetailsId = addressDetails.getId();

        // Get all the siteAccountList where addressDetails equals to addressDetailsId
        defaultSiteAccountShouldBeFound("addressDetailsId.equals=" + addressDetailsId);

        // Get all the siteAccountList where addressDetails equals to (addressDetailsId + 1)
        defaultSiteAccountShouldNotBeFound("addressDetailsId.equals=" + (addressDetailsId + 1));
    }

    @Test
    @Transactional
    void getAllSiteAccountsByTradeChallengeIsEqualToSomething() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);
        TradeChallenge tradeChallenge = TradeChallengeResourceIT.createEntity(em);
        em.persist(tradeChallenge);
        em.flush();
        siteAccount.addTradeChallenge(tradeChallenge);
        siteAccountRepository.saveAndFlush(siteAccount);
        Long tradeChallengeId = tradeChallenge.getId();

        // Get all the siteAccountList where tradeChallenge equals to tradeChallengeId
        defaultSiteAccountShouldBeFound("tradeChallengeId.equals=" + tradeChallengeId);

        // Get all the siteAccountList where tradeChallenge equals to (tradeChallengeId + 1)
        defaultSiteAccountShouldNotBeFound("tradeChallengeId.equals=" + (tradeChallengeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSiteAccountShouldBeFound(String filter) throws Exception {
        restSiteAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(siteAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].userPictureContentType").value(hasItem(DEFAULT_USER_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].userPicture").value(hasItem(Base64Utils.encodeToString(DEFAULT_USER_PICTURE))))
            .andExpect(jsonPath("$.[*].userBio").value(hasItem(DEFAULT_USER_BIO.toString())))
            .andExpect(jsonPath("$.[*].inActive").value(hasItem(DEFAULT_IN_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].inActiveDate").value(hasItem(DEFAULT_IN_ACTIVE_DATE.toString())));

        // Check, that the count call also returns 1
        restSiteAccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSiteAccountShouldNotBeFound(String filter) throws Exception {
        restSiteAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSiteAccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSiteAccount() throws Exception {
        // Get the siteAccount
        restSiteAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSiteAccount() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        int databaseSizeBeforeUpdate = siteAccountRepository.findAll().size();

        // Update the siteAccount
        SiteAccount updatedSiteAccount = siteAccountRepository.findById(siteAccount.getId()).get();
        // Disconnect from session so that the updates on updatedSiteAccount are not directly saved in db
        em.detach(updatedSiteAccount);
        updatedSiteAccount
            .accountName(UPDATED_ACCOUNT_NAME)
            .userPicture(UPDATED_USER_PICTURE)
            .userPictureContentType(UPDATED_USER_PICTURE_CONTENT_TYPE)
            .userBio(UPDATED_USER_BIO)
            .inActive(UPDATED_IN_ACTIVE)
            .inActiveDate(UPDATED_IN_ACTIVE_DATE);

        restSiteAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSiteAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSiteAccount))
            )
            .andExpect(status().isOk());

        // Validate the SiteAccount in the database
        List<SiteAccount> siteAccountList = siteAccountRepository.findAll();
        assertThat(siteAccountList).hasSize(databaseSizeBeforeUpdate);
        SiteAccount testSiteAccount = siteAccountList.get(siteAccountList.size() - 1);
        assertThat(testSiteAccount.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testSiteAccount.getUserPicture()).isEqualTo(UPDATED_USER_PICTURE);
        assertThat(testSiteAccount.getUserPictureContentType()).isEqualTo(UPDATED_USER_PICTURE_CONTENT_TYPE);
        assertThat(testSiteAccount.getUserBio()).isEqualTo(UPDATED_USER_BIO);
        assertThat(testSiteAccount.getInActive()).isEqualTo(UPDATED_IN_ACTIVE);
        assertThat(testSiteAccount.getInActiveDate()).isEqualTo(UPDATED_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSiteAccount() throws Exception {
        int databaseSizeBeforeUpdate = siteAccountRepository.findAll().size();
        siteAccount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, siteAccount.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(siteAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteAccount in the database
        List<SiteAccount> siteAccountList = siteAccountRepository.findAll();
        assertThat(siteAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSiteAccount() throws Exception {
        int databaseSizeBeforeUpdate = siteAccountRepository.findAll().size();
        siteAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(siteAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteAccount in the database
        List<SiteAccount> siteAccountList = siteAccountRepository.findAll();
        assertThat(siteAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSiteAccount() throws Exception {
        int databaseSizeBeforeUpdate = siteAccountRepository.findAll().size();
        siteAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteAccountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteAccount)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SiteAccount in the database
        List<SiteAccount> siteAccountList = siteAccountRepository.findAll();
        assertThat(siteAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSiteAccountWithPatch() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        int databaseSizeBeforeUpdate = siteAccountRepository.findAll().size();

        // Update the siteAccount using partial update
        SiteAccount partialUpdatedSiteAccount = new SiteAccount();
        partialUpdatedSiteAccount.setId(siteAccount.getId());

        partialUpdatedSiteAccount.inActive(UPDATED_IN_ACTIVE).inActiveDate(UPDATED_IN_ACTIVE_DATE);

        restSiteAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSiteAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSiteAccount))
            )
            .andExpect(status().isOk());

        // Validate the SiteAccount in the database
        List<SiteAccount> siteAccountList = siteAccountRepository.findAll();
        assertThat(siteAccountList).hasSize(databaseSizeBeforeUpdate);
        SiteAccount testSiteAccount = siteAccountList.get(siteAccountList.size() - 1);
        assertThat(testSiteAccount.getAccountName()).isEqualTo(DEFAULT_ACCOUNT_NAME);
        assertThat(testSiteAccount.getUserPicture()).isEqualTo(DEFAULT_USER_PICTURE);
        assertThat(testSiteAccount.getUserPictureContentType()).isEqualTo(DEFAULT_USER_PICTURE_CONTENT_TYPE);
        assertThat(testSiteAccount.getUserBio()).isEqualTo(DEFAULT_USER_BIO);
        assertThat(testSiteAccount.getInActive()).isEqualTo(UPDATED_IN_ACTIVE);
        assertThat(testSiteAccount.getInActiveDate()).isEqualTo(UPDATED_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSiteAccountWithPatch() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        int databaseSizeBeforeUpdate = siteAccountRepository.findAll().size();

        // Update the siteAccount using partial update
        SiteAccount partialUpdatedSiteAccount = new SiteAccount();
        partialUpdatedSiteAccount.setId(siteAccount.getId());

        partialUpdatedSiteAccount
            .accountName(UPDATED_ACCOUNT_NAME)
            .userPicture(UPDATED_USER_PICTURE)
            .userPictureContentType(UPDATED_USER_PICTURE_CONTENT_TYPE)
            .userBio(UPDATED_USER_BIO)
            .inActive(UPDATED_IN_ACTIVE)
            .inActiveDate(UPDATED_IN_ACTIVE_DATE);

        restSiteAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSiteAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSiteAccount))
            )
            .andExpect(status().isOk());

        // Validate the SiteAccount in the database
        List<SiteAccount> siteAccountList = siteAccountRepository.findAll();
        assertThat(siteAccountList).hasSize(databaseSizeBeforeUpdate);
        SiteAccount testSiteAccount = siteAccountList.get(siteAccountList.size() - 1);
        assertThat(testSiteAccount.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testSiteAccount.getUserPicture()).isEqualTo(UPDATED_USER_PICTURE);
        assertThat(testSiteAccount.getUserPictureContentType()).isEqualTo(UPDATED_USER_PICTURE_CONTENT_TYPE);
        assertThat(testSiteAccount.getUserBio()).isEqualTo(UPDATED_USER_BIO);
        assertThat(testSiteAccount.getInActive()).isEqualTo(UPDATED_IN_ACTIVE);
        assertThat(testSiteAccount.getInActiveDate()).isEqualTo(UPDATED_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSiteAccount() throws Exception {
        int databaseSizeBeforeUpdate = siteAccountRepository.findAll().size();
        siteAccount.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, siteAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(siteAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteAccount in the database
        List<SiteAccount> siteAccountList = siteAccountRepository.findAll();
        assertThat(siteAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSiteAccount() throws Exception {
        int databaseSizeBeforeUpdate = siteAccountRepository.findAll().size();
        siteAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(siteAccount))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteAccount in the database
        List<SiteAccount> siteAccountList = siteAccountRepository.findAll();
        assertThat(siteAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSiteAccount() throws Exception {
        int databaseSizeBeforeUpdate = siteAccountRepository.findAll().size();
        siteAccount.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteAccountMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(siteAccount))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SiteAccount in the database
        List<SiteAccount> siteAccountList = siteAccountRepository.findAll();
        assertThat(siteAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSiteAccount() throws Exception {
        // Initialize the database
        siteAccountRepository.saveAndFlush(siteAccount);

        int databaseSizeBeforeDelete = siteAccountRepository.findAll().size();

        // Delete the siteAccount
        restSiteAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, siteAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SiteAccount> siteAccountList = siteAccountRepository.findAll();
        assertThat(siteAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
