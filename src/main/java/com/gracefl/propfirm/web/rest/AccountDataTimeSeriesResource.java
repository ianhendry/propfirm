package com.gracefl.propfirm.web.rest;

import com.gracefl.propfirm.domain.AccountDataTimeSeries;
import com.gracefl.propfirm.repository.AccountDataTimeSeriesRepository;
import com.gracefl.propfirm.service.AccountDataTimeSeriesService;
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
 * REST controller for managing {@link com.gracefl.propfirm.domain.AccountDataTimeSeries}.
 */
@RestController
@RequestMapping("/api")
public class AccountDataTimeSeriesResource {

    private final Logger log = LoggerFactory.getLogger(AccountDataTimeSeriesResource.class);

    private static final String ENTITY_NAME = "accountDataTimeSeries";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountDataTimeSeriesService accountDataTimeSeriesService;

    private final AccountDataTimeSeriesRepository accountDataTimeSeriesRepository;

    public AccountDataTimeSeriesResource(
        AccountDataTimeSeriesService accountDataTimeSeriesService,
        AccountDataTimeSeriesRepository accountDataTimeSeriesRepository
    ) {
        this.accountDataTimeSeriesService = accountDataTimeSeriesService;
        this.accountDataTimeSeriesRepository = accountDataTimeSeriesRepository;
    }

    /**
     * {@code POST  /account-data-time-series} : Create a new accountDataTimeSeries.
     *
     * @param accountDataTimeSeries the accountDataTimeSeries to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountDataTimeSeries, or with status {@code 400 (Bad Request)} if the accountDataTimeSeries has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/account-data-time-series")
    public ResponseEntity<AccountDataTimeSeries> createAccountDataTimeSeries(@RequestBody AccountDataTimeSeries accountDataTimeSeries)
        throws URISyntaxException {
        log.debug("REST request to save AccountDataTimeSeries : {}", accountDataTimeSeries);
        if (accountDataTimeSeries.getId() != null) {
            throw new BadRequestAlertException("A new accountDataTimeSeries cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountDataTimeSeries result = accountDataTimeSeriesService.save(accountDataTimeSeries);
        return ResponseEntity
            .created(new URI("/api/account-data-time-series/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /account-data-time-series/:id} : Updates an existing accountDataTimeSeries.
     *
     * @param id the id of the accountDataTimeSeries to save.
     * @param accountDataTimeSeries the accountDataTimeSeries to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountDataTimeSeries,
     * or with status {@code 400 (Bad Request)} if the accountDataTimeSeries is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountDataTimeSeries couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/account-data-time-series/{id}")
    public ResponseEntity<AccountDataTimeSeries> updateAccountDataTimeSeries(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccountDataTimeSeries accountDataTimeSeries
    ) throws URISyntaxException {
        log.debug("REST request to update AccountDataTimeSeries : {}, {}", id, accountDataTimeSeries);
        if (accountDataTimeSeries.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountDataTimeSeries.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountDataTimeSeriesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AccountDataTimeSeries result = accountDataTimeSeriesService.save(accountDataTimeSeries);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountDataTimeSeries.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /account-data-time-series/:id} : Partial updates given fields of an existing accountDataTimeSeries, field will ignore if it is null
     *
     * @param id the id of the accountDataTimeSeries to save.
     * @param accountDataTimeSeries the accountDataTimeSeries to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountDataTimeSeries,
     * or with status {@code 400 (Bad Request)} if the accountDataTimeSeries is not valid,
     * or with status {@code 404 (Not Found)} if the accountDataTimeSeries is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountDataTimeSeries couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/account-data-time-series/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AccountDataTimeSeries> partialUpdateAccountDataTimeSeries(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AccountDataTimeSeries accountDataTimeSeries
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountDataTimeSeries partially : {}, {}", id, accountDataTimeSeries);
        if (accountDataTimeSeries.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountDataTimeSeries.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountDataTimeSeriesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountDataTimeSeries> result = accountDataTimeSeriesService.partialUpdate(accountDataTimeSeries);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountDataTimeSeries.getId().toString())
        );
    }

    /**
     * {@code GET  /account-data-time-series} : get all the accountDataTimeSeries.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountDataTimeSeries in body.
     */
    @GetMapping("/account-data-time-series")
    public List<AccountDataTimeSeries> getAllAccountDataTimeSeries() {
        log.debug("REST request to get all AccountDataTimeSeries");
        return accountDataTimeSeriesService.findAll();
    }

    /**
     * {@code GET  /account-data-time-series/:id} : get the "id" accountDataTimeSeries.
     *
     * @param id the id of the accountDataTimeSeries to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountDataTimeSeries, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-data-time-series/{id}")
    public ResponseEntity<AccountDataTimeSeries> getAccountDataTimeSeries(@PathVariable Long id) {
        log.debug("REST request to get AccountDataTimeSeries : {}", id);
        Optional<AccountDataTimeSeries> accountDataTimeSeries = accountDataTimeSeriesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountDataTimeSeries);
    }

    /**
     * {@code DELETE  /account-data-time-series/:id} : delete the "id" accountDataTimeSeries.
     *
     * @param id the id of the accountDataTimeSeries to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/account-data-time-series/{id}")
    public ResponseEntity<Void> deleteAccountDataTimeSeries(@PathVariable Long id) {
        log.debug("REST request to delete AccountDataTimeSeries : {}", id);
        accountDataTimeSeriesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
