package com.gracefl.propfirm.service;

import com.gracefl.propfirm.domain.Mt4Trade;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Mt4Trade}.
 */
public interface Mt4TradeService {
    /**
     * Save a mt4Trade.
     *
     * @param mt4Trade the entity to save.
     * @return the persisted entity.
     */
    Mt4Trade save(Mt4Trade mt4Trade);

    /**
     * Partially updates a mt4Trade.
     *
     * @param mt4Trade the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Mt4Trade> partialUpdate(Mt4Trade mt4Trade);

    /**
     * Get all the mt4Trades.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Mt4Trade> findAll(Pageable pageable);

    /**
     * Get the "id" mt4Trade.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Mt4Trade> findOne(Long id);

    /**
     * Delete the "id" mt4Trade.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
