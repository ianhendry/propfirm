package com.gracefl.propfirm.service;

import com.gracefl.propfirm.domain.AccountDataTimeSeries;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link AccountDataTimeSeries}.
 */
public interface AccountDataTimeSeriesService {
    /**
     * Save a accountDataTimeSeries.
     *
     * @param accountDataTimeSeries the entity to save.
     * @return the persisted entity.
     */
    AccountDataTimeSeries save(AccountDataTimeSeries accountDataTimeSeries);

    /**
     * Partially updates a accountDataTimeSeries.
     *
     * @param accountDataTimeSeries the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccountDataTimeSeries> partialUpdate(AccountDataTimeSeries accountDataTimeSeries);

    /**
     * Get all the accountDataTimeSeries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountDataTimeSeries> findAll(Pageable pageable);

    /**
     * Get the "id" accountDataTimeSeries.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccountDataTimeSeries> findOne(Long id);

    /**
     * Delete the "id" accountDataTimeSeries.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
