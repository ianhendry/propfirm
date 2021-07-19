package com.gracefl.propfirm.service;

import com.gracefl.propfirm.domain.ChallengeType;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ChallengeType}.
 */
public interface ChallengeTypeService {
    /**
     * Save a challengeType.
     *
     * @param challengeType the entity to save.
     * @return the persisted entity.
     */
    ChallengeType save(ChallengeType challengeType);

    /**
     * Partially updates a challengeType.
     *
     * @param challengeType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ChallengeType> partialUpdate(ChallengeType challengeType);

    /**
     * Get all the challengeTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ChallengeType> findAll(Pageable pageable);

    /**
     * Get the "id" challengeType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChallengeType> findOne(Long id);

    /**
     * Delete the "id" challengeType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
