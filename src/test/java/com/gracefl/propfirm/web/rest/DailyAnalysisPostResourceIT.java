package com.gracefl.propfirm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gracefl.propfirm.IntegrationTest;
import com.gracefl.propfirm.domain.DailyAnalysisPost;
import com.gracefl.propfirm.domain.Instrument;
import com.gracefl.propfirm.domain.User;
import com.gracefl.propfirm.domain.UserComment;
import com.gracefl.propfirm.repository.DailyAnalysisPostRepository;
import com.gracefl.propfirm.service.criteria.DailyAnalysisPostCriteria;
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
 * Integration tests for the {@link DailyAnalysisPostResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DailyAnalysisPostResourceIT {

    private static final String DEFAULT_POST_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_POST_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_ADDED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ADDED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_BACKGROUND_VOLUME = "AAAAAAAAAA";
    private static final String UPDATED_BACKGROUND_VOLUME = "BBBBBBBBBB";

    private static final String DEFAULT_OVERALL_THOUGHTS = "AAAAAAAAAA";
    private static final String UPDATED_OVERALL_THOUGHTS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_WEEKLY_CHART = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_WEEKLY_CHART = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_WEEKLY_CHART_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_WEEKLY_CHART_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_DAILY_CHART = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DAILY_CHART = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DAILY_CHART_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DAILY_CHART_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_ONE_HR_CHART = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ONE_HR_CHART = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ONE_HR_CHART_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ONE_HR_CHART_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_PLAN_FOR_TODAY = "AAAAAAAAAA";
    private static final String UPDATED_PLAN_FOR_TODAY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE = false;
    private static final Boolean UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE = true;

    private static final String ENTITY_API_URL = "/api/daily-analysis-posts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DailyAnalysisPostRepository dailyAnalysisPostRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDailyAnalysisPostMockMvc;

    private DailyAnalysisPost dailyAnalysisPost;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DailyAnalysisPost createEntity(EntityManager em) {
        DailyAnalysisPost dailyAnalysisPost = new DailyAnalysisPost()
            .postTitle(DEFAULT_POST_TITLE)
            .dateAdded(DEFAULT_DATE_ADDED)
            .backgroundVolume(DEFAULT_BACKGROUND_VOLUME)
            .overallThoughts(DEFAULT_OVERALL_THOUGHTS)
            .weeklyChart(DEFAULT_WEEKLY_CHART)
            .weeklyChartContentType(DEFAULT_WEEKLY_CHART_CONTENT_TYPE)
            .dailyChart(DEFAULT_DAILY_CHART)
            .dailyChartContentType(DEFAULT_DAILY_CHART_CONTENT_TYPE)
            .oneHrChart(DEFAULT_ONE_HR_CHART)
            .oneHrChartContentType(DEFAULT_ONE_HR_CHART_CONTENT_TYPE)
            .planForToday(DEFAULT_PLAN_FOR_TODAY)
            .makePublicVisibleOnSite(DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE);
        return dailyAnalysisPost;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DailyAnalysisPost createUpdatedEntity(EntityManager em) {
        DailyAnalysisPost dailyAnalysisPost = new DailyAnalysisPost()
            .postTitle(UPDATED_POST_TITLE)
            .dateAdded(UPDATED_DATE_ADDED)
            .backgroundVolume(UPDATED_BACKGROUND_VOLUME)
            .overallThoughts(UPDATED_OVERALL_THOUGHTS)
            .weeklyChart(UPDATED_WEEKLY_CHART)
            .weeklyChartContentType(UPDATED_WEEKLY_CHART_CONTENT_TYPE)
            .dailyChart(UPDATED_DAILY_CHART)
            .dailyChartContentType(UPDATED_DAILY_CHART_CONTENT_TYPE)
            .oneHrChart(UPDATED_ONE_HR_CHART)
            .oneHrChartContentType(UPDATED_ONE_HR_CHART_CONTENT_TYPE)
            .planForToday(UPDATED_PLAN_FOR_TODAY)
            .makePublicVisibleOnSite(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);
        return dailyAnalysisPost;
    }

    @BeforeEach
    public void initTest() {
        dailyAnalysisPost = createEntity(em);
    }

    @Test
    @Transactional
    void createDailyAnalysisPost() throws Exception {
        int databaseSizeBeforeCreate = dailyAnalysisPostRepository.findAll().size();
        // Create the DailyAnalysisPost
        restDailyAnalysisPostMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dailyAnalysisPost))
            )
            .andExpect(status().isCreated());

        // Validate the DailyAnalysisPost in the database
        List<DailyAnalysisPost> dailyAnalysisPostList = dailyAnalysisPostRepository.findAll();
        assertThat(dailyAnalysisPostList).hasSize(databaseSizeBeforeCreate + 1);
        DailyAnalysisPost testDailyAnalysisPost = dailyAnalysisPostList.get(dailyAnalysisPostList.size() - 1);
        assertThat(testDailyAnalysisPost.getPostTitle()).isEqualTo(DEFAULT_POST_TITLE);
        assertThat(testDailyAnalysisPost.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testDailyAnalysisPost.getBackgroundVolume()).isEqualTo(DEFAULT_BACKGROUND_VOLUME);
        assertThat(testDailyAnalysisPost.getOverallThoughts()).isEqualTo(DEFAULT_OVERALL_THOUGHTS);
        assertThat(testDailyAnalysisPost.getWeeklyChart()).isEqualTo(DEFAULT_WEEKLY_CHART);
        assertThat(testDailyAnalysisPost.getWeeklyChartContentType()).isEqualTo(DEFAULT_WEEKLY_CHART_CONTENT_TYPE);
        assertThat(testDailyAnalysisPost.getDailyChart()).isEqualTo(DEFAULT_DAILY_CHART);
        assertThat(testDailyAnalysisPost.getDailyChartContentType()).isEqualTo(DEFAULT_DAILY_CHART_CONTENT_TYPE);
        assertThat(testDailyAnalysisPost.getOneHrChart()).isEqualTo(DEFAULT_ONE_HR_CHART);
        assertThat(testDailyAnalysisPost.getOneHrChartContentType()).isEqualTo(DEFAULT_ONE_HR_CHART_CONTENT_TYPE);
        assertThat(testDailyAnalysisPost.getPlanForToday()).isEqualTo(DEFAULT_PLAN_FOR_TODAY);
        assertThat(testDailyAnalysisPost.getMakePublicVisibleOnSite()).isEqualTo(DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE);
    }

    @Test
    @Transactional
    void createDailyAnalysisPostWithExistingId() throws Exception {
        // Create the DailyAnalysisPost with an existing ID
        dailyAnalysisPost.setId(1L);

        int databaseSizeBeforeCreate = dailyAnalysisPostRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDailyAnalysisPostMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dailyAnalysisPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyAnalysisPost in the database
        List<DailyAnalysisPost> dailyAnalysisPostList = dailyAnalysisPostRepository.findAll();
        assertThat(dailyAnalysisPostList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPostTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = dailyAnalysisPostRepository.findAll().size();
        // set the field null
        dailyAnalysisPost.setPostTitle(null);

        // Create the DailyAnalysisPost, which fails.

        restDailyAnalysisPostMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dailyAnalysisPost))
            )
            .andExpect(status().isBadRequest());

        List<DailyAnalysisPost> dailyAnalysisPostList = dailyAnalysisPostRepository.findAll();
        assertThat(dailyAnalysisPostList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateAddedIsRequired() throws Exception {
        int databaseSizeBeforeTest = dailyAnalysisPostRepository.findAll().size();
        // set the field null
        dailyAnalysisPost.setDateAdded(null);

        // Create the DailyAnalysisPost, which fails.

        restDailyAnalysisPostMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dailyAnalysisPost))
            )
            .andExpect(status().isBadRequest());

        List<DailyAnalysisPost> dailyAnalysisPostList = dailyAnalysisPostRepository.findAll();
        assertThat(dailyAnalysisPostList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDailyAnalysisPosts() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        // Get all the dailyAnalysisPostList
        restDailyAnalysisPostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dailyAnalysisPost.getId().intValue())))
            .andExpect(jsonPath("$.[*].postTitle").value(hasItem(DEFAULT_POST_TITLE)))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].backgroundVolume").value(hasItem(DEFAULT_BACKGROUND_VOLUME.toString())))
            .andExpect(jsonPath("$.[*].overallThoughts").value(hasItem(DEFAULT_OVERALL_THOUGHTS.toString())))
            .andExpect(jsonPath("$.[*].weeklyChartContentType").value(hasItem(DEFAULT_WEEKLY_CHART_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].weeklyChart").value(hasItem(Base64Utils.encodeToString(DEFAULT_WEEKLY_CHART))))
            .andExpect(jsonPath("$.[*].dailyChartContentType").value(hasItem(DEFAULT_DAILY_CHART_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].dailyChart").value(hasItem(Base64Utils.encodeToString(DEFAULT_DAILY_CHART))))
            .andExpect(jsonPath("$.[*].oneHrChartContentType").value(hasItem(DEFAULT_ONE_HR_CHART_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].oneHrChart").value(hasItem(Base64Utils.encodeToString(DEFAULT_ONE_HR_CHART))))
            .andExpect(jsonPath("$.[*].planForToday").value(hasItem(DEFAULT_PLAN_FOR_TODAY.toString())))
            .andExpect(jsonPath("$.[*].makePublicVisibleOnSite").value(hasItem(DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE.booleanValue())));
    }

    @Test
    @Transactional
    void getDailyAnalysisPost() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        // Get the dailyAnalysisPost
        restDailyAnalysisPostMockMvc
            .perform(get(ENTITY_API_URL_ID, dailyAnalysisPost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dailyAnalysisPost.getId().intValue()))
            .andExpect(jsonPath("$.postTitle").value(DEFAULT_POST_TITLE))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.backgroundVolume").value(DEFAULT_BACKGROUND_VOLUME.toString()))
            .andExpect(jsonPath("$.overallThoughts").value(DEFAULT_OVERALL_THOUGHTS.toString()))
            .andExpect(jsonPath("$.weeklyChartContentType").value(DEFAULT_WEEKLY_CHART_CONTENT_TYPE))
            .andExpect(jsonPath("$.weeklyChart").value(Base64Utils.encodeToString(DEFAULT_WEEKLY_CHART)))
            .andExpect(jsonPath("$.dailyChartContentType").value(DEFAULT_DAILY_CHART_CONTENT_TYPE))
            .andExpect(jsonPath("$.dailyChart").value(Base64Utils.encodeToString(DEFAULT_DAILY_CHART)))
            .andExpect(jsonPath("$.oneHrChartContentType").value(DEFAULT_ONE_HR_CHART_CONTENT_TYPE))
            .andExpect(jsonPath("$.oneHrChart").value(Base64Utils.encodeToString(DEFAULT_ONE_HR_CHART)))
            .andExpect(jsonPath("$.planForToday").value(DEFAULT_PLAN_FOR_TODAY.toString()))
            .andExpect(jsonPath("$.makePublicVisibleOnSite").value(DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE.booleanValue()));
    }

    @Test
    @Transactional
    void getDailyAnalysisPostsByIdFiltering() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        Long id = dailyAnalysisPost.getId();

        defaultDailyAnalysisPostShouldBeFound("id.equals=" + id);
        defaultDailyAnalysisPostShouldNotBeFound("id.notEquals=" + id);

        defaultDailyAnalysisPostShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDailyAnalysisPostShouldNotBeFound("id.greaterThan=" + id);

        defaultDailyAnalysisPostShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDailyAnalysisPostShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDailyAnalysisPostsByPostTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        // Get all the dailyAnalysisPostList where postTitle equals to DEFAULT_POST_TITLE
        defaultDailyAnalysisPostShouldBeFound("postTitle.equals=" + DEFAULT_POST_TITLE);

        // Get all the dailyAnalysisPostList where postTitle equals to UPDATED_POST_TITLE
        defaultDailyAnalysisPostShouldNotBeFound("postTitle.equals=" + UPDATED_POST_TITLE);
    }

    @Test
    @Transactional
    void getAllDailyAnalysisPostsByPostTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        // Get all the dailyAnalysisPostList where postTitle not equals to DEFAULT_POST_TITLE
        defaultDailyAnalysisPostShouldNotBeFound("postTitle.notEquals=" + DEFAULT_POST_TITLE);

        // Get all the dailyAnalysisPostList where postTitle not equals to UPDATED_POST_TITLE
        defaultDailyAnalysisPostShouldBeFound("postTitle.notEquals=" + UPDATED_POST_TITLE);
    }

    @Test
    @Transactional
    void getAllDailyAnalysisPostsByPostTitleIsInShouldWork() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        // Get all the dailyAnalysisPostList where postTitle in DEFAULT_POST_TITLE or UPDATED_POST_TITLE
        defaultDailyAnalysisPostShouldBeFound("postTitle.in=" + DEFAULT_POST_TITLE + "," + UPDATED_POST_TITLE);

        // Get all the dailyAnalysisPostList where postTitle equals to UPDATED_POST_TITLE
        defaultDailyAnalysisPostShouldNotBeFound("postTitle.in=" + UPDATED_POST_TITLE);
    }

    @Test
    @Transactional
    void getAllDailyAnalysisPostsByPostTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        // Get all the dailyAnalysisPostList where postTitle is not null
        defaultDailyAnalysisPostShouldBeFound("postTitle.specified=true");

        // Get all the dailyAnalysisPostList where postTitle is null
        defaultDailyAnalysisPostShouldNotBeFound("postTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllDailyAnalysisPostsByPostTitleContainsSomething() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        // Get all the dailyAnalysisPostList where postTitle contains DEFAULT_POST_TITLE
        defaultDailyAnalysisPostShouldBeFound("postTitle.contains=" + DEFAULT_POST_TITLE);

        // Get all the dailyAnalysisPostList where postTitle contains UPDATED_POST_TITLE
        defaultDailyAnalysisPostShouldNotBeFound("postTitle.contains=" + UPDATED_POST_TITLE);
    }

    @Test
    @Transactional
    void getAllDailyAnalysisPostsByPostTitleNotContainsSomething() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        // Get all the dailyAnalysisPostList where postTitle does not contain DEFAULT_POST_TITLE
        defaultDailyAnalysisPostShouldNotBeFound("postTitle.doesNotContain=" + DEFAULT_POST_TITLE);

        // Get all the dailyAnalysisPostList where postTitle does not contain UPDATED_POST_TITLE
        defaultDailyAnalysisPostShouldBeFound("postTitle.doesNotContain=" + UPDATED_POST_TITLE);
    }

    @Test
    @Transactional
    void getAllDailyAnalysisPostsByDateAddedIsEqualToSomething() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        // Get all the dailyAnalysisPostList where dateAdded equals to DEFAULT_DATE_ADDED
        defaultDailyAnalysisPostShouldBeFound("dateAdded.equals=" + DEFAULT_DATE_ADDED);

        // Get all the dailyAnalysisPostList where dateAdded equals to UPDATED_DATE_ADDED
        defaultDailyAnalysisPostShouldNotBeFound("dateAdded.equals=" + UPDATED_DATE_ADDED);
    }

    @Test
    @Transactional
    void getAllDailyAnalysisPostsByDateAddedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        // Get all the dailyAnalysisPostList where dateAdded not equals to DEFAULT_DATE_ADDED
        defaultDailyAnalysisPostShouldNotBeFound("dateAdded.notEquals=" + DEFAULT_DATE_ADDED);

        // Get all the dailyAnalysisPostList where dateAdded not equals to UPDATED_DATE_ADDED
        defaultDailyAnalysisPostShouldBeFound("dateAdded.notEquals=" + UPDATED_DATE_ADDED);
    }

    @Test
    @Transactional
    void getAllDailyAnalysisPostsByDateAddedIsInShouldWork() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        // Get all the dailyAnalysisPostList where dateAdded in DEFAULT_DATE_ADDED or UPDATED_DATE_ADDED
        defaultDailyAnalysisPostShouldBeFound("dateAdded.in=" + DEFAULT_DATE_ADDED + "," + UPDATED_DATE_ADDED);

        // Get all the dailyAnalysisPostList where dateAdded equals to UPDATED_DATE_ADDED
        defaultDailyAnalysisPostShouldNotBeFound("dateAdded.in=" + UPDATED_DATE_ADDED);
    }

    @Test
    @Transactional
    void getAllDailyAnalysisPostsByDateAddedIsNullOrNotNull() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        // Get all the dailyAnalysisPostList where dateAdded is not null
        defaultDailyAnalysisPostShouldBeFound("dateAdded.specified=true");

        // Get all the dailyAnalysisPostList where dateAdded is null
        defaultDailyAnalysisPostShouldNotBeFound("dateAdded.specified=false");
    }

    @Test
    @Transactional
    void getAllDailyAnalysisPostsByMakePublicVisibleOnSiteIsEqualToSomething() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        // Get all the dailyAnalysisPostList where makePublicVisibleOnSite equals to DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE
        defaultDailyAnalysisPostShouldBeFound("makePublicVisibleOnSite.equals=" + DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE);

        // Get all the dailyAnalysisPostList where makePublicVisibleOnSite equals to UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE
        defaultDailyAnalysisPostShouldNotBeFound("makePublicVisibleOnSite.equals=" + UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);
    }

    @Test
    @Transactional
    void getAllDailyAnalysisPostsByMakePublicVisibleOnSiteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        // Get all the dailyAnalysisPostList where makePublicVisibleOnSite not equals to DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE
        defaultDailyAnalysisPostShouldNotBeFound("makePublicVisibleOnSite.notEquals=" + DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE);

        // Get all the dailyAnalysisPostList where makePublicVisibleOnSite not equals to UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE
        defaultDailyAnalysisPostShouldBeFound("makePublicVisibleOnSite.notEquals=" + UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);
    }

    @Test
    @Transactional
    void getAllDailyAnalysisPostsByMakePublicVisibleOnSiteIsInShouldWork() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        // Get all the dailyAnalysisPostList where makePublicVisibleOnSite in DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE or UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE
        defaultDailyAnalysisPostShouldBeFound(
            "makePublicVisibleOnSite.in=" + DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE + "," + UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE
        );

        // Get all the dailyAnalysisPostList where makePublicVisibleOnSite equals to UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE
        defaultDailyAnalysisPostShouldNotBeFound("makePublicVisibleOnSite.in=" + UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);
    }

    @Test
    @Transactional
    void getAllDailyAnalysisPostsByMakePublicVisibleOnSiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        // Get all the dailyAnalysisPostList where makePublicVisibleOnSite is not null
        defaultDailyAnalysisPostShouldBeFound("makePublicVisibleOnSite.specified=true");

        // Get all the dailyAnalysisPostList where makePublicVisibleOnSite is null
        defaultDailyAnalysisPostShouldNotBeFound("makePublicVisibleOnSite.specified=false");
    }

    @Test
    @Transactional
    void getAllDailyAnalysisPostsByInstrumentIsEqualToSomething() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);
        Instrument instrument = InstrumentResourceIT.createEntity(em);
        em.persist(instrument);
        em.flush();
        dailyAnalysisPost.setInstrument(instrument);
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);
        Long instrumentId = instrument.getId();

        // Get all the dailyAnalysisPostList where instrument equals to instrumentId
        defaultDailyAnalysisPostShouldBeFound("instrumentId.equals=" + instrumentId);

        // Get all the dailyAnalysisPostList where instrument equals to (instrumentId + 1)
        defaultDailyAnalysisPostShouldNotBeFound("instrumentId.equals=" + (instrumentId + 1));
    }

    @Test
    @Transactional
    void getAllDailyAnalysisPostsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        dailyAnalysisPost.setUser(user);
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);
        Long userId = user.getId();

        // Get all the dailyAnalysisPostList where user equals to userId
        defaultDailyAnalysisPostShouldBeFound("userId.equals=" + userId);

        // Get all the dailyAnalysisPostList where user equals to (userId + 1)
        defaultDailyAnalysisPostShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllDailyAnalysisPostsByUserCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);
        UserComment userComment = UserCommentResourceIT.createEntity(em);
        em.persist(userComment);
        em.flush();
        dailyAnalysisPost.addUserComment(userComment);
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);
        Long userCommentId = userComment.getId();

        // Get all the dailyAnalysisPostList where userComment equals to userCommentId
        defaultDailyAnalysisPostShouldBeFound("userCommentId.equals=" + userCommentId);

        // Get all the dailyAnalysisPostList where userComment equals to (userCommentId + 1)
        defaultDailyAnalysisPostShouldNotBeFound("userCommentId.equals=" + (userCommentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDailyAnalysisPostShouldBeFound(String filter) throws Exception {
        restDailyAnalysisPostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dailyAnalysisPost.getId().intValue())))
            .andExpect(jsonPath("$.[*].postTitle").value(hasItem(DEFAULT_POST_TITLE)))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].backgroundVolume").value(hasItem(DEFAULT_BACKGROUND_VOLUME.toString())))
            .andExpect(jsonPath("$.[*].overallThoughts").value(hasItem(DEFAULT_OVERALL_THOUGHTS.toString())))
            .andExpect(jsonPath("$.[*].weeklyChartContentType").value(hasItem(DEFAULT_WEEKLY_CHART_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].weeklyChart").value(hasItem(Base64Utils.encodeToString(DEFAULT_WEEKLY_CHART))))
            .andExpect(jsonPath("$.[*].dailyChartContentType").value(hasItem(DEFAULT_DAILY_CHART_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].dailyChart").value(hasItem(Base64Utils.encodeToString(DEFAULT_DAILY_CHART))))
            .andExpect(jsonPath("$.[*].oneHrChartContentType").value(hasItem(DEFAULT_ONE_HR_CHART_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].oneHrChart").value(hasItem(Base64Utils.encodeToString(DEFAULT_ONE_HR_CHART))))
            .andExpect(jsonPath("$.[*].planForToday").value(hasItem(DEFAULT_PLAN_FOR_TODAY.toString())))
            .andExpect(jsonPath("$.[*].makePublicVisibleOnSite").value(hasItem(DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE.booleanValue())));

        // Check, that the count call also returns 1
        restDailyAnalysisPostMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDailyAnalysisPostShouldNotBeFound(String filter) throws Exception {
        restDailyAnalysisPostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDailyAnalysisPostMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDailyAnalysisPost() throws Exception {
        // Get the dailyAnalysisPost
        restDailyAnalysisPostMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDailyAnalysisPost() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        int databaseSizeBeforeUpdate = dailyAnalysisPostRepository.findAll().size();

        // Update the dailyAnalysisPost
        DailyAnalysisPost updatedDailyAnalysisPost = dailyAnalysisPostRepository.findById(dailyAnalysisPost.getId()).get();
        // Disconnect from session so that the updates on updatedDailyAnalysisPost are not directly saved in db
        em.detach(updatedDailyAnalysisPost);
        updatedDailyAnalysisPost
            .postTitle(UPDATED_POST_TITLE)
            .dateAdded(UPDATED_DATE_ADDED)
            .backgroundVolume(UPDATED_BACKGROUND_VOLUME)
            .overallThoughts(UPDATED_OVERALL_THOUGHTS)
            .weeklyChart(UPDATED_WEEKLY_CHART)
            .weeklyChartContentType(UPDATED_WEEKLY_CHART_CONTENT_TYPE)
            .dailyChart(UPDATED_DAILY_CHART)
            .dailyChartContentType(UPDATED_DAILY_CHART_CONTENT_TYPE)
            .oneHrChart(UPDATED_ONE_HR_CHART)
            .oneHrChartContentType(UPDATED_ONE_HR_CHART_CONTENT_TYPE)
            .planForToday(UPDATED_PLAN_FOR_TODAY)
            .makePublicVisibleOnSite(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);

        restDailyAnalysisPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDailyAnalysisPost.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDailyAnalysisPost))
            )
            .andExpect(status().isOk());

        // Validate the DailyAnalysisPost in the database
        List<DailyAnalysisPost> dailyAnalysisPostList = dailyAnalysisPostRepository.findAll();
        assertThat(dailyAnalysisPostList).hasSize(databaseSizeBeforeUpdate);
        DailyAnalysisPost testDailyAnalysisPost = dailyAnalysisPostList.get(dailyAnalysisPostList.size() - 1);
        assertThat(testDailyAnalysisPost.getPostTitle()).isEqualTo(UPDATED_POST_TITLE);
        assertThat(testDailyAnalysisPost.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testDailyAnalysisPost.getBackgroundVolume()).isEqualTo(UPDATED_BACKGROUND_VOLUME);
        assertThat(testDailyAnalysisPost.getOverallThoughts()).isEqualTo(UPDATED_OVERALL_THOUGHTS);
        assertThat(testDailyAnalysisPost.getWeeklyChart()).isEqualTo(UPDATED_WEEKLY_CHART);
        assertThat(testDailyAnalysisPost.getWeeklyChartContentType()).isEqualTo(UPDATED_WEEKLY_CHART_CONTENT_TYPE);
        assertThat(testDailyAnalysisPost.getDailyChart()).isEqualTo(UPDATED_DAILY_CHART);
        assertThat(testDailyAnalysisPost.getDailyChartContentType()).isEqualTo(UPDATED_DAILY_CHART_CONTENT_TYPE);
        assertThat(testDailyAnalysisPost.getOneHrChart()).isEqualTo(UPDATED_ONE_HR_CHART);
        assertThat(testDailyAnalysisPost.getOneHrChartContentType()).isEqualTo(UPDATED_ONE_HR_CHART_CONTENT_TYPE);
        assertThat(testDailyAnalysisPost.getPlanForToday()).isEqualTo(UPDATED_PLAN_FOR_TODAY);
        assertThat(testDailyAnalysisPost.getMakePublicVisibleOnSite()).isEqualTo(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);
    }

    @Test
    @Transactional
    void putNonExistingDailyAnalysisPost() throws Exception {
        int databaseSizeBeforeUpdate = dailyAnalysisPostRepository.findAll().size();
        dailyAnalysisPost.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDailyAnalysisPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dailyAnalysisPost.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dailyAnalysisPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyAnalysisPost in the database
        List<DailyAnalysisPost> dailyAnalysisPostList = dailyAnalysisPostRepository.findAll();
        assertThat(dailyAnalysisPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDailyAnalysisPost() throws Exception {
        int databaseSizeBeforeUpdate = dailyAnalysisPostRepository.findAll().size();
        dailyAnalysisPost.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDailyAnalysisPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dailyAnalysisPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyAnalysisPost in the database
        List<DailyAnalysisPost> dailyAnalysisPostList = dailyAnalysisPostRepository.findAll();
        assertThat(dailyAnalysisPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDailyAnalysisPost() throws Exception {
        int databaseSizeBeforeUpdate = dailyAnalysisPostRepository.findAll().size();
        dailyAnalysisPost.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDailyAnalysisPostMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dailyAnalysisPost))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DailyAnalysisPost in the database
        List<DailyAnalysisPost> dailyAnalysisPostList = dailyAnalysisPostRepository.findAll();
        assertThat(dailyAnalysisPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDailyAnalysisPostWithPatch() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        int databaseSizeBeforeUpdate = dailyAnalysisPostRepository.findAll().size();

        // Update the dailyAnalysisPost using partial update
        DailyAnalysisPost partialUpdatedDailyAnalysisPost = new DailyAnalysisPost();
        partialUpdatedDailyAnalysisPost.setId(dailyAnalysisPost.getId());

        partialUpdatedDailyAnalysisPost
            .dateAdded(UPDATED_DATE_ADDED)
            .backgroundVolume(UPDATED_BACKGROUND_VOLUME)
            .oneHrChart(UPDATED_ONE_HR_CHART)
            .oneHrChartContentType(UPDATED_ONE_HR_CHART_CONTENT_TYPE)
            .makePublicVisibleOnSite(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);

        restDailyAnalysisPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDailyAnalysisPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDailyAnalysisPost))
            )
            .andExpect(status().isOk());

        // Validate the DailyAnalysisPost in the database
        List<DailyAnalysisPost> dailyAnalysisPostList = dailyAnalysisPostRepository.findAll();
        assertThat(dailyAnalysisPostList).hasSize(databaseSizeBeforeUpdate);
        DailyAnalysisPost testDailyAnalysisPost = dailyAnalysisPostList.get(dailyAnalysisPostList.size() - 1);
        assertThat(testDailyAnalysisPost.getPostTitle()).isEqualTo(DEFAULT_POST_TITLE);
        assertThat(testDailyAnalysisPost.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testDailyAnalysisPost.getBackgroundVolume()).isEqualTo(UPDATED_BACKGROUND_VOLUME);
        assertThat(testDailyAnalysisPost.getOverallThoughts()).isEqualTo(DEFAULT_OVERALL_THOUGHTS);
        assertThat(testDailyAnalysisPost.getWeeklyChart()).isEqualTo(DEFAULT_WEEKLY_CHART);
        assertThat(testDailyAnalysisPost.getWeeklyChartContentType()).isEqualTo(DEFAULT_WEEKLY_CHART_CONTENT_TYPE);
        assertThat(testDailyAnalysisPost.getDailyChart()).isEqualTo(DEFAULT_DAILY_CHART);
        assertThat(testDailyAnalysisPost.getDailyChartContentType()).isEqualTo(DEFAULT_DAILY_CHART_CONTENT_TYPE);
        assertThat(testDailyAnalysisPost.getOneHrChart()).isEqualTo(UPDATED_ONE_HR_CHART);
        assertThat(testDailyAnalysisPost.getOneHrChartContentType()).isEqualTo(UPDATED_ONE_HR_CHART_CONTENT_TYPE);
        assertThat(testDailyAnalysisPost.getPlanForToday()).isEqualTo(DEFAULT_PLAN_FOR_TODAY);
        assertThat(testDailyAnalysisPost.getMakePublicVisibleOnSite()).isEqualTo(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);
    }

    @Test
    @Transactional
    void fullUpdateDailyAnalysisPostWithPatch() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        int databaseSizeBeforeUpdate = dailyAnalysisPostRepository.findAll().size();

        // Update the dailyAnalysisPost using partial update
        DailyAnalysisPost partialUpdatedDailyAnalysisPost = new DailyAnalysisPost();
        partialUpdatedDailyAnalysisPost.setId(dailyAnalysisPost.getId());

        partialUpdatedDailyAnalysisPost
            .postTitle(UPDATED_POST_TITLE)
            .dateAdded(UPDATED_DATE_ADDED)
            .backgroundVolume(UPDATED_BACKGROUND_VOLUME)
            .overallThoughts(UPDATED_OVERALL_THOUGHTS)
            .weeklyChart(UPDATED_WEEKLY_CHART)
            .weeklyChartContentType(UPDATED_WEEKLY_CHART_CONTENT_TYPE)
            .dailyChart(UPDATED_DAILY_CHART)
            .dailyChartContentType(UPDATED_DAILY_CHART_CONTENT_TYPE)
            .oneHrChart(UPDATED_ONE_HR_CHART)
            .oneHrChartContentType(UPDATED_ONE_HR_CHART_CONTENT_TYPE)
            .planForToday(UPDATED_PLAN_FOR_TODAY)
            .makePublicVisibleOnSite(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);

        restDailyAnalysisPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDailyAnalysisPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDailyAnalysisPost))
            )
            .andExpect(status().isOk());

        // Validate the DailyAnalysisPost in the database
        List<DailyAnalysisPost> dailyAnalysisPostList = dailyAnalysisPostRepository.findAll();
        assertThat(dailyAnalysisPostList).hasSize(databaseSizeBeforeUpdate);
        DailyAnalysisPost testDailyAnalysisPost = dailyAnalysisPostList.get(dailyAnalysisPostList.size() - 1);
        assertThat(testDailyAnalysisPost.getPostTitle()).isEqualTo(UPDATED_POST_TITLE);
        assertThat(testDailyAnalysisPost.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testDailyAnalysisPost.getBackgroundVolume()).isEqualTo(UPDATED_BACKGROUND_VOLUME);
        assertThat(testDailyAnalysisPost.getOverallThoughts()).isEqualTo(UPDATED_OVERALL_THOUGHTS);
        assertThat(testDailyAnalysisPost.getWeeklyChart()).isEqualTo(UPDATED_WEEKLY_CHART);
        assertThat(testDailyAnalysisPost.getWeeklyChartContentType()).isEqualTo(UPDATED_WEEKLY_CHART_CONTENT_TYPE);
        assertThat(testDailyAnalysisPost.getDailyChart()).isEqualTo(UPDATED_DAILY_CHART);
        assertThat(testDailyAnalysisPost.getDailyChartContentType()).isEqualTo(UPDATED_DAILY_CHART_CONTENT_TYPE);
        assertThat(testDailyAnalysisPost.getOneHrChart()).isEqualTo(UPDATED_ONE_HR_CHART);
        assertThat(testDailyAnalysisPost.getOneHrChartContentType()).isEqualTo(UPDATED_ONE_HR_CHART_CONTENT_TYPE);
        assertThat(testDailyAnalysisPost.getPlanForToday()).isEqualTo(UPDATED_PLAN_FOR_TODAY);
        assertThat(testDailyAnalysisPost.getMakePublicVisibleOnSite()).isEqualTo(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);
    }

    @Test
    @Transactional
    void patchNonExistingDailyAnalysisPost() throws Exception {
        int databaseSizeBeforeUpdate = dailyAnalysisPostRepository.findAll().size();
        dailyAnalysisPost.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDailyAnalysisPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dailyAnalysisPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dailyAnalysisPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyAnalysisPost in the database
        List<DailyAnalysisPost> dailyAnalysisPostList = dailyAnalysisPostRepository.findAll();
        assertThat(dailyAnalysisPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDailyAnalysisPost() throws Exception {
        int databaseSizeBeforeUpdate = dailyAnalysisPostRepository.findAll().size();
        dailyAnalysisPost.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDailyAnalysisPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dailyAnalysisPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyAnalysisPost in the database
        List<DailyAnalysisPost> dailyAnalysisPostList = dailyAnalysisPostRepository.findAll();
        assertThat(dailyAnalysisPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDailyAnalysisPost() throws Exception {
        int databaseSizeBeforeUpdate = dailyAnalysisPostRepository.findAll().size();
        dailyAnalysisPost.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDailyAnalysisPostMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dailyAnalysisPost))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DailyAnalysisPost in the database
        List<DailyAnalysisPost> dailyAnalysisPostList = dailyAnalysisPostRepository.findAll();
        assertThat(dailyAnalysisPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDailyAnalysisPost() throws Exception {
        // Initialize the database
        dailyAnalysisPostRepository.saveAndFlush(dailyAnalysisPost);

        int databaseSizeBeforeDelete = dailyAnalysisPostRepository.findAll().size();

        // Delete the dailyAnalysisPost
        restDailyAnalysisPostMockMvc
            .perform(delete(ENTITY_API_URL_ID, dailyAnalysisPost.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DailyAnalysisPost> dailyAnalysisPostList = dailyAnalysisPostRepository.findAll();
        assertThat(dailyAnalysisPostList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
