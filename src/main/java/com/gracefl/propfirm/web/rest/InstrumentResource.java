package com.gracefl.propfirm.web.rest;

import com.gracefl.propfirm.domain.Instrument;
import com.gracefl.propfirm.repository.InstrumentRepository;
import com.gracefl.propfirm.service.InstrumentService;
import com.gracefl.propfirm.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gracefl.propfirm.domain.Instrument}.
 */
@RestController
@RequestMapping("/api")
public class InstrumentResource {

    private final Logger log = LoggerFactory.getLogger(InstrumentResource.class);

    private static final String ENTITY_NAME = "instrument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InstrumentService instrumentService;

    private final InstrumentRepository instrumentRepository;

    public InstrumentResource(InstrumentService instrumentService, InstrumentRepository instrumentRepository) {
        this.instrumentService = instrumentService;
        this.instrumentRepository = instrumentRepository;
    }

    /**
     * {@code POST  /instruments} : Create a new instrument.
     *
     * @param instrument the instrument to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new instrument, or with status {@code 400 (Bad Request)} if the instrument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/instruments")
    public ResponseEntity<Instrument> createInstrument(@RequestBody Instrument instrument) throws URISyntaxException {
        log.debug("REST request to save Instrument : {}", instrument);
        if (instrument.getId() != null) {
            throw new BadRequestAlertException("A new instrument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Instrument result = instrumentService.save(instrument);
        return ResponseEntity
            .created(new URI("/api/instruments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /instruments/:id} : Updates an existing instrument.
     *
     * @param id the id of the instrument to save.
     * @param instrument the instrument to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instrument,
     * or with status {@code 400 (Bad Request)} if the instrument is not valid,
     * or with status {@code 500 (Internal Server Error)} if the instrument couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/instruments/{id}")
    public ResponseEntity<Instrument> updateInstrument(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Instrument instrument
    ) throws URISyntaxException {
        log.debug("REST request to update Instrument : {}, {}", id, instrument);
        if (instrument.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, instrument.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!instrumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Instrument result = instrumentService.save(instrument);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, instrument.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /instruments/:id} : Partial updates given fields of an existing instrument, field will ignore if it is null
     *
     * @param id the id of the instrument to save.
     * @param instrument the instrument to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instrument,
     * or with status {@code 400 (Bad Request)} if the instrument is not valid,
     * or with status {@code 404 (Not Found)} if the instrument is not found,
     * or with status {@code 500 (Internal Server Error)} if the instrument couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/instruments/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Instrument> partialUpdateInstrument(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Instrument instrument
    ) throws URISyntaxException {
        log.debug("REST request to partial update Instrument partially : {}, {}", id, instrument);
        if (instrument.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, instrument.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!instrumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Instrument> result = instrumentService.partialUpdate(instrument);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, instrument.getId().toString())
        );
    }

    /**
     * {@code GET  /instruments} : get all the instruments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of instruments in body.
     */
    @GetMapping("/instruments")
    public List<Instrument> getAllInstruments() {
        log.debug("REST request to get all Instruments");
        return instrumentService.findAll();
    }

    /**
     * {@code GET  /instruments/:id} : get the "id" instrument.
     *
     * @param id the id of the instrument to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the instrument, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/instruments/{id}")
    public ResponseEntity<Instrument> getInstrument(@PathVariable Long id) {
        log.debug("REST request to get Instrument : {}", id);
        Optional<Instrument> instrument = instrumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(instrument);
    }

    /**
     * {@code DELETE  /instruments/:id} : delete the "id" instrument.
     *
     * @param id the id of the instrument to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/instruments/{id}")
    public ResponseEntity<Void> deleteInstrument(@PathVariable Long id) {
        log.debug("REST request to delete Instrument : {}", id);
        instrumentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
