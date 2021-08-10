package com.gracefl.propfirm.web.rest;

import com.gracefl.propfirm.domain.TradeJournalPost;
import com.gracefl.propfirm.repository.TradeJournalPostRepository;
import com.gracefl.propfirm.service.TradeJournalPostQueryService;
import com.gracefl.propfirm.service.TradeJournalPostService;
import com.gracefl.propfirm.service.criteria.TradeJournalPostCriteria;
import com.gracefl.propfirm.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.gracefl.propfirm.domain.TradeJournalPost}.
 */
@RestController
@RequestMapping("/api")
public class TradeJournalPostResource {

    private final Logger log = LoggerFactory.getLogger(TradeJournalPostResource.class);

    private static final String ENTITY_NAME = "tradeJournalPost";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TradeJournalPostService tradeJournalPostService;

    private final TradeJournalPostRepository tradeJournalPostRepository;

    private final TradeJournalPostQueryService tradeJournalPostQueryService;

    public TradeJournalPostResource(
        TradeJournalPostService tradeJournalPostService,
        TradeJournalPostRepository tradeJournalPostRepository,
        TradeJournalPostQueryService tradeJournalPostQueryService
    ) {
        this.tradeJournalPostService = tradeJournalPostService;
        this.tradeJournalPostRepository = tradeJournalPostRepository;
        this.tradeJournalPostQueryService = tradeJournalPostQueryService;
    }

    /**
     * {@code POST  /trade-journal-posts} : Create a new tradeJournalPost.
     *
     * @param tradeJournalPost the tradeJournalPost to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tradeJournalPost, or with status {@code 400 (Bad Request)} if the tradeJournalPost has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trade-journal-posts")
    public ResponseEntity<TradeJournalPost> createTradeJournalPost(@Valid @RequestBody TradeJournalPost tradeJournalPost)
        throws URISyntaxException {
        log.debug("REST request to save TradeJournalPost : {}", tradeJournalPost);
        if (tradeJournalPost.getId() != null) {
            throw new BadRequestAlertException("A new tradeJournalPost cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TradeJournalPost result = tradeJournalPostService.save(tradeJournalPost);
        return ResponseEntity
            .created(new URI("/api/trade-journal-posts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trade-journal-posts/:id} : Updates an existing tradeJournalPost.
     *
     * @param id the id of the tradeJournalPost to save.
     * @param tradeJournalPost the tradeJournalPost to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tradeJournalPost,
     * or with status {@code 400 (Bad Request)} if the tradeJournalPost is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tradeJournalPost couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trade-journal-posts/{id}")
    public ResponseEntity<TradeJournalPost> updateTradeJournalPost(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TradeJournalPost tradeJournalPost
    ) throws URISyntaxException {
        log.debug("REST request to update TradeJournalPost : {}, {}", id, tradeJournalPost);
        if (tradeJournalPost.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tradeJournalPost.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tradeJournalPostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TradeJournalPost result = tradeJournalPostService.save(tradeJournalPost);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tradeJournalPost.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trade-journal-posts/:id} : Partial updates given fields of an existing tradeJournalPost, field will ignore if it is null
     *
     * @param id the id of the tradeJournalPost to save.
     * @param tradeJournalPost the tradeJournalPost to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tradeJournalPost,
     * or with status {@code 400 (Bad Request)} if the tradeJournalPost is not valid,
     * or with status {@code 404 (Not Found)} if the tradeJournalPost is not found,
     * or with status {@code 500 (Internal Server Error)} if the tradeJournalPost couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trade-journal-posts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TradeJournalPost> partialUpdateTradeJournalPost(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TradeJournalPost tradeJournalPost
    ) throws URISyntaxException {
        log.debug("REST request to partial update TradeJournalPost partially : {}, {}", id, tradeJournalPost);
        if (tradeJournalPost.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tradeJournalPost.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tradeJournalPostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TradeJournalPost> result = tradeJournalPostService.partialUpdate(tradeJournalPost);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tradeJournalPost.getId().toString())
        );
    }

    /**
     * {@code GET  /trade-journal-posts} : get all the tradeJournalPosts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tradeJournalPosts in body.
     */
    @GetMapping("/trade-journal-posts")
    public ResponseEntity<List<TradeJournalPost>> getAllTradeJournalPosts(TradeJournalPostCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TradeJournalPosts by criteria: {}", criteria);
        Page<TradeJournalPost> page = tradeJournalPostQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trade-journal-posts/count} : count all the tradeJournalPosts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/trade-journal-posts/count")
    public ResponseEntity<Long> countTradeJournalPosts(TradeJournalPostCriteria criteria) {
        log.debug("REST request to count TradeJournalPosts by criteria: {}", criteria);
        return ResponseEntity.ok().body(tradeJournalPostQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /trade-journal-posts/:id} : get the "id" tradeJournalPost.
     *
     * @param id the id of the tradeJournalPost to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tradeJournalPost, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trade-journal-posts/{id}")
    public ResponseEntity<TradeJournalPost> getTradeJournalPost(@PathVariable Long id) {
        log.debug("REST request to get TradeJournalPost : {}", id);
        Optional<TradeJournalPost> tradeJournalPost = tradeJournalPostService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tradeJournalPost);
    }

    /**
     * {@code DELETE  /trade-journal-posts/:id} : delete the "id" tradeJournalPost.
     *
     * @param id the id of the tradeJournalPost to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trade-journal-posts/{id}")
    public ResponseEntity<Void> deleteTradeJournalPost(@PathVariable Long id) {
        log.debug("REST request to delete TradeJournalPost : {}", id);
        tradeJournalPostService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
