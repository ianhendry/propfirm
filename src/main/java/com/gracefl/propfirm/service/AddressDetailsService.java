package com.gracefl.propfirm.service;

import com.gracefl.propfirm.domain.AddressDetails;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link AddressDetails}.
 */
public interface AddressDetailsService {
    /**
     * Save a addressDetails.
     *
     * @param addressDetails the entity to save.
     * @return the persisted entity.
     */
    AddressDetails save(AddressDetails addressDetails);

    /**
     * Partially updates a addressDetails.
     *
     * @param addressDetails the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AddressDetails> partialUpdate(AddressDetails addressDetails);

    /**
     * Get all the addressDetails.
     *
     * @return the list of entities.
     */
    List<AddressDetails> findAll();

    /**
     * Get the "id" addressDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AddressDetails> findOne(Long id);

    /**
     * Delete the "id" addressDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
