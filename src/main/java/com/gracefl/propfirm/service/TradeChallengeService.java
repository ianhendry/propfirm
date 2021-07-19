package com.gracefl.propfirm.service;

import com.gracefl.propfirm.domain.TradeChallenge;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TradeChallenge}.
 */
public interface TradeChallengeService {
    /**
     * Save a tradeChallenge.
     *
     * @param tradeChallenge the entity to save.
     * @return the persisted entity.
     */
    TradeChallenge save(TradeChallenge tradeChallenge);

    /**
     * Partially updates a tradeChallenge.
     *
     * @param tradeChallenge the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TradeChallenge> partialUpdate(TradeChallenge tradeChallenge);

    /**
     * Get all the tradeChallenges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TradeChallenge> findAll(Pageable pageable);

    /**
     * Get the "id" tradeChallenge.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TradeChallenge> findOne(Long id);

    /**
     * Delete the "id" tradeChallenge.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
