package com.gracefl.propfirm.web.rest;

import com.gracefl.propfirm.domain.Mt4Account;
import com.gracefl.propfirm.repository.Mt4AccountRepository;
import com.gracefl.propfirm.service.Mt4AccountQueryService;
import com.gracefl.propfirm.service.Mt4AccountService;
import com.gracefl.propfirm.service.criteria.Mt4AccountCriteria;
import com.gracefl.propfirm.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gracefl.propfirm.domain.Mt4Account}.
 */
@RestController
@RequestMapping("/api")
public class Mt4AccountResource {

    private final Logger log = LoggerFactory.getLogger(Mt4AccountResource.class);

    private static final String ENTITY_NAME = "mt4Account";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Mt4AccountService mt4AccountService;

    private final Mt4AccountRepository mt4AccountRepository;

    private final Mt4AccountQueryService mt4AccountQueryService;

    public Mt4AccountResource(
        Mt4AccountService mt4AccountService,
        Mt4AccountRepository mt4AccountRepository,
        Mt4AccountQueryService mt4AccountQueryService
    ) {
        this.mt4AccountService = mt4AccountService;
        this.mt4AccountRepository = mt4AccountRepository;
        this.mt4AccountQueryService = mt4AccountQueryService;
    }

    /**
     * {@code POST  /mt-4-accounts} : Create a new mt4Account.
     *
     * @param mt4Account the mt4Account to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mt4Account, or with status {@code 400 (Bad Request)} if the mt4Account has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mt-4-accounts")
    public ResponseEntity<Mt4Account> createMt4Account(@RequestBody Mt4Account mt4Account) throws URISyntaxException {
        log.debug("REST request to save Mt4Account : {}", mt4Account);
        if (mt4Account.getId() != null) {
            throw new BadRequestAlertException("A new mt4Account cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mt4Account result = mt4AccountService.save(mt4Account);
        return ResponseEntity
            .created(new URI("/api/mt-4-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mt-4-accounts/:id} : Updates an existing mt4Account.
     *
     * @param id the id of the mt4Account to save.
     * @param mt4Account the mt4Account to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mt4Account,
     * or with status {@code 400 (Bad Request)} if the mt4Account is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mt4Account couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mt-4-accounts/{id}")
    public ResponseEntity<Mt4Account> updateMt4Account(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Mt4Account mt4Account
    ) throws URISyntaxException {
        log.debug("REST request to update Mt4Account : {}, {}", id, mt4Account);
        if (mt4Account.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mt4Account.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mt4AccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mt4Account result = mt4AccountService.save(mt4Account);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mt4Account.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mt-4-accounts/:id} : Partial updates given fields of an existing mt4Account, field will ignore if it is null
     *
     * @param id the id of the mt4Account to save.
     * @param mt4Account the mt4Account to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mt4Account,
     * or with status {@code 400 (Bad Request)} if the mt4Account is not valid,
     * or with status {@code 404 (Not Found)} if the mt4Account is not found,
     * or with status {@code 500 (Internal Server Error)} if the mt4Account couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mt-4-accounts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Mt4Account> partialUpdateMt4Account(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Mt4Account mt4Account
    ) throws URISyntaxException {
        log.debug("REST request to partial update Mt4Account partially : {}, {}", id, mt4Account);
        if (mt4Account.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mt4Account.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mt4AccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Mt4Account> result = mt4AccountService.partialUpdate(mt4Account);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mt4Account.getId().toString())
        );
    }

    /**
     * {@code GET  /mt-4-accounts} : get all the mt4Accounts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mt4Accounts in body.
     */
    @GetMapping("/mt-4-accounts")
    public ResponseEntity<List<Mt4Account>> getAllMt4Accounts(Mt4AccountCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Mt4Accounts by criteria: {}", criteria);
        Page<Mt4Account> page = mt4AccountQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mt-4-accounts/count} : count all the mt4Accounts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/mt-4-accounts/count")
    public ResponseEntity<Long> countMt4Accounts(Mt4AccountCriteria criteria) {
        log.debug("REST request to count Mt4Accounts by criteria: {}", criteria);
        return ResponseEntity.ok().body(mt4AccountQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /mt-4-accounts/:id} : get the "id" mt4Account.
     *
     * @param id the id of the mt4Account to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mt4Account, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mt-4-accounts/{id}")
    public ResponseEntity<Mt4Account> getMt4Account(@PathVariable Long id) {
        log.debug("REST request to get Mt4Account : {}", id);
        Optional<Mt4Account> mt4Account = mt4AccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mt4Account);
    }

    /**
     * {@code DELETE  /mt-4-accounts/:id} : delete the "id" mt4Account.
     *
     * @param id the id of the mt4Account to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mt-4-accounts/{id}")
    public ResponseEntity<Void> deleteMt4Account(@PathVariable Long id) {
        log.debug("REST request to delete Mt4Account : {}", id);
        mt4AccountService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
