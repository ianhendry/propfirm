package com.gracefl.propfirm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gracefl.propfirm.IntegrationTest;
import com.gracefl.propfirm.domain.UserComment;
import com.gracefl.propfirm.repository.UserCommentRepository;
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
 * Integration tests for the {@link UserCommentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserCommentResourceIT {

    private static final String DEFAULT_COMMENT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT_BODY = "BBBBBBBBBB";

    private static final byte[] DEFAULT_COMMENT_MEDIA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_COMMENT_MEDIA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_COMMENT_MEDIA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_COMMENT_MEDIA_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_DATE_ADDED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ADDED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE = false;
    private static final Boolean UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE = true;

    private static final String ENTITY_API_URL = "/api/user-comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserCommentRepository userCommentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserCommentMockMvc;

    private UserComment userComment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserComment createEntity(EntityManager em) {
        UserComment userComment = new UserComment()
            .commentTitle(DEFAULT_COMMENT_TITLE)
            .commentBody(DEFAULT_COMMENT_BODY)
            .commentMedia(DEFAULT_COMMENT_MEDIA)
            .commentMediaContentType(DEFAULT_COMMENT_MEDIA_CONTENT_TYPE)
            .dateAdded(DEFAULT_DATE_ADDED)
            .makePublicVisibleOnSite(DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE);
        return userComment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserComment createUpdatedEntity(EntityManager em) {
        UserComment userComment = new UserComment()
            .commentTitle(UPDATED_COMMENT_TITLE)
            .commentBody(UPDATED_COMMENT_BODY)
            .commentMedia(UPDATED_COMMENT_MEDIA)
            .commentMediaContentType(UPDATED_COMMENT_MEDIA_CONTENT_TYPE)
            .dateAdded(UPDATED_DATE_ADDED)
            .makePublicVisibleOnSite(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);
        return userComment;
    }

    @BeforeEach
    public void initTest() {
        userComment = createEntity(em);
    }

    @Test
    @Transactional
    void createUserComment() throws Exception {
        int databaseSizeBeforeCreate = userCommentRepository.findAll().size();
        // Create the UserComment
        restUserCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userComment)))
            .andExpect(status().isCreated());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeCreate + 1);
        UserComment testUserComment = userCommentList.get(userCommentList.size() - 1);
        assertThat(testUserComment.getCommentTitle()).isEqualTo(DEFAULT_COMMENT_TITLE);
        assertThat(testUserComment.getCommentBody()).isEqualTo(DEFAULT_COMMENT_BODY);
        assertThat(testUserComment.getCommentMedia()).isEqualTo(DEFAULT_COMMENT_MEDIA);
        assertThat(testUserComment.getCommentMediaContentType()).isEqualTo(DEFAULT_COMMENT_MEDIA_CONTENT_TYPE);
        assertThat(testUserComment.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testUserComment.getMakePublicVisibleOnSite()).isEqualTo(DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE);
    }

    @Test
    @Transactional
    void createUserCommentWithExistingId() throws Exception {
        // Create the UserComment with an existing ID
        userComment.setId(1L);

        int databaseSizeBeforeCreate = userCommentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userComment)))
            .andExpect(status().isBadRequest());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCommentTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCommentRepository.findAll().size();
        // set the field null
        userComment.setCommentTitle(null);

        // Create the UserComment, which fails.

        restUserCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userComment)))
            .andExpect(status().isBadRequest());

        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateAddedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCommentRepository.findAll().size();
        // set the field null
        userComment.setDateAdded(null);

        // Create the UserComment, which fails.

        restUserCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userComment)))
            .andExpect(status().isBadRequest());

        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserComments() throws Exception {
        // Initialize the database
        userCommentRepository.saveAndFlush(userComment);

        // Get all the userCommentList
        restUserCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].commentTitle").value(hasItem(DEFAULT_COMMENT_TITLE)))
            .andExpect(jsonPath("$.[*].commentBody").value(hasItem(DEFAULT_COMMENT_BODY.toString())))
            .andExpect(jsonPath("$.[*].commentMediaContentType").value(hasItem(DEFAULT_COMMENT_MEDIA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].commentMedia").value(hasItem(Base64Utils.encodeToString(DEFAULT_COMMENT_MEDIA))))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].makePublicVisibleOnSite").value(hasItem(DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE.booleanValue())));
    }

    @Test
    @Transactional
    void getUserComment() throws Exception {
        // Initialize the database
        userCommentRepository.saveAndFlush(userComment);

        // Get the userComment
        restUserCommentMockMvc
            .perform(get(ENTITY_API_URL_ID, userComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userComment.getId().intValue()))
            .andExpect(jsonPath("$.commentTitle").value(DEFAULT_COMMENT_TITLE))
            .andExpect(jsonPath("$.commentBody").value(DEFAULT_COMMENT_BODY.toString()))
            .andExpect(jsonPath("$.commentMediaContentType").value(DEFAULT_COMMENT_MEDIA_CONTENT_TYPE))
            .andExpect(jsonPath("$.commentMedia").value(Base64Utils.encodeToString(DEFAULT_COMMENT_MEDIA)))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.makePublicVisibleOnSite").value(DEFAULT_MAKE_PUBLIC_VISIBLE_ON_SITE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserComment() throws Exception {
        // Get the userComment
        restUserCommentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserComment() throws Exception {
        // Initialize the database
        userCommentRepository.saveAndFlush(userComment);

        int databaseSizeBeforeUpdate = userCommentRepository.findAll().size();

        // Update the userComment
        UserComment updatedUserComment = userCommentRepository.findById(userComment.getId()).get();
        // Disconnect from session so that the updates on updatedUserComment are not directly saved in db
        em.detach(updatedUserComment);
        updatedUserComment
            .commentTitle(UPDATED_COMMENT_TITLE)
            .commentBody(UPDATED_COMMENT_BODY)
            .commentMedia(UPDATED_COMMENT_MEDIA)
            .commentMediaContentType(UPDATED_COMMENT_MEDIA_CONTENT_TYPE)
            .dateAdded(UPDATED_DATE_ADDED)
            .makePublicVisibleOnSite(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);

        restUserCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserComment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserComment))
            )
            .andExpect(status().isOk());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
        UserComment testUserComment = userCommentList.get(userCommentList.size() - 1);
        assertThat(testUserComment.getCommentTitle()).isEqualTo(UPDATED_COMMENT_TITLE);
        assertThat(testUserComment.getCommentBody()).isEqualTo(UPDATED_COMMENT_BODY);
        assertThat(testUserComment.getCommentMedia()).isEqualTo(UPDATED_COMMENT_MEDIA);
        assertThat(testUserComment.getCommentMediaContentType()).isEqualTo(UPDATED_COMMENT_MEDIA_CONTENT_TYPE);
        assertThat(testUserComment.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testUserComment.getMakePublicVisibleOnSite()).isEqualTo(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);
    }

    @Test
    @Transactional
    void putNonExistingUserComment() throws Exception {
        int databaseSizeBeforeUpdate = userCommentRepository.findAll().size();
        userComment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userComment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userComment))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserComment() throws Exception {
        int databaseSizeBeforeUpdate = userCommentRepository.findAll().size();
        userComment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userComment))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserComment() throws Exception {
        int databaseSizeBeforeUpdate = userCommentRepository.findAll().size();
        userComment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCommentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userComment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserCommentWithPatch() throws Exception {
        // Initialize the database
        userCommentRepository.saveAndFlush(userComment);

        int databaseSizeBeforeUpdate = userCommentRepository.findAll().size();

        // Update the userComment using partial update
        UserComment partialUpdatedUserComment = new UserComment();
        partialUpdatedUserComment.setId(userComment.getId());

        partialUpdatedUserComment.makePublicVisibleOnSite(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);

        restUserCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserComment))
            )
            .andExpect(status().isOk());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
        UserComment testUserComment = userCommentList.get(userCommentList.size() - 1);
        assertThat(testUserComment.getCommentTitle()).isEqualTo(DEFAULT_COMMENT_TITLE);
        assertThat(testUserComment.getCommentBody()).isEqualTo(DEFAULT_COMMENT_BODY);
        assertThat(testUserComment.getCommentMedia()).isEqualTo(DEFAULT_COMMENT_MEDIA);
        assertThat(testUserComment.getCommentMediaContentType()).isEqualTo(DEFAULT_COMMENT_MEDIA_CONTENT_TYPE);
        assertThat(testUserComment.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testUserComment.getMakePublicVisibleOnSite()).isEqualTo(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);
    }

    @Test
    @Transactional
    void fullUpdateUserCommentWithPatch() throws Exception {
        // Initialize the database
        userCommentRepository.saveAndFlush(userComment);

        int databaseSizeBeforeUpdate = userCommentRepository.findAll().size();

        // Update the userComment using partial update
        UserComment partialUpdatedUserComment = new UserComment();
        partialUpdatedUserComment.setId(userComment.getId());

        partialUpdatedUserComment
            .commentTitle(UPDATED_COMMENT_TITLE)
            .commentBody(UPDATED_COMMENT_BODY)
            .commentMedia(UPDATED_COMMENT_MEDIA)
            .commentMediaContentType(UPDATED_COMMENT_MEDIA_CONTENT_TYPE)
            .dateAdded(UPDATED_DATE_ADDED)
            .makePublicVisibleOnSite(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);

        restUserCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserComment))
            )
            .andExpect(status().isOk());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
        UserComment testUserComment = userCommentList.get(userCommentList.size() - 1);
        assertThat(testUserComment.getCommentTitle()).isEqualTo(UPDATED_COMMENT_TITLE);
        assertThat(testUserComment.getCommentBody()).isEqualTo(UPDATED_COMMENT_BODY);
        assertThat(testUserComment.getCommentMedia()).isEqualTo(UPDATED_COMMENT_MEDIA);
        assertThat(testUserComment.getCommentMediaContentType()).isEqualTo(UPDATED_COMMENT_MEDIA_CONTENT_TYPE);
        assertThat(testUserComment.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testUserComment.getMakePublicVisibleOnSite()).isEqualTo(UPDATED_MAKE_PUBLIC_VISIBLE_ON_SITE);
    }

    @Test
    @Transactional
    void patchNonExistingUserComment() throws Exception {
        int databaseSizeBeforeUpdate = userCommentRepository.findAll().size();
        userComment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userComment))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserComment() throws Exception {
        int databaseSizeBeforeUpdate = userCommentRepository.findAll().size();
        userComment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userComment))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserComment() throws Exception {
        int databaseSizeBeforeUpdate = userCommentRepository.findAll().size();
        userComment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCommentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userComment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserComment() throws Exception {
        // Initialize the database
        userCommentRepository.saveAndFlush(userComment);

        int databaseSizeBeforeDelete = userCommentRepository.findAll().size();

        // Delete the userComment
        restUserCommentMockMvc
            .perform(delete(ENTITY_API_URL_ID, userComment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
