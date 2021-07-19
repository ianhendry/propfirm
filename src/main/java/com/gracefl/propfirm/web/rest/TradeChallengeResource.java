package com.gracefl.propfirm.web.rest;

import com.gracefl.propfirm.domain.TradeChallenge;
import com.gracefl.propfirm.repository.TradeChallengeRepository;
import com.gracefl.propfirm.service.TradeChallengeService;
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
 * REST controller for managing {@link com.gracefl.propfirm.domain.TradeChallenge}.
 */
@RestController
@RequestMapping("/api")
public class TradeChallengeResource {

    private final Logger log = LoggerFactory.getLogger(TradeChallengeResource.class);

    private static final String ENTITY_NAME = "tradeChallenge";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TradeChallengeService tradeChallengeService;

    private final TradeChallengeRepository tradeChallengeRepository;

    public TradeChallengeResource(TradeChallengeService tradeChallengeService, TradeChallengeRepository tradeChallengeRepository) {
        this.tradeChallengeService = tradeChallengeService;
        this.tradeChallengeRepository = tradeChallengeRepository;
    }

    /**
     * {@code POST  /trade-challenges} : Create a new tradeChallenge.
     *
     * @param tradeChallenge the tradeChallenge to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tradeChallenge, or with status {@code 400 (Bad Request)} if the tradeChallenge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trade-challenges")
    public ResponseEntity<TradeChallenge> createTradeChallenge(@RequestBody TradeChallenge tradeChallenge) throws URISyntaxException {
        log.debug("REST request to save TradeChallenge : {}", tradeChallenge);
        if (tradeChallenge.getId() != null) {
            throw new BadRequestAlertException("A new tradeChallenge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TradeChallenge result = tradeChallengeService.save(tradeChallenge);
        return ResponseEntity
            .created(new URI("/api/trade-challenges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trade-challenges/:id} : Updates an existing tradeChallenge.
     *
     * @param id the id of the tradeChallenge to save.
     * @param tradeChallenge the tradeChallenge to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tradeChallenge,
     * or with status {@code 400 (Bad Request)} if the tradeChallenge is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tradeChallenge couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trade-challenges/{id}")
    public ResponseEntity<TradeChallenge> updateTradeChallenge(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TradeChallenge tradeChallenge
    ) throws URISyntaxException {
        log.debug("REST request to update TradeChallenge : {}, {}", id, tradeChallenge);
        if (tradeChallenge.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tradeChallenge.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tradeChallengeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TradeChallenge result = tradeChallengeService.save(tradeChallenge);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tradeChallenge.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trade-challenges/:id} : Partial updates given fields of an existing tradeChallenge, field will ignore if it is null
     *
     * @param id the id of the tradeChallenge to save.
     * @param tradeChallenge the tradeChallenge to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tradeChallenge,
     * or with status {@code 400 (Bad Request)} if the tradeChallenge is not valid,
     * or with status {@code 404 (Not Found)} if the tradeChallenge is not found,
     * or with status {@code 500 (Internal Server Error)} if the tradeChallenge couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trade-challenges/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TradeChallenge> partialUpdateTradeChallenge(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TradeChallenge tradeChallenge
    ) throws URISyntaxException {
        log.debug("REST request to partial update TradeChallenge partially : {}, {}", id, tradeChallenge);
        if (tradeChallenge.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tradeChallenge.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tradeChallengeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TradeChallenge> result = tradeChallengeService.partialUpdate(tradeChallenge);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tradeChallenge.getId().toString())
        );
    }

    /**
     * {@code GET  /trade-challenges} : get all the tradeChallenges.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tradeChallenges in body.
     */
    @GetMapping("/trade-challenges")
    public ResponseEntity<List<TradeChallenge>> getAllTradeChallenges(Pageable pageable) {
        log.debug("REST request to get a page of TradeChallenges");
        Page<TradeChallenge> page = tradeChallengeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trade-challenges/:id} : get the "id" tradeChallenge.
     *
     * @param id the id of the tradeChallenge to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tradeChallenge, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trade-challenges/{id}")
    public ResponseEntity<TradeChallenge> getTradeChallenge(@PathVariable Long id) {
        log.debug("REST request to get TradeChallenge : {}", id);
        Optional<TradeChallenge> tradeChallenge = tradeChallengeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tradeChallenge);
    }

    /**
     * {@code DELETE  /trade-challenges/:id} : delete the "id" tradeChallenge.
     *
     * @param id the id of the tradeChallenge to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trade-challenges/{id}")
    public ResponseEntity<Void> deleteTradeChallenge(@PathVariable Long id) {
        log.debug("REST request to delete TradeChallenge : {}", id);
        tradeChallengeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
