package com.gracefl.propfirm.service;

import com.gracefl.propfirm.domain.TradeJournalPost;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TradeJournalPost}.
 */
public interface TradeJournalPostService {
    /**
     * Save a tradeJournalPost.
     *
     * @param tradeJournalPost the entity to save.
     * @return the persisted entity.
     */
    TradeJournalPost save(TradeJournalPost tradeJournalPost);

    /**
     * Partially updates a tradeJournalPost.
     *
     * @param tradeJournalPost the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TradeJournalPost> partialUpdate(TradeJournalPost tradeJournalPost);

    /**
     * Get all the tradeJournalPosts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TradeJournalPost> findAll(Pageable pageable);
    /**
     * Get all the TradeJournalPost where Mt4Trade is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<TradeJournalPost> findAllWhereMt4TradeIsNull();

    /**
     * Get the "id" tradeJournalPost.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TradeJournalPost> findOne(Long id);

    /**
     * Delete the "id" tradeJournalPost.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
