package com.gracefl.propfirm.web.rest;

import com.gracefl.propfirm.domain.SiteAccount;
import com.gracefl.propfirm.repository.SiteAccountRepository;
import com.gracefl.propfirm.service.SiteAccountQueryService;
import com.gracefl.propfirm.service.SiteAccountService;
import com.gracefl.propfirm.service.criteria.SiteAccountCriteria;
import com.gracefl.propfirm.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.gracefl.propfirm.domain.SiteAccount}.
 */
@RestController
@RequestMapping("/api")
public class SiteAccountResource {

    private final Logger log = LoggerFactory.getLogger(SiteAccountResource.class);

    private static final String ENTITY_NAME = "siteAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SiteAccountService siteAccountService;

    private final SiteAccountRepository siteAccountRepository;

    private final SiteAccountQueryService siteAccountQueryService;

    public SiteAccountResource(
        SiteAccountService siteAccountService,
        SiteAccountRepository siteAccountRepository,
        SiteAccountQueryService siteAccountQueryService
    ) {
        this.siteAccountService = siteAccountService;
        this.siteAccountRepository = siteAccountRepository;
        this.siteAccountQueryService = siteAccountQueryService;
    }

    /**
     * {@code POST  /site-accounts} : Create a new siteAccount.
     *
     * @param siteAccount the siteAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new siteAccount, or with status {@code 400 (Bad Request)} if the siteAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/site-accounts")
    public ResponseEntity<SiteAccount> createSiteAccount(@RequestBody SiteAccount siteAccount) throws URISyntaxException {
        log.debug("REST request to save SiteAccount : {}", siteAccount);
        if (siteAccount.getId() != null) {
            throw new BadRequestAlertException("A new siteAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SiteAccount result = siteAccountService.save(siteAccount);
        return ResponseEntity
            .created(new URI("/api/site-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /site-accounts/:id} : Updates an existing siteAccount.
     *
     * @param id the id of the siteAccount to save.
     * @param siteAccount the siteAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated siteAccount,
     * or with status {@code 400 (Bad Request)} if the siteAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the siteAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/site-accounts/{id}")
    public ResponseEntity<SiteAccount> updateSiteAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SiteAccount siteAccount
    ) throws URISyntaxException {
        log.debug("REST request to update SiteAccount : {}, {}", id, siteAccount);
        if (siteAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, siteAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!siteAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SiteAccount result = siteAccountService.save(siteAccount);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, siteAccount.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /site-accounts/:id} : Partial updates given fields of an existing siteAccount, field will ignore if it is null
     *
     * @param id the id of the siteAccount to save.
     * @param siteAccount the siteAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated siteAccount,
     * or with status {@code 400 (Bad Request)} if the siteAccount is not valid,
     * or with status {@code 404 (Not Found)} if the siteAccount is not found,
     * or with status {@code 500 (Internal Server Error)} if the siteAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/site-accounts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SiteAccount> partialUpdateSiteAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SiteAccount siteAccount
    ) throws URISyntaxException {
        log.debug("REST request to partial update SiteAccount partially : {}, {}", id, siteAccount);
        if (siteAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, siteAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!siteAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SiteAccount> result = siteAccountService.partialUpdate(siteAccount);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, siteAccount.getId().toString())
        );
    }

    /**
     * {@code GET  /site-accounts} : get all the siteAccounts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of siteAccounts in body.
     */
    @GetMapping("/site-accounts")
    public ResponseEntity<List<SiteAccount>> getAllSiteAccounts(SiteAccountCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SiteAccounts by criteria: {}", criteria);
        Page<SiteAccount> page = siteAccountQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /site-accounts/count} : count all the siteAccounts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/site-accounts/count")
    public ResponseEntity<Long> countSiteAccounts(SiteAccountCriteria criteria) {
        log.debug("REST request to count SiteAccounts by criteria: {}", criteria);
        return ResponseEntity.ok().body(siteAccountQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /site-accounts/:id} : get the "id" siteAccount.
     *
     * @param id the id of the siteAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the siteAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/site-accounts/{id}")
    public ResponseEntity<SiteAccount> getSiteAccount(@PathVariable Long id) {
        log.debug("REST request to get SiteAccount : {}", id);
        Optional<SiteAccount> siteAccount = siteAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(siteAccount);
    }

    /**
     * {@code DELETE  /site-accounts/:id} : delete the "id" siteAccount.
     *
     * @param id the id of the siteAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/site-accounts/{id}")
    public ResponseEntity<Void> deleteSiteAccount(@PathVariable Long id) {
        log.debug("REST request to delete SiteAccount : {}", id);
        siteAccountService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
