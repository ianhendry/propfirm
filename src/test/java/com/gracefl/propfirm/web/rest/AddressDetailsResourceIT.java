package com.gracefl.propfirm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gracefl.propfirm.IntegrationTest;
import com.gracefl.propfirm.domain.AddressDetails;
import com.gracefl.propfirm.repository.AddressDetailsRepository;
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
 * Integration tests for the {@link AddressDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AddressDetailsResourceIT {

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_3 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_3 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_4 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_4 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_5 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_5 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_6 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_6 = "BBBBBBBBBB";

    private static final String DEFAULT_DIAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DIAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_MESSENGER_ID = "AAAAAAAAAA";
    private static final String UPDATED_MESSENGER_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_ADDED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ADDED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IN_ACTIVE = false;
    private static final Boolean UPDATED_IN_ACTIVE = true;

    private static final Instant DEFAULT_IN_ACTIVE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_IN_ACTIVE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/address-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AddressDetailsRepository addressDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAddressDetailsMockMvc;

    private AddressDetails addressDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AddressDetails createEntity(EntityManager em) {
        AddressDetails addressDetails = new AddressDetails()
            .contactName(DEFAULT_CONTACT_NAME)
            .address1(DEFAULT_ADDRESS_1)
            .address2(DEFAULT_ADDRESS_2)
            .address3(DEFAULT_ADDRESS_3)
            .address4(DEFAULT_ADDRESS_4)
            .address5(DEFAULT_ADDRESS_5)
            .address6(DEFAULT_ADDRESS_6)
            .dialCode(DEFAULT_DIAL_CODE)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .messengerId(DEFAULT_MESSENGER_ID)
            .dateAdded(DEFAULT_DATE_ADDED)
            .inActive(DEFAULT_IN_ACTIVE)
            .inActiveDate(DEFAULT_IN_ACTIVE_DATE);
        return addressDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AddressDetails createUpdatedEntity(EntityManager em) {
        AddressDetails addressDetails = new AddressDetails()
            .contactName(UPDATED_CONTACT_NAME)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .address3(UPDATED_ADDRESS_3)
            .address4(UPDATED_ADDRESS_4)
            .address5(UPDATED_ADDRESS_5)
            .address6(UPDATED_ADDRESS_6)
            .dialCode(UPDATED_DIAL_CODE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .messengerId(UPDATED_MESSENGER_ID)
            .dateAdded(UPDATED_DATE_ADDED)
            .inActive(UPDATED_IN_ACTIVE)
            .inActiveDate(UPDATED_IN_ACTIVE_DATE);
        return addressDetails;
    }

    @BeforeEach
    public void initTest() {
        addressDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createAddressDetails() throws Exception {
        int databaseSizeBeforeCreate = addressDetailsRepository.findAll().size();
        // Create the AddressDetails
        restAddressDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDetails))
            )
            .andExpect(status().isCreated());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        AddressDetails testAddressDetails = addressDetailsList.get(addressDetailsList.size() - 1);
        assertThat(testAddressDetails.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testAddressDetails.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testAddressDetails.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testAddressDetails.getAddress3()).isEqualTo(DEFAULT_ADDRESS_3);
        assertThat(testAddressDetails.getAddress4()).isEqualTo(DEFAULT_ADDRESS_4);
        assertThat(testAddressDetails.getAddress5()).isEqualTo(DEFAULT_ADDRESS_5);
        assertThat(testAddressDetails.getAddress6()).isEqualTo(DEFAULT_ADDRESS_6);
        assertThat(testAddressDetails.getDialCode()).isEqualTo(DEFAULT_DIAL_CODE);
        assertThat(testAddressDetails.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testAddressDetails.getMessengerId()).isEqualTo(DEFAULT_MESSENGER_ID);
        assertThat(testAddressDetails.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testAddressDetails.getInActive()).isEqualTo(DEFAULT_IN_ACTIVE);
        assertThat(testAddressDetails.getInActiveDate()).isEqualTo(DEFAULT_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void createAddressDetailsWithExistingId() throws Exception {
        // Create the AddressDetails with an existing ID
        addressDetails.setId(1L);

        int databaseSizeBeforeCreate = addressDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAddressDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAddressDetails() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get all the addressDetailsList
        restAddressDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(addressDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactName").value(hasItem(DEFAULT_CONTACT_NAME)))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1)))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2)))
            .andExpect(jsonPath("$.[*].address3").value(hasItem(DEFAULT_ADDRESS_3)))
            .andExpect(jsonPath("$.[*].address4").value(hasItem(DEFAULT_ADDRESS_4)))
            .andExpect(jsonPath("$.[*].address5").value(hasItem(DEFAULT_ADDRESS_5)))
            .andExpect(jsonPath("$.[*].address6").value(hasItem(DEFAULT_ADDRESS_6)))
            .andExpect(jsonPath("$.[*].dialCode").value(hasItem(DEFAULT_DIAL_CODE)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].messengerId").value(hasItem(DEFAULT_MESSENGER_ID)))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())))
            .andExpect(jsonPath("$.[*].inActive").value(hasItem(DEFAULT_IN_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].inActiveDate").value(hasItem(DEFAULT_IN_ACTIVE_DATE.toString())));
    }

    @Test
    @Transactional
    void getAddressDetails() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        // Get the addressDetails
        restAddressDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, addressDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(addressDetails.getId().intValue()))
            .andExpect(jsonPath("$.contactName").value(DEFAULT_CONTACT_NAME))
            .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS_1))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2))
            .andExpect(jsonPath("$.address3").value(DEFAULT_ADDRESS_3))
            .andExpect(jsonPath("$.address4").value(DEFAULT_ADDRESS_4))
            .andExpect(jsonPath("$.address5").value(DEFAULT_ADDRESS_5))
            .andExpect(jsonPath("$.address6").value(DEFAULT_ADDRESS_6))
            .andExpect(jsonPath("$.dialCode").value(DEFAULT_DIAL_CODE))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.messengerId").value(DEFAULT_MESSENGER_ID))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()))
            .andExpect(jsonPath("$.inActive").value(DEFAULT_IN_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.inActiveDate").value(DEFAULT_IN_ACTIVE_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAddressDetails() throws Exception {
        // Get the addressDetails
        restAddressDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAddressDetails() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        int databaseSizeBeforeUpdate = addressDetailsRepository.findAll().size();

        // Update the addressDetails
        AddressDetails updatedAddressDetails = addressDetailsRepository.findById(addressDetails.getId()).get();
        // Disconnect from session so that the updates on updatedAddressDetails are not directly saved in db
        em.detach(updatedAddressDetails);
        updatedAddressDetails
            .contactName(UPDATED_CONTACT_NAME)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .address3(UPDATED_ADDRESS_3)
            .address4(UPDATED_ADDRESS_4)
            .address5(UPDATED_ADDRESS_5)
            .address6(UPDATED_ADDRESS_6)
            .dialCode(UPDATED_DIAL_CODE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .messengerId(UPDATED_MESSENGER_ID)
            .dateAdded(UPDATED_DATE_ADDED)
            .inActive(UPDATED_IN_ACTIVE)
            .inActiveDate(UPDATED_IN_ACTIVE_DATE);

        restAddressDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAddressDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAddressDetails))
            )
            .andExpect(status().isOk());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeUpdate);
        AddressDetails testAddressDetails = addressDetailsList.get(addressDetailsList.size() - 1);
        assertThat(testAddressDetails.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testAddressDetails.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testAddressDetails.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testAddressDetails.getAddress3()).isEqualTo(UPDATED_ADDRESS_3);
        assertThat(testAddressDetails.getAddress4()).isEqualTo(UPDATED_ADDRESS_4);
        assertThat(testAddressDetails.getAddress5()).isEqualTo(UPDATED_ADDRESS_5);
        assertThat(testAddressDetails.getAddress6()).isEqualTo(UPDATED_ADDRESS_6);
        assertThat(testAddressDetails.getDialCode()).isEqualTo(UPDATED_DIAL_CODE);
        assertThat(testAddressDetails.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testAddressDetails.getMessengerId()).isEqualTo(UPDATED_MESSENGER_ID);
        assertThat(testAddressDetails.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testAddressDetails.getInActive()).isEqualTo(UPDATED_IN_ACTIVE);
        assertThat(testAddressDetails.getInActiveDate()).isEqualTo(UPDATED_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void putNonExistingAddressDetails() throws Exception {
        int databaseSizeBeforeUpdate = addressDetailsRepository.findAll().size();
        addressDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, addressDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAddressDetails() throws Exception {
        int databaseSizeBeforeUpdate = addressDetailsRepository.findAll().size();
        addressDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAddressDetails() throws Exception {
        int databaseSizeBeforeUpdate = addressDetailsRepository.findAll().size();
        addressDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAddressDetailsWithPatch() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        int databaseSizeBeforeUpdate = addressDetailsRepository.findAll().size();

        // Update the addressDetails using partial update
        AddressDetails partialUpdatedAddressDetails = new AddressDetails();
        partialUpdatedAddressDetails.setId(addressDetails.getId());

        partialUpdatedAddressDetails
            .address3(UPDATED_ADDRESS_3)
            .address4(UPDATED_ADDRESS_4)
            .address6(UPDATED_ADDRESS_6)
            .dialCode(UPDATED_DIAL_CODE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .messengerId(UPDATED_MESSENGER_ID)
            .inActiveDate(UPDATED_IN_ACTIVE_DATE);

        restAddressDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddressDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddressDetails))
            )
            .andExpect(status().isOk());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeUpdate);
        AddressDetails testAddressDetails = addressDetailsList.get(addressDetailsList.size() - 1);
        assertThat(testAddressDetails.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testAddressDetails.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testAddressDetails.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testAddressDetails.getAddress3()).isEqualTo(UPDATED_ADDRESS_3);
        assertThat(testAddressDetails.getAddress4()).isEqualTo(UPDATED_ADDRESS_4);
        assertThat(testAddressDetails.getAddress5()).isEqualTo(DEFAULT_ADDRESS_5);
        assertThat(testAddressDetails.getAddress6()).isEqualTo(UPDATED_ADDRESS_6);
        assertThat(testAddressDetails.getDialCode()).isEqualTo(UPDATED_DIAL_CODE);
        assertThat(testAddressDetails.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testAddressDetails.getMessengerId()).isEqualTo(UPDATED_MESSENGER_ID);
        assertThat(testAddressDetails.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
        assertThat(testAddressDetails.getInActive()).isEqualTo(DEFAULT_IN_ACTIVE);
        assertThat(testAddressDetails.getInActiveDate()).isEqualTo(UPDATED_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void fullUpdateAddressDetailsWithPatch() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        int databaseSizeBeforeUpdate = addressDetailsRepository.findAll().size();

        // Update the addressDetails using partial update
        AddressDetails partialUpdatedAddressDetails = new AddressDetails();
        partialUpdatedAddressDetails.setId(addressDetails.getId());

        partialUpdatedAddressDetails
            .contactName(UPDATED_CONTACT_NAME)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .address3(UPDATED_ADDRESS_3)
            .address4(UPDATED_ADDRESS_4)
            .address5(UPDATED_ADDRESS_5)
            .address6(UPDATED_ADDRESS_6)
            .dialCode(UPDATED_DIAL_CODE)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .messengerId(UPDATED_MESSENGER_ID)
            .dateAdded(UPDATED_DATE_ADDED)
            .inActive(UPDATED_IN_ACTIVE)
            .inActiveDate(UPDATED_IN_ACTIVE_DATE);

        restAddressDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddressDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddressDetails))
            )
            .andExpect(status().isOk());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeUpdate);
        AddressDetails testAddressDetails = addressDetailsList.get(addressDetailsList.size() - 1);
        assertThat(testAddressDetails.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testAddressDetails.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testAddressDetails.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testAddressDetails.getAddress3()).isEqualTo(UPDATED_ADDRESS_3);
        assertThat(testAddressDetails.getAddress4()).isEqualTo(UPDATED_ADDRESS_4);
        assertThat(testAddressDetails.getAddress5()).isEqualTo(UPDATED_ADDRESS_5);
        assertThat(testAddressDetails.getAddress6()).isEqualTo(UPDATED_ADDRESS_6);
        assertThat(testAddressDetails.getDialCode()).isEqualTo(UPDATED_DIAL_CODE);
        assertThat(testAddressDetails.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testAddressDetails.getMessengerId()).isEqualTo(UPDATED_MESSENGER_ID);
        assertThat(testAddressDetails.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
        assertThat(testAddressDetails.getInActive()).isEqualTo(UPDATED_IN_ACTIVE);
        assertThat(testAddressDetails.getInActiveDate()).isEqualTo(UPDATED_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingAddressDetails() throws Exception {
        int databaseSizeBeforeUpdate = addressDetailsRepository.findAll().size();
        addressDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, addressDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addressDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAddressDetails() throws Exception {
        int databaseSizeBeforeUpdate = addressDetailsRepository.findAll().size();
        addressDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addressDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAddressDetails() throws Exception {
        int databaseSizeBeforeUpdate = addressDetailsRepository.findAll().size();
        addressDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(addressDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AddressDetails in the database
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAddressDetails() throws Exception {
        // Initialize the database
        addressDetailsRepository.saveAndFlush(addressDetails);

        int databaseSizeBeforeDelete = addressDetailsRepository.findAll().size();

        // Delete the addressDetails
        restAddressDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, addressDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AddressDetails> addressDetailsList = addressDetailsRepository.findAll();
        assertThat(addressDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
