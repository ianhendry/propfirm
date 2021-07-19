package com.gracefl.propfirm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gracefl.propfirm.IntegrationTest;
import com.gracefl.propfirm.domain.Instrument;
import com.gracefl.propfirm.domain.enumeration.INSTRUMENTTYPE;
import com.gracefl.propfirm.repository.InstrumentRepository;
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
 * Integration tests for the {@link InstrumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InstrumentResourceIT {

    private static final String DEFAULT_TICKER = "AAAAAAAAAA";
    private static final String UPDATED_TICKER = "BBBBBBBBBB";

    private static final INSTRUMENTTYPE DEFAULT_INSTRUMENT_TYPE = INSTRUMENTTYPE.CFD;
    private static final INSTRUMENTTYPE UPDATED_INSTRUMENT_TYPE = INSTRUMENTTYPE.CRYPTO;

    private static final String DEFAULT_EXCHANGE = "AAAAAAAAAA";
    private static final String UPDATED_EXCHANGE = "BBBBBBBBBB";

    private static final Double DEFAULT_AVERAGE_SPREAD = 1D;
    private static final Double UPDATED_AVERAGE_SPREAD = 2D;

    private static final String DEFAULT_TRADE_RESTRICTIONS = "AAAAAAAAAA";
    private static final String UPDATED_TRADE_RESTRICTIONS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IN_ACTIVE = false;
    private static final Boolean UPDATED_IN_ACTIVE = true;

    private static final Instant DEFAULT_IN_ACTIVE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_IN_ACTIVE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/instruments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInstrumentMockMvc;

    private Instrument instrument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instrument createEntity(EntityManager em) {
        Instrument instrument = new Instrument()
            .ticker(DEFAULT_TICKER)
            .instrumentType(DEFAULT_INSTRUMENT_TYPE)
            .exchange(DEFAULT_EXCHANGE)
            .averageSpread(DEFAULT_AVERAGE_SPREAD)
            .tradeRestrictions(DEFAULT_TRADE_RESTRICTIONS)
            .inActive(DEFAULT_IN_ACTIVE)
            .inActiveDate(DEFAULT_IN_ACTIVE_DATE);
        return instrument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Instrument createUpdatedEntity(EntityManager em) {
        Instrument instrument = new Instrument()
            .ticker(UPDATED_TICKER)
            .instrumentType(UPDATED_INSTRUMENT_TYPE)
            .exchange(UPDATED_EXCHANGE)
            .averageSpread(UPDATED_AVERAGE_SPREAD)
            .tradeRestrictions(UPDATED_TRADE_RESTRICTIONS)
            .inActive(UPDATED_IN_ACTIVE)
            .inActiveDate(UPDATED_IN_ACTIVE_DATE);
        return instrument;
    }

    @BeforeEach
    public void initTest() {
        instrument = createEntity(em);
    }

    @Test
    @Transactional
    void createInstrument() throws Exception {
        int databaseSizeBeforeCreate = instrumentRepository.findAll().size();
        // Create the Instrument
        restInstrumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(instrument)))
            .andExpect(status().isCreated());

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeCreate + 1);
        Instrument testInstrument = instrumentList.get(instrumentList.size() - 1);
        assertThat(testInstrument.getTicker()).isEqualTo(DEFAULT_TICKER);
        assertThat(testInstrument.getInstrumentType()).isEqualTo(DEFAULT_INSTRUMENT_TYPE);
        assertThat(testInstrument.getExchange()).isEqualTo(DEFAULT_EXCHANGE);
        assertThat(testInstrument.getAverageSpread()).isEqualTo(DEFAULT_AVERAGE_SPREAD);
        assertThat(testInstrument.getTradeRestrictions()).isEqualTo(DEFAULT_TRADE_RESTRICTIONS);
        assertThat(testInstrument.getInActive()).isEqualTo(DEFAULT_IN_ACTIVE);
        assertThat(testInstrument.getInActiveDate()).isEqualTo(DEFAULT_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void createInstrumentWithExistingId() throws Exception {
        // Create the Instrument with an existing ID
        instrument.setId(1L);

        int databaseSizeBeforeCreate = instrumentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstrumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(instrument)))
            .andExpect(status().isBadRequest());

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInstruments() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        // Get all the instrumentList
        restInstrumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instrument.getId().intValue())))
            .andExpect(jsonPath("$.[*].ticker").value(hasItem(DEFAULT_TICKER)))
            .andExpect(jsonPath("$.[*].instrumentType").value(hasItem(DEFAULT_INSTRUMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].exchange").value(hasItem(DEFAULT_EXCHANGE)))
            .andExpect(jsonPath("$.[*].averageSpread").value(hasItem(DEFAULT_AVERAGE_SPREAD.doubleValue())))
            .andExpect(jsonPath("$.[*].tradeRestrictions").value(hasItem(DEFAULT_TRADE_RESTRICTIONS.toString())))
            .andExpect(jsonPath("$.[*].inActive").value(hasItem(DEFAULT_IN_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].inActiveDate").value(hasItem(DEFAULT_IN_ACTIVE_DATE.toString())));
    }

    @Test
    @Transactional
    void getInstrument() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        // Get the instrument
        restInstrumentMockMvc
            .perform(get(ENTITY_API_URL_ID, instrument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(instrument.getId().intValue()))
            .andExpect(jsonPath("$.ticker").value(DEFAULT_TICKER))
            .andExpect(jsonPath("$.instrumentType").value(DEFAULT_INSTRUMENT_TYPE.toString()))
            .andExpect(jsonPath("$.exchange").value(DEFAULT_EXCHANGE))
            .andExpect(jsonPath("$.averageSpread").value(DEFAULT_AVERAGE_SPREAD.doubleValue()))
            .andExpect(jsonPath("$.tradeRestrictions").value(DEFAULT_TRADE_RESTRICTIONS.toString()))
            .andExpect(jsonPath("$.inActive").value(DEFAULT_IN_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.inActiveDate").value(DEFAULT_IN_ACTIVE_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingInstrument() throws Exception {
        // Get the instrument
        restInstrumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInstrument() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        int databaseSizeBeforeUpdate = instrumentRepository.findAll().size();

        // Update the instrument
        Instrument updatedInstrument = instrumentRepository.findById(instrument.getId()).get();
        // Disconnect from session so that the updates on updatedInstrument are not directly saved in db
        em.detach(updatedInstrument);
        updatedInstrument
            .ticker(UPDATED_TICKER)
            .instrumentType(UPDATED_INSTRUMENT_TYPE)
            .exchange(UPDATED_EXCHANGE)
            .averageSpread(UPDATED_AVERAGE_SPREAD)
            .tradeRestrictions(UPDATED_TRADE_RESTRICTIONS)
            .inActive(UPDATED_IN_ACTIVE)
            .inActiveDate(UPDATED_IN_ACTIVE_DATE);

        restInstrumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInstrument.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInstrument))
            )
            .andExpect(status().isOk());

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
        Instrument testInstrument = instrumentList.get(instrumentList.size() - 1);
        assertThat(testInstrument.getTicker()).isEqualTo(UPDATED_TICKER);
        assertThat(testInstrument.getInstrumentType()).isEqualTo(UPDATED_INSTRUMENT_TYPE);
        assertThat(testInstrument.getExchange()).isEqualTo(UPDATED_EXCHANGE);
        assertThat(testInstrument.getAverageSpread()).isEqualTo(UPDATED_AVERAGE_SPREAD);
        assertThat(testInstrument.getTradeRestrictions()).isEqualTo(UPDATED_TRADE_RESTRICTIONS);
        assertThat(testInstrument.getInActive()).isEqualTo(UPDATED_IN_ACTIVE);
        assertThat(testInstrument.getInActiveDate()).isEqualTo(UPDATED_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void putNonExistingInstrument() throws Exception {
        int databaseSizeBeforeUpdate = instrumentRepository.findAll().size();
        instrument.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstrumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, instrument.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(instrument))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInstrument() throws Exception {
        int databaseSizeBeforeUpdate = instrumentRepository.findAll().size();
        instrument.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstrumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(instrument))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInstrument() throws Exception {
        int databaseSizeBeforeUpdate = instrumentRepository.findAll().size();
        instrument.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstrumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(instrument)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInstrumentWithPatch() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        int databaseSizeBeforeUpdate = instrumentRepository.findAll().size();

        // Update the instrument using partial update
        Instrument partialUpdatedInstrument = new Instrument();
        partialUpdatedInstrument.setId(instrument.getId());

        partialUpdatedInstrument
            .ticker(UPDATED_TICKER)
            .instrumentType(UPDATED_INSTRUMENT_TYPE)
            .inActive(UPDATED_IN_ACTIVE)
            .inActiveDate(UPDATED_IN_ACTIVE_DATE);

        restInstrumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstrument.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInstrument))
            )
            .andExpect(status().isOk());

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
        Instrument testInstrument = instrumentList.get(instrumentList.size() - 1);
        assertThat(testInstrument.getTicker()).isEqualTo(UPDATED_TICKER);
        assertThat(testInstrument.getInstrumentType()).isEqualTo(UPDATED_INSTRUMENT_TYPE);
        assertThat(testInstrument.getExchange()).isEqualTo(DEFAULT_EXCHANGE);
        assertThat(testInstrument.getAverageSpread()).isEqualTo(DEFAULT_AVERAGE_SPREAD);
        assertThat(testInstrument.getTradeRestrictions()).isEqualTo(DEFAULT_TRADE_RESTRICTIONS);
        assertThat(testInstrument.getInActive()).isEqualTo(UPDATED_IN_ACTIVE);
        assertThat(testInstrument.getInActiveDate()).isEqualTo(UPDATED_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void fullUpdateInstrumentWithPatch() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        int databaseSizeBeforeUpdate = instrumentRepository.findAll().size();

        // Update the instrument using partial update
        Instrument partialUpdatedInstrument = new Instrument();
        partialUpdatedInstrument.setId(instrument.getId());

        partialUpdatedInstrument
            .ticker(UPDATED_TICKER)
            .instrumentType(UPDATED_INSTRUMENT_TYPE)
            .exchange(UPDATED_EXCHANGE)
            .averageSpread(UPDATED_AVERAGE_SPREAD)
            .tradeRestrictions(UPDATED_TRADE_RESTRICTIONS)
            .inActive(UPDATED_IN_ACTIVE)
            .inActiveDate(UPDATED_IN_ACTIVE_DATE);

        restInstrumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInstrument.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInstrument))
            )
            .andExpect(status().isOk());

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
        Instrument testInstrument = instrumentList.get(instrumentList.size() - 1);
        assertThat(testInstrument.getTicker()).isEqualTo(UPDATED_TICKER);
        assertThat(testInstrument.getInstrumentType()).isEqualTo(UPDATED_INSTRUMENT_TYPE);
        assertThat(testInstrument.getExchange()).isEqualTo(UPDATED_EXCHANGE);
        assertThat(testInstrument.getAverageSpread()).isEqualTo(UPDATED_AVERAGE_SPREAD);
        assertThat(testInstrument.getTradeRestrictions()).isEqualTo(UPDATED_TRADE_RESTRICTIONS);
        assertThat(testInstrument.getInActive()).isEqualTo(UPDATED_IN_ACTIVE);
        assertThat(testInstrument.getInActiveDate()).isEqualTo(UPDATED_IN_ACTIVE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingInstrument() throws Exception {
        int databaseSizeBeforeUpdate = instrumentRepository.findAll().size();
        instrument.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstrumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, instrument.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(instrument))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInstrument() throws Exception {
        int databaseSizeBeforeUpdate = instrumentRepository.findAll().size();
        instrument.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstrumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(instrument))
            )
            .andExpect(status().isBadRequest());

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInstrument() throws Exception {
        int databaseSizeBeforeUpdate = instrumentRepository.findAll().size();
        instrument.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInstrumentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(instrument))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Instrument in the database
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInstrument() throws Exception {
        // Initialize the database
        instrumentRepository.saveAndFlush(instrument);

        int databaseSizeBeforeDelete = instrumentRepository.findAll().size();

        // Delete the instrument
        restInstrumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, instrument.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Instrument> instrumentList = instrumentRepository.findAll();
        assertThat(instrumentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
