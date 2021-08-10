package com.gracefl.propfirm.web.rest;

import com.gracefl.propfirm.domain.ChallengeType;
import com.gracefl.propfirm.repository.ChallengeTypeRepository;
import com.gracefl.propfirm.service.ChallengeTypeService;
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
 * REST controller for managing {@link com.gracefl.propfirm.domain.ChallengeType}.
 */
@RestController
@RequestMapping("/api")
public class ChallengeTypeResource {

    private final Logger log = LoggerFactory.getLogger(ChallengeTypeResource.class);

    private static final String ENTITY_NAME = "challengeType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChallengeTypeService challengeTypeService;

    private final ChallengeTypeRepository challengeTypeRepository;

    public ChallengeTypeResource(ChallengeTypeService challengeTypeService, ChallengeTypeRepository challengeTypeRepository) {
        this.challengeTypeService = challengeTypeService;
        this.challengeTypeRepository = challengeTypeRepository;
    }

    /**
     * {@code POST  /challenge-types} : Create a new challengeType.
     *
     * @param challengeType the challengeType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new challengeType, or with status {@code 400 (Bad Request)} if the challengeType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/challenge-types")
    public ResponseEntity<ChallengeType> createChallengeType(@RequestBody ChallengeType challengeType) throws URISyntaxException {
        log.debug("REST request to save ChallengeType : {}", challengeType);
        if (challengeType.getId() != null) {
            throw new BadRequestAlertException("A new challengeType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChallengeType result = challengeTypeService.save(challengeType);
        return ResponseEntity
            .created(new URI("/api/challenge-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /challenge-types/:id} : Updates an existing challengeType.
     *
     * @param id the id of the challengeType to save.
     * @param challengeType the challengeType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated challengeType,
     * or with status {@code 400 (Bad Request)} if the challengeType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the challengeType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/challenge-types/{id}")
    public ResponseEntity<ChallengeType> updateChallengeType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ChallengeType challengeType
    ) throws URISyntaxException {
        log.debug("REST request to update ChallengeType : {}, {}", id, challengeType);
        if (challengeType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, challengeType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!challengeTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ChallengeType result = challengeTypeService.save(challengeType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, challengeType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /challenge-types/:id} : Partial updates given fields of an existing challengeType, field will ignore if it is null
     *
     * @param id the id of the challengeType to save.
     * @param challengeType the challengeType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated challengeType,
     * or with status {@code 400 (Bad Request)} if the challengeType is not valid,
     * or with status {@code 404 (Not Found)} if the challengeType is not found,
     * or with status {@code 500 (Internal Server Error)} if the challengeType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/challenge-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ChallengeType> partialUpdateChallengeType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ChallengeType challengeType
    ) throws URISyntaxException {
        log.debug("REST request to partial update ChallengeType partially : {}, {}", id, challengeType);
        if (challengeType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, challengeType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!challengeTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChallengeType> result = challengeTypeService.partialUpdate(challengeType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, challengeType.getId().toString())
        );
    }

    /**
     * {@code GET  /challenge-types} : get all the challengeTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of challengeTypes in body.
     */
    @GetMapping("/challenge-types")
    public ResponseEntity<List<ChallengeType>> getAllChallengeTypes(Pageable pageable) {
        log.debug("REST request to get a page of ChallengeTypes");
        Page<ChallengeType> page = challengeTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /challenge-types/:id} : get the "id" challengeType.
     *
     * @param id the id of the challengeType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the challengeType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/challenge-types/{id}")
    public ResponseEntity<ChallengeType> getChallengeType(@PathVariable Long id) {
        log.debug("REST request to get ChallengeType : {}", id);
        Optional<ChallengeType> challengeType = challengeTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(challengeType);
    }

    /**
     * {@code DELETE  /challenge-types/:id} : delete the "id" challengeType.
     *
     * @param id the id of the challengeType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/challenge-types/{id}")
    public ResponseEntity<Void> deleteChallengeType(@PathVariable Long id) {
        log.debug("REST request to delete ChallengeType : {}", id);
        challengeTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
