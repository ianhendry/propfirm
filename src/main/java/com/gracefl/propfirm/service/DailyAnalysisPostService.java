package com.gracefl.propfirm.service;

import com.gracefl.propfirm.domain.DailyAnalysisPost;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link DailyAnalysisPost}.
 */
public interface DailyAnalysisPostService {
    /**
     * Save a dailyAnalysisPost.
     *
     * @param dailyAnalysisPost the entity to save.
     * @return the persisted entity.
     */
    DailyAnalysisPost save(DailyAnalysisPost dailyAnalysisPost);

    /**
     * Partially updates a dailyAnalysisPost.
     *
     * @param dailyAnalysisPost the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DailyAnalysisPost> partialUpdate(DailyAnalysisPost dailyAnalysisPost);

    /**
     * Get all the dailyAnalysisPosts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DailyAnalysisPost> findAll(Pageable pageable);

    /**
     * Get the "id" dailyAnalysisPost.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DailyAnalysisPost> findOne(Long id);

    /**
     * Delete the "id" dailyAnalysisPost.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
