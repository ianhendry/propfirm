package com.gracefl.propfirm.web.rest;

import com.gracefl.propfirm.domain.AddressDetails;
import com.gracefl.propfirm.repository.AddressDetailsRepository;
import com.gracefl.propfirm.service.AddressDetailsService;
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
 * REST controller for managing {@link com.gracefl.propfirm.domain.AddressDetails}.
 */
@RestController
@RequestMapping("/api")
public class AddressDetailsResource {

    private final Logger log = LoggerFactory.getLogger(AddressDetailsResource.class);

    private static final String ENTITY_NAME = "addressDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AddressDetailsService addressDetailsService;

    private final AddressDetailsRepository addressDetailsRepository;

    public AddressDetailsResource(AddressDetailsService addressDetailsService, AddressDetailsRepository addressDetailsRepository) {
        this.addressDetailsService = addressDetailsService;
        this.addressDetailsRepository = addressDetailsRepository;
    }

    /**
     * {@code POST  /address-details} : Create a new addressDetails.
     *
     * @param addressDetails the addressDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new addressDetails, or with status {@code 400 (Bad Request)} if the addressDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/address-details")
    public ResponseEntity<AddressDetails> createAddressDetails(@RequestBody AddressDetails addressDetails) throws URISyntaxException {
        log.debug("REST request to save AddressDetails : {}", addressDetails);
        if (addressDetails.getId() != null) {
            throw new BadRequestAlertException("A new addressDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AddressDetails result = addressDetailsService.save(addressDetails);
        return ResponseEntity
            .created(new URI("/api/address-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /address-details/:id} : Updates an existing addressDetails.
     *
     * @param id the id of the addressDetails to save.
     * @param addressDetails the addressDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated addressDetails,
     * or with status {@code 400 (Bad Request)} if the addressDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the addressDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/address-details/{id}")
    public ResponseEntity<AddressDetails> updateAddressDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AddressDetails addressDetails
    ) throws URISyntaxException {
        log.debug("REST request to update AddressDetails : {}, {}", id, addressDetails);
        if (addressDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, addressDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!addressDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AddressDetails result = addressDetailsService.save(addressDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, addressDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /address-details/:id} : Partial updates given fields of an existing addressDetails, field will ignore if it is null
     *
     * @param id the id of the addressDetails to save.
     * @param addressDetails the addressDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated addressDetails,
     * or with status {@code 400 (Bad Request)} if the addressDetails is not valid,
     * or with status {@code 404 (Not Found)} if the addressDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the addressDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/address-details/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AddressDetails> partialUpdateAddressDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AddressDetails addressDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update AddressDetails partially : {}, {}", id, addressDetails);
        if (addressDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, addressDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!addressDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AddressDetails> result = addressDetailsService.partialUpdate(addressDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, addressDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /address-details} : get all the addressDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of addressDetails in body.
     */
    @GetMapping("/address-details")
    public List<AddressDetails> getAllAddressDetails() {
        log.debug("REST request to get all AddressDetails");
        return addressDetailsService.findAll();
    }

    /**
     * {@code GET  /address-details/:id} : get the "id" addressDetails.
     *
     * @param id the id of the addressDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the addressDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/address-details/{id}")
    public ResponseEntity<AddressDetails> getAddressDetails(@PathVariable Long id) {
        log.debug("REST request to get AddressDetails : {}", id);
        Optional<AddressDetails> addressDetails = addressDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(addressDetails);
    }

    /**
     * {@code DELETE  /address-details/:id} : delete the "id" addressDetails.
     *
     * @param id the id of the addressDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/address-details/{id}")
    public ResponseEntity<Void> deleteAddressDetails(@PathVariable Long id) {
        log.debug("REST request to delete AddressDetails : {}", id);
        addressDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
