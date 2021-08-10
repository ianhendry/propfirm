package com.gracefl.propfirm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gracefl.propfirm.IntegrationTest;
import com.gracefl.propfirm.domain.Mt4Trade;
import com.gracefl.propfirm.domain.TradeJournalPost;
import com.gracefl.propfirm.domain.User;
import com.gracefl.propfirm.domain.UserComment;
import com.gracefl.propfirm.repository.TradeJournalPostRepository;
import com.gracefl.propfirm.service.criteria.TradeJournalPostCriteria;
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
 * Integration tests for the {@link TradeJournalPostResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TradeJournalPostResourceIT {

    private static final String DEFAULT_POST_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_POST_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_ADDED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ADDED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_THOUGHTS_ON_PSYCHOLOGY = "AAAAAAAAAA";
    private static final String UPDATED_THOUGHTS_ON_PSYCHOLOGY = "BBBBBBBBBB";

    private static final String DEFAULT_THOUGHTS_ON_TRADE_PROCESS_ACCURACY = "AAAAAAAAAA";
    private static final String UPDATED_THOUGHTS_ON_TRADE_PROCESS_ACCURACY = "BBBBBBBBBB";

    private static final String DEFAULT_THOUGHTS_ON_AREAS_OF_STRENGTH = "AAAAAAAAAA";
    private static final String UPDATED_THOUGHTS_ON_AREAS_OF_STRENGTH = "BBBBBBBBBB";

    private static final String DEFAULT_THOUGHTS_ON_AREAS_FOR_IMPROVEMENT = "AAAAAAAAAA";
    private static final String UPDATED_THOUGHTS_ON_AREAS_FOR_IMPROVEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_AREA_OF_FOCUS_FOR_TOMORROW = "AAAAAAAAAA";
    private static final String UPDATED_AREA_OF_FOCUS_FOR_TOMORROW = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE = false;
    private static final Boolean UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE = true;

    private static final byte[] DEFAULT_ANY_MEDIA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ANY_MEDIA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ANY_MEDIA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ANY_MEDIA_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_ANY_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ANY_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ANY_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ANY_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/trade-journal-posts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TradeJournalPostRepository tradeJournalPostRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTradeJournalPostMockMvc;

    private TradeJournalPost tradeJournalPost;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TradeJournalPost createEntity(EntityManager em) {
        TradeJournalPost tradeJournalPost = new TradeJournalPost()
            .postTitle(DEFAULT_POST_TITLE)
            .dateAdded(DEFAULT_DATE_ADDED)
            .thoughtsOnPsychology(DEFAULT_THOUGHTS_ON_PSYCHOLOGY)
            .thoughtsOnTradeProcessAccuracy(DEFAULT_THOUGHTS_ON_TRADE_PROCESS_ACCURACY)
            .thoughtsOnAreasOfStrength(DEFAULT_THOUGHTS_ON_AREAS_OF_STRENGTH)
            .thoughtsOnAreasForImprovement(DEFAULT_THOUGHTS_ON_AREAS_FOR_IMPROVEMENT)
            .areaOfFocusForTomorrow(DEFAULT_AREA_OF_FOCUS_FOR_TOMORROW)
            .makePublicVisibleOnSite(DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE)
            .anyMedia(DEFAULT_ANY_MEDIA)
            .anyMediaContentType(DEFAULT_ANY_MEDIA_CONTENT_TYPE)
            .anyImage(DEFAULT_ANY_IMAGE)
            .anyImageContentType(DEFAULT_ANY_IMAGE_CONTENT_TYPE);
        return tradeJournalPost;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TradeJournalPost createUpdatedEntity(EntityManager em) {
        TradeJournalPost tradeJournalPost = new TradeJournalPost()
            .postTitle(UPDATED_POST_TITLE)
            .dateAdded(UPDATED_DATE_ADDED)
            .thoughtsOnPsychology(UPDATED_THOUGHTS_ON_PSYCHOLOGY)
            .thoughtsOnTradeProcessAccuracy(UPDATED_THOUGHTS_ON_TRADE_PROCESS_ACCURACY)
            .thoughtsOnAreasOfStrength(UPDATED_THOUGHTS_ON_AREAS_OF_STRENGTH)
            .thoughtsOnAreasForImprovement(UPDATED_THOUGHTS_ON_AREAS_FOR_IMPROVEMENT)
            .areaOfFocusForTomorrow(UPDATED_AREA_OF_FOCUS_FOR_TOMORROW)
            .makePublicVisibleOnSite(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE)
            .anyMedia(UPDATED_ANY_MEDIA)
            .anyMediaContentType(UPDATED_ANY_MEDIA_CONTENT_TYPE)
            .anyImage(UPDATED_ANY_IMAGE)
            .anyImageContentType(UPDATED_ANY_IMAGE_CONTENT_TYPE);
        return tradeJournalPost;
    }

    @BeforeEach
    public void initTest() {
        tradeJournalPost = createEntity(em);
    }

    @Test
    @Transactional
    void createTradeJournalPost() throws Exception {
        int databaseSizeBeforeCreate = tradeJournalPostRepository.findAll().size();
        // Create the TradeJournalPost
        restTradeJournalPostMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeJournalPost))
            )
            .andExpect(status().isCreated());

        // Validate the TradeJournalPost in the database
        List<TradeJournalPost> tradeJournalPostList = tradeJournalPostRepository.findAll();
        assertThat(tradeJournalPostList).hasSize(databaseSizeBeforeCreate + 1);
        TradeJournalPost testTradeJournalPost = tradeJournalPostList.get(tradeJournalPostList.size() - 1);
        assertThat(testTradeJournalPost.getPostTitle()).isEqualTo(DEFAULT_POST_TITLE);
        assertThat(testTradeJournalPost.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testTradeJournalPost.getThoughtsOnPsychology()).isEqualTo(DEFAULT_THOUGHTS_ON_PSYCHOLOGY);
        assertThat(testTradeJournalPost.getThoughtsOnTradeProcessAccuracy()).isEqualTo(DEFAULT_THOUGHTS_ON_TRADE_PROCESS_ACCURACY);
        assertThat(testTradeJournalPost.getThoughtsOnAreasOfStrength()).isEqualTo(DEFAULT_THOUGHTS_ON_AREAS_OF_STRENGTH);
        assertThat(testTradeJournalPost.getThoughtsOnAreasForImprovement()).isEqualTo(DEFAULT_THOUGHTS_ON_AREAS_FOR_IMPROVEMENT);
        assertThat(testTradeJournalPost.getAreaOfFocusForTomorrow()).isEqualTo(DEFAULT_AREA_OF_FOCUS_FOR_TOMORROW);
        assertThat(testTradeJournalPost.getMakePublicVisibleOnSite()).isEqualTo(DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE);
        assertThat(testTradeJournalPost.getAnyMedia()).isEqualTo(DEFAULT_ANY_MEDIA);
        assertThat(testTradeJournalPost.getAnyMediaContentType()).isEqualTo(DEFAULT_ANY_MEDIA_CONTENT_TYPE);
        assertThat(testTradeJournalPost.getAnyImage()).isEqualTo(DEFAULT_ANY_IMAGE);
        assertThat(testTradeJournalPost.getAnyImageContentType()).isEqualTo(DEFAULT_ANY_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createTradeJournalPostWithExistingId() throws Exception {
        // Create the TradeJournalPost with an existing ID
        tradeJournalPost.setId(1L);

        int databaseSizeBeforeCreate = tradeJournalPostRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTradeJournalPostMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeJournalPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeJournalPost in the database
        List<TradeJournalPost> tradeJournalPostList = tradeJournalPostRepository.findAll();
        assertThat(tradeJournalPostList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPostTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = tradeJournalPostRepository.findAll().size();
        // set the field null
        tradeJournalPost.setPostTitle(null);

        // Create the TradeJournalPost, which fails.

        restTradeJournalPostMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeJournalPost))
            )
            .andExpect(status().isBadRequest());

        List<TradeJournalPost> tradeJournalPostList = tradeJournalPostRepository.findAll();
        assertThat(tradeJournalPostList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateAddedIsRequired() throws Exception {
        int databaseSizeBeforeTest = tradeJournalPostRepository.findAll().size();
        // set the field null
        tradeJournalPost.setDateAdded(null);

        // Create the TradeJournalPost, which fails.

        restTradeJournalPostMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeJournalPost))
            )
            .andExpect(status().isBadRequest());

        List<TradeJournalPost> tradeJournalPostList = tradeJournalPostRepository.findAll();
        assertThat(tradeJournalPostList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTradeJournalPosts() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        // Get all the tradeJournalPostList
        restTradeJournalPostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tradeJournalPost.getId().intValue())))
            .andExpect(jsonPath("$.[*].postTitle").value(hasItem(DEFAULT_POST_TITLE)))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].thoughtsOnPsychology").value(hasItem(DEFAULT_THOUGHTS_ON_PSYCHOLOGY.toString())))
            .andExpect(
                jsonPath("$.[*].thoughtsOnTradeProcessAccuracy").value(hasItem(DEFAULT_THOUGHTS_ON_TRADE_PROCESS_ACCURACY.toString()))
            )
            .andExpect(jsonPath("$.[*].thoughtsOnAreasOfStrength").value(hasItem(DEFAULT_THOUGHTS_ON_AREAS_OF_STRENGTH.toString())))
            .andExpect(jsonPath("$.[*].thoughtsOnAreasForImprovement").value(hasItem(DEFAULT_THOUGHTS_ON_AREAS_FOR_IMPROVEMENT.toString())))
            .andExpect(jsonPath("$.[*].areaOfFocusForTomorrow").value(hasItem(DEFAULT_AREA_OF_FOCUS_FOR_TOMORROW.toString())))
            .andExpect(jsonPath("$.[*].makePublicVisibleOnSite").value(hasItem(DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE.booleanValue())))
            .andExpect(jsonPath("$.[*].anyMediaContentType").value(hasItem(DEFAULT_ANY_MEDIA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].anyMedia").value(hasItem(Base64Utils.encodeToString(DEFAULT_ANY_MEDIA))))
            .andExpect(jsonPath("$.[*].anyImageContentType").value(hasItem(DEFAULT_ANY_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].anyImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_ANY_IMAGE))));
    }

    @Test
    @Transactional
    void getTradeJournalPost() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        // Get the tradeJournalPost
        restTradeJournalPostMockMvc
            .perform(get(ENTITY_API_URL_ID, tradeJournalPost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tradeJournalPost.getId().intValue()))
            .andExpect(jsonPath("$.postTitle").value(DEFAULT_POST_TITLE))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.thoughtsOnPsychology").value(DEFAULT_THOUGHTS_ON_PSYCHOLOGY.toString()))
            .andExpect(jsonPath("$.thoughtsOnTradeProcessAccuracy").value(DEFAULT_THOUGHTS_ON_TRADE_PROCESS_ACCURACY.toString()))
            .andExpect(jsonPath("$.thoughtsOnAreasOfStrength").value(DEFAULT_THOUGHTS_ON_AREAS_OF_STRENGTH.toString()))
            .andExpect(jsonPath("$.thoughtsOnAreasForImprovement").value(DEFAULT_THOUGHTS_ON_AREAS_FOR_IMPROVEMENT.toString()))
            .andExpect(jsonPath("$.areaOfFocusForTomorrow").value(DEFAULT_AREA_OF_FOCUS_FOR_TOMORROW.toString()))
            .andExpect(jsonPath("$.makePublicVisibleOnSite").value(DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE.booleanValue()))
            .andExpect(jsonPath("$.anyMediaContentType").value(DEFAULT_ANY_MEDIA_CONTENT_TYPE))
            .andExpect(jsonPath("$.anyMedia").value(Base64Utils.encodeToString(DEFAULT_ANY_MEDIA)))
            .andExpect(jsonPath("$.anyImageContentType").value(DEFAULT_ANY_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.anyImage").value(Base64Utils.encodeToString(DEFAULT_ANY_IMAGE)));
    }

    @Test
    @Transactional
    void getTradeJournalPostsByIdFiltering() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        Long id = tradeJournalPost.getId();

        defaultTradeJournalPostShouldBeFound("id.equals=" + id);
        defaultTradeJournalPostShouldNotBeFound("id.notEquals=" + id);

        defaultTradeJournalPostShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTradeJournalPostShouldNotBeFound("id.greaterThan=" + id);

        defaultTradeJournalPostShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTradeJournalPostShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTradeJournalPostsByPostTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        // Get all the tradeJournalPostList where postTitle equals to DEFAULT_POST_TITLE
        defaultTradeJournalPostShouldBeFound("postTitle.equals=" + DEFAULT_POST_TITLE);

        // Get all the tradeJournalPostList where postTitle equals to UPDATED_POST_TITLE
        defaultTradeJournalPostShouldNotBeFound("postTitle.equals=" + UPDATED_POST_TITLE);
    }

    @Test
    @Transactional
    void getAllTradeJournalPostsByPostTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        // Get all the tradeJournalPostList where postTitle not equals to DEFAULT_POST_TITLE
        defaultTradeJournalPostShouldNotBeFound("postTitle.notEquals=" + DEFAULT_POST_TITLE);

        // Get all the tradeJournalPostList where postTitle not equals to UPDATED_POST_TITLE
        defaultTradeJournalPostShouldBeFound("postTitle.notEquals=" + UPDATED_POST_TITLE);
    }

    @Test
    @Transactional
    void getAllTradeJournalPostsByPostTitleIsInShouldWork() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        // Get all the tradeJournalPostList where postTitle in DEFAULT_POST_TITLE or UPDATED_POST_TITLE
        defaultTradeJournalPostShouldBeFound("postTitle.in=" + DEFAULT_POST_TITLE + "," + UPDATED_POST_TITLE);

        // Get all the tradeJournalPostList where postTitle equals to UPDATED_POST_TITLE
        defaultTradeJournalPostShouldNotBeFound("postTitle.in=" + UPDATED_POST_TITLE);
    }

    @Test
    @Transactional
    void getAllTradeJournalPostsByPostTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        // Get all the tradeJournalPostList where postTitle is not null
        defaultTradeJournalPostShouldBeFound("postTitle.specified=true");

        // Get all the tradeJournalPostList where postTitle is null
        defaultTradeJournalPostShouldNotBeFound("postTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllTradeJournalPostsByPostTitleContainsSomething() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        // Get all the tradeJournalPostList where postTitle contains DEFAULT_POST_TITLE
        defaultTradeJournalPostShouldBeFound("postTitle.contains=" + DEFAULT_POST_TITLE);

        // Get all the tradeJournalPostList where postTitle contains UPDATED_POST_TITLE
        defaultTradeJournalPostShouldNotBeFound("postTitle.contains=" + UPDATED_POST_TITLE);
    }

    @Test
    @Transactional
    void getAllTradeJournalPostsByPostTitleNotContainsSomething() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        // Get all the tradeJournalPostList where postTitle does not contain DEFAULT_POST_TITLE
        defaultTradeJournalPostShouldNotBeFound("postTitle.doesNotContain=" + DEFAULT_POST_TITLE);

        // Get all the tradeJournalPostList where postTitle does not contain UPDATED_POST_TITLE
        defaultTradeJournalPostShouldBeFound("postTitle.doesNotContain=" + UPDATED_POST_TITLE);
    }

    @Test
    @Transactional
    void getAllTradeJournalPostsByDateAddedIsEqualToSomething() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        // Get all the tradeJournalPostList where dateAdded equals to DEFAULT_DATE_ADDED
        defaultTradeJournalPostShouldBeFound("dateAdded.equals=" + DEFAULT_DATE_ADDED);

        // Get all the tradeJournalPostList where dateAdded equals to UPDATED_DATE_ADDED
        defaultTradeJournalPostShouldNotBeFound("dateAdded.equals=" + UPDATED_DATE_ADDED);
    }

    @Test
    @Transactional
    void getAllTradeJournalPostsByDateAddedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        // Get all the tradeJournalPostList where dateAdded not equals to DEFAULT_DATE_ADDED
        defaultTradeJournalPostShouldNotBeFound("dateAdded.notEquals=" + DEFAULT_DATE_ADDED);

        // Get all the tradeJournalPostList where dateAdded not equals to UPDATED_DATE_ADDED
        defaultTradeJournalPostShouldBeFound("dateAdded.notEquals=" + UPDATED_DATE_ADDED);
    }

    @Test
    @Transactional
    void getAllTradeJournalPostsByDateAddedIsInShouldWork() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        // Get all the tradeJournalPostList where dateAdded in DEFAULT_DATE_ADDED or UPDATED_DATE_ADDED
        defaultTradeJournalPostShouldBeFound("dateAdded.in=" + DEFAULT_DATE_ADDED + "," + UPDATED_DATE_ADDED);

        // Get all the tradeJournalPostList where dateAdded equals to UPDATED_DATE_ADDED
        defaultTradeJournalPostShouldNotBeFound("dateAdded.in=" + UPDATED_DATE_ADDED);
    }

    @Test
    @Transactional
    void getAllTradeJournalPostsByDateAddedIsNullOrNotNull() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        // Get all the tradeJournalPostList where dateAdded is not null
        defaultTradeJournalPostShouldBeFound("dateAdded.specified=true");

        // Get all the tradeJournalPostList where dateAdded is null
        defaultTradeJournalPostShouldNotBeFound("dateAdded.specified=false");
    }

    @Test
    @Transactional
    void getAllTradeJournalPostsByMakePublicVisibleOnSiteIsEqualToSomething() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        // Get all the tradeJournalPostList where makePublicVisibleOnSite equals to DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE
        defaultTradeJournalPostShouldBeFound("makePublicVisibleOnSite.equals=" + DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE);

        // Get all the tradeJournalPostList where makePublicVisibleOnSite equals to UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE
        defaultTradeJournalPostShouldNotBeFound("makePublicVisibleOnSite.equals=" + UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);
    }

    @Test
    @Transactional
    void getAllTradeJournalPostsByMakePublicVisibleOnSiteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        // Get all the tradeJournalPostList where makePublicVisibleOnSite not equals to DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE
        defaultTradeJournalPostShouldNotBeFound("makePublicVisibleOnSite.notEquals=" + DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE);

        // Get all the tradeJournalPostList where makePublicVisibleOnSite not equals to UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE
        defaultTradeJournalPostShouldBeFound("makePublicVisibleOnSite.notEquals=" + UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);
    }

    @Test
    @Transactional
    void getAllTradeJournalPostsByMakePublicVisibleOnSiteIsInShouldWork() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        // Get all the tradeJournalPostList where makePublicVisibleOnSite in DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE or UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE
        defaultTradeJournalPostShouldBeFound(
            "makePublicVisibleOnSite.in=" + DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE + "," + UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE
        );

        // Get all the tradeJournalPostList where makePublicVisibleOnSite equals to UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE
        defaultTradeJournalPostShouldNotBeFound("makePublicVisibleOnSite.in=" + UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);
    }

    @Test
    @Transactional
    void getAllTradeJournalPostsByMakePublicVisibleOnSiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        // Get all the tradeJournalPostList where makePublicVisibleOnSite is not null
        defaultTradeJournalPostShouldBeFound("makePublicVisibleOnSite.specified=true");

        // Get all the tradeJournalPostList where makePublicVisibleOnSite is null
        defaultTradeJournalPostShouldNotBeFound("makePublicVisibleOnSite.specified=false");
    }

    @Test
    @Transactional
    void getAllTradeJournalPostsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        tradeJournalPost.setUser(user);
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);
        Long userId = user.getId();

        // Get all the tradeJournalPostList where user equals to userId
        defaultTradeJournalPostShouldBeFound("userId.equals=" + userId);

        // Get all the tradeJournalPostList where user equals to (userId + 1)
        defaultTradeJournalPostShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllTradeJournalPostsByMt4TradeIsEqualToSomething() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);
        Mt4Trade mt4Trade = Mt4TradeResourceIT.createEntity(em);
        em.persist(mt4Trade);
        em.flush();
        tradeJournalPost.setMt4Trade(mt4Trade);
        mt4Trade.setTradeJournalPost(tradeJournalPost);
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);
        Long mt4TradeId = mt4Trade.getId();

        // Get all the tradeJournalPostList where mt4Trade equals to mt4TradeId
        defaultTradeJournalPostShouldBeFound("mt4TradeId.equals=" + mt4TradeId);

        // Get all the tradeJournalPostList where mt4Trade equals to (mt4TradeId + 1)
        defaultTradeJournalPostShouldNotBeFound("mt4TradeId.equals=" + (mt4TradeId + 1));
    }

    @Test
    @Transactional
    void getAllTradeJournalPostsByUserCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);
        UserComment userComment = UserCommentResourceIT.createEntity(em);
        em.persist(userComment);
        em.flush();
        tradeJournalPost.addUserComment(userComment);
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);
        Long userCommentId = userComment.getId();

        // Get all the tradeJournalPostList where userComment equals to userCommentId
        defaultTradeJournalPostShouldBeFound("userCommentId.equals=" + userCommentId);

        // Get all the tradeJournalPostList where userComment equals to (userCommentId + 1)
        defaultTradeJournalPostShouldNotBeFound("userCommentId.equals=" + (userCommentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTradeJournalPostShouldBeFound(String filter) throws Exception {
        restTradeJournalPostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tradeJournalPost.getId().intValue())))
            .andExpect(jsonPath("$.[*].postTitle").value(hasItem(DEFAULT_POST_TITLE)))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].thoughtsOnPsychology").value(hasItem(DEFAULT_THOUGHTS_ON_PSYCHOLOGY.toString())))
            .andExpect(
                jsonPath("$.[*].thoughtsOnTradeProcessAccuracy").value(hasItem(DEFAULT_THOUGHTS_ON_TRADE_PROCESS_ACCURACY.toString()))
            )
            .andExpect(jsonPath("$.[*].thoughtsOnAreasOfStrength").value(hasItem(DEFAULT_THOUGHTS_ON_AREAS_OF_STRENGTH.toString())))
            .andExpect(jsonPath("$.[*].thoughtsOnAreasForImprovement").value(hasItem(DEFAULT_THOUGHTS_ON_AREAS_FOR_IMPROVEMENT.toString())))
            .andExpect(jsonPath("$.[*].areaOfFocusForTomorrow").value(hasItem(DEFAULT_AREA_OF_FOCUS_FOR_TOMORROW.toString())))
            .andExpect(jsonPath("$.[*].makePublicVisibleOnSite").value(hasItem(DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE.booleanValue())))
            .andExpect(jsonPath("$.[*].anyMediaContentType").value(hasItem(DEFAULT_ANY_MEDIA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].anyMedia").value(hasItem(Base64Utils.encodeToString(DEFAULT_ANY_MEDIA))))
            .andExpect(jsonPath("$.[*].anyImageContentType").value(hasItem(DEFAULT_ANY_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].anyImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_ANY_IMAGE))));

        // Check, that the count call also returns 1
        restTradeJournalPostMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTradeJournalPostShouldNotBeFound(String filter) throws Exception {
        restTradeJournalPostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTradeJournalPostMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTradeJournalPost() throws Exception {
        // Get the tradeJournalPost
        restTradeJournalPostMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTradeJournalPost() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        int databaseSizeBeforeUpdate = tradeJournalPostRepository.findAll().size();

        // Update the tradeJournalPost
        TradeJournalPost updatedTradeJournalPost = tradeJournalPostRepository.findById(tradeJournalPost.getId()).get();
        // Disconnect from session so that the updates on updatedTradeJournalPost are not directly saved in db
        em.detach(updatedTradeJournalPost);
        updatedTradeJournalPost
            .postTitle(UPDATED_POST_TITLE)
            .dateAdded(UPDATED_DATE_ADDED)
            .thoughtsOnPsychology(UPDATED_THOUGHTS_ON_PSYCHOLOGY)
            .thoughtsOnTradeProcessAccuracy(UPDATED_THOUGHTS_ON_TRADE_PROCESS_ACCURACY)
            .thoughtsOnAreasOfStrength(UPDATED_THOUGHTS_ON_AREAS_OF_STRENGTH)
            .thoughtsOnAreasForImprovement(UPDATED_THOUGHTS_ON_AREAS_FOR_IMPROVEMENT)
            .areaOfFocusForTomorrow(UPDATED_AREA_OF_FOCUS_FOR_TOMORROW)
            .makePublicVisibleOnSite(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE)
            .anyMedia(UPDATED_ANY_MEDIA)
            .anyMediaContentType(UPDATED_ANY_MEDIA_CONTENT_TYPE)
            .anyImage(UPDATED_ANY_IMAGE)
            .anyImageContentType(UPDATED_ANY_IMAGE_CONTENT_TYPE);

        restTradeJournalPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTradeJournalPost.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTradeJournalPost))
            )
            .andExpect(status().isOk());

        // Validate the TradeJournalPost in the database
        List<TradeJournalPost> tradeJournalPostList = tradeJournalPostRepository.findAll();
        assertThat(tradeJournalPostList).hasSize(databaseSizeBeforeUpdate);
        TradeJournalPost testTradeJournalPost = tradeJournalPostList.get(tradeJournalPostList.size() - 1);
        assertThat(testTradeJournalPost.getPostTitle()).isEqualTo(UPDATED_POST_TITLE);
        assertThat(testTradeJournalPost.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testTradeJournalPost.getThoughtsOnPsychology()).isEqualTo(UPDATED_THOUGHTS_ON_PSYCHOLOGY);
        assertThat(testTradeJournalPost.getThoughtsOnTradeProcessAccuracy()).isEqualTo(UPDATED_THOUGHTS_ON_TRADE_PROCESS_ACCURACY);
        assertThat(testTradeJournalPost.getThoughtsOnAreasOfStrength()).isEqualTo(UPDATED_THOUGHTS_ON_AREAS_OF_STRENGTH);
        assertThat(testTradeJournalPost.getThoughtsOnAreasForImprovement()).isEqualTo(UPDATED_THOUGHTS_ON_AREAS_FOR_IMPROVEMENT);
        assertThat(testTradeJournalPost.getAreaOfFocusForTomorrow()).isEqualTo(UPDATED_AREA_OF_FOCUS_FOR_TOMORROW);
        assertThat(testTradeJournalPost.getMakePublicVisibleOnSite()).isEqualTo(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);
        assertThat(testTradeJournalPost.getAnyMedia()).isEqualTo(UPDATED_ANY_MEDIA);
        assertThat(testTradeJournalPost.getAnyMediaContentType()).isEqualTo(UPDATED_ANY_MEDIA_CONTENT_TYPE);
        assertThat(testTradeJournalPost.getAnyImage()).isEqualTo(UPDATED_ANY_IMAGE);
        assertThat(testTradeJournalPost.getAnyImageContentType()).isEqualTo(UPDATED_ANY_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingTradeJournalPost() throws Exception {
        int databaseSizeBeforeUpdate = tradeJournalPostRepository.findAll().size();
        tradeJournalPost.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTradeJournalPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tradeJournalPost.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tradeJournalPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeJournalPost in the database
        List<TradeJournalPost> tradeJournalPostList = tradeJournalPostRepository.findAll();
        assertThat(tradeJournalPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTradeJournalPost() throws Exception {
        int databaseSizeBeforeUpdate = tradeJournalPostRepository.findAll().size();
        tradeJournalPost.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTradeJournalPostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tradeJournalPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeJournalPost in the database
        List<TradeJournalPost> tradeJournalPostList = tradeJournalPostRepository.findAll();
        assertThat(tradeJournalPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTradeJournalPost() throws Exception {
        int databaseSizeBeforeUpdate = tradeJournalPostRepository.findAll().size();
        tradeJournalPost.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTradeJournalPostMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeJournalPost))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TradeJournalPost in the database
        List<TradeJournalPost> tradeJournalPostList = tradeJournalPostRepository.findAll();
        assertThat(tradeJournalPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTradeJournalPostWithPatch() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        int databaseSizeBeforeUpdate = tradeJournalPostRepository.findAll().size();

        // Update the tradeJournalPost using partial update
        TradeJournalPost partialUpdatedTradeJournalPost = new TradeJournalPost();
        partialUpdatedTradeJournalPost.setId(tradeJournalPost.getId());

        partialUpdatedTradeJournalPost
            .postTitle(UPDATED_POST_TITLE)
            .thoughtsOnPsychology(UPDATED_THOUGHTS_ON_PSYCHOLOGY)
            .thoughtsOnTradeProcessAccuracy(UPDATED_THOUGHTS_ON_TRADE_PROCESS_ACCURACY)
            .thoughtsOnAreasForImprovement(UPDATED_THOUGHTS_ON_AREAS_FOR_IMPROVEMENT)
            .makePublicVisibleOnSite(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);

        restTradeJournalPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTradeJournalPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTradeJournalPost))
            )
            .andExpect(status().isOk());

        // Validate the TradeJournalPost in the database
        List<TradeJournalPost> tradeJournalPostList = tradeJournalPostRepository.findAll();
        assertThat(tradeJournalPostList).hasSize(databaseSizeBeforeUpdate);
        TradeJournalPost testTradeJournalPost = tradeJournalPostList.get(tradeJournalPostList.size() - 1);
        assertThat(testTradeJournalPost.getPostTitle()).isEqualTo(UPDATED_POST_TITLE);
        assertThat(testTradeJournalPost.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testTradeJournalPost.getThoughtsOnPsychology()).isEqualTo(UPDATED_THOUGHTS_ON_PSYCHOLOGY);
        assertThat(testTradeJournalPost.getThoughtsOnTradeProcessAccuracy()).isEqualTo(UPDATED_THOUGHTS_ON_TRADE_PROCESS_ACCURACY);
        assertThat(testTradeJournalPost.getThoughtsOnAreasOfStrength()).isEqualTo(DEFAULT_THOUGHTS_ON_AREAS_OF_STRENGTH);
        assertThat(testTradeJournalPost.getThoughtsOnAreasForImprovement()).isEqualTo(UPDATED_THOUGHTS_ON_AREAS_FOR_IMPROVEMENT);
        assertThat(testTradeJournalPost.getAreaOfFocusForTomorrow()).isEqualTo(DEFAULT_AREA_OF_FOCUS_FOR_TOMORROW);
        assertThat(testTradeJournalPost.getMakePublicVisibleOnSite()).isEqualTo(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);
        assertThat(testTradeJournalPost.getAnyMedia()).isEqualTo(DEFAULT_ANY_MEDIA);
        assertThat(testTradeJournalPost.getAnyMediaContentType()).isEqualTo(DEFAULT_ANY_MEDIA_CONTENT_TYPE);
        assertThat(testTradeJournalPost.getAnyImage()).isEqualTo(DEFAULT_ANY_IMAGE);
        assertThat(testTradeJournalPost.getAnyImageContentType()).isEqualTo(DEFAULT_ANY_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateTradeJournalPostWithPatch() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        int databaseSizeBeforeUpdate = tradeJournalPostRepository.findAll().size();

        // Update the tradeJournalPost using partial update
        TradeJournalPost partialUpdatedTradeJournalPost = new TradeJournalPost();
        partialUpdatedTradeJournalPost.setId(tradeJournalPost.getId());

        partialUpdatedTradeJournalPost
            .postTitle(UPDATED_POST_TITLE)
            .dateAdded(UPDATED_DATE_ADDED)
            .thoughtsOnPsychology(UPDATED_THOUGHTS_ON_PSYCHOLOGY)
            .thoughtsOnTradeProcessAccuracy(UPDATED_THOUGHTS_ON_TRADE_PROCESS_ACCURACY)
            .thoughtsOnAreasOfStrength(UPDATED_THOUGHTS_ON_AREAS_OF_STRENGTH)
            .thoughtsOnAreasForImprovement(UPDATED_THOUGHTS_ON_AREAS_FOR_IMPROVEMENT)
            .areaOfFocusForTomorrow(UPDATED_AREA_OF_FOCUS_FOR_TOMORROW)
            .makePublicVisibleOnSite(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE)
            .anyMedia(UPDATED_ANY_MEDIA)
            .anyMediaContentType(UPDATED_ANY_MEDIA_CONTENT_TYPE)
            .anyImage(UPDATED_ANY_IMAGE)
            .anyImageContentType(UPDATED_ANY_IMAGE_CONTENT_TYPE);

        restTradeJournalPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTradeJournalPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTradeJournalPost))
            )
            .andExpect(status().isOk());

        // Validate the TradeJournalPost in the database
        List<TradeJournalPost> tradeJournalPostList = tradeJournalPostRepository.findAll();
        assertThat(tradeJournalPostList).hasSize(databaseSizeBeforeUpdate);
        TradeJournalPost testTradeJournalPost = tradeJournalPostList.get(tradeJournalPostList.size() - 1);
        assertThat(testTradeJournalPost.getPostTitle()).isEqualTo(UPDATED_POST_TITLE);
        assertThat(testTradeJournalPost.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testTradeJournalPost.getThoughtsOnPsychology()).isEqualTo(UPDATED_THOUGHTS_ON_PSYCHOLOGY);
        assertThat(testTradeJournalPost.getThoughtsOnTradeProcessAccuracy()).isEqualTo(UPDATED_THOUGHTS_ON_TRADE_PROCESS_ACCURACY);
        assertThat(testTradeJournalPost.getThoughtsOnAreasOfStrength()).isEqualTo(UPDATED_THOUGHTS_ON_AREAS_OF_STRENGTH);
        assertThat(testTradeJournalPost.getThoughtsOnAreasForImprovement()).isEqualTo(UPDATED_THOUGHTS_ON_AREAS_FOR_IMPROVEMENT);
        assertThat(testTradeJournalPost.getAreaOfFocusForTomorrow()).isEqualTo(UPDATED_AREA_OF_FOCUS_FOR_TOMORROW);
        assertThat(testTradeJournalPost.getMakePublicVisibleOnSite()).isEqualTo(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);
        assertThat(testTradeJournalPost.getAnyMedia()).isEqualTo(UPDATED_ANY_MEDIA);
        assertThat(testTradeJournalPost.getAnyMediaContentType()).isEqualTo(UPDATED_ANY_MEDIA_CONTENT_TYPE);
        assertThat(testTradeJournalPost.getAnyImage()).isEqualTo(UPDATED_ANY_IMAGE);
        assertThat(testTradeJournalPost.getAnyImageContentType()).isEqualTo(UPDATED_ANY_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingTradeJournalPost() throws Exception {
        int databaseSizeBeforeUpdate = tradeJournalPostRepository.findAll().size();
        tradeJournalPost.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTradeJournalPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tradeJournalPost.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tradeJournalPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeJournalPost in the database
        List<TradeJournalPost> tradeJournalPostList = tradeJournalPostRepository.findAll();
        assertThat(tradeJournalPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTradeJournalPost() throws Exception {
        int databaseSizeBeforeUpdate = tradeJournalPostRepository.findAll().size();
        tradeJournalPost.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTradeJournalPostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tradeJournalPost))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeJournalPost in the database
        List<TradeJournalPost> tradeJournalPostList = tradeJournalPostRepository.findAll();
        assertThat(tradeJournalPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTradeJournalPost() throws Exception {
        int databaseSizeBeforeUpdate = tradeJournalPostRepository.findAll().size();
        tradeJournalPost.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTradeJournalPostMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tradeJournalPost))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TradeJournalPost in the database
        List<TradeJournalPost> tradeJournalPostList = tradeJournalPostRepository.findAll();
        assertThat(tradeJournalPostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTradeJournalPost() throws Exception {
        // Initialize the database
        tradeJournalPostRepository.saveAndFlush(tradeJournalPost);

        int databaseSizeBeforeDelete = tradeJournalPostRepository.findAll().size();

        // Delete the tradeJournalPost
        restTradeJournalPostMockMvc
            .perform(delete(ENTITY_API_URL_ID, tradeJournalPost.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TradeJournalPost> tradeJournalPostList = tradeJournalPostRepository.findAll();
        assertThat(tradeJournalPostList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
