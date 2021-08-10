package com.gracefl.propfirm.service.impl;

import com.gracefl.propfirm.domain.DailyAnalysisPost;
import com.gracefl.propfirm.repository.DailyAnalysisPostRepository;
import com.gracefl.propfirm.service.DailyAnalysisPostService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DailyAnalysisPost}.
 */
@Service
@Transactional
public class DailyAnalysisPostServiceImpl implements DailyAnalysisPostService {

    private final Logger log = LoggerFactory.getLogger(DailyAnalysisPostServiceImpl.class);

    private final DailyAnalysisPostRepository dailyAnalysisPostRepository;

    public DailyAnalysisPostServiceImpl(DailyAnalysisPostRepository dailyAnalysisPostRepository) {
        this.dailyAnalysisPostRepository = dailyAnalysisPostRepository;
    }

    @Override
    public DailyAnalysisPost save(DailyAnalysisPost dailyAnalysisPost) {
        log.debug("Request to save DailyAnalysisPost : {}", dailyAnalysisPost);
        return dailyAnalysisPostRepository.save(dailyAnalysisPost);
    }

    @Override
    public Optional<DailyAnalysisPost> partialUpdate(DailyAnalysisPost dailyAnalysisPost) {
        log.debug("Request to partially update DailyAnalysisPost : {}", dailyAnalysisPost);

        return dailyAnalysisPostRepository
            .findById(dailyAnalysisPost.getId())
            .map(
                existingDailyAnalysisPost -> {
                    if (dailyAnalysisPost.getPostTitle() != null) {
                        existingDailyAnalysisPost.setPostTitle(dailyAnalysisPost.getPostTitle());
                    }
                    if (dailyAnalysisPost.getDateAdded() != null) {
                        existingDailyAnalysisPost.setDateAdded(dailyAnalysisPost.getDateAdded());
                    }
                    if (dailyAnalysisPost.getBackgroundVolume() != null) {
                        existingDailyAnalysisPost.setBackgroundVolume(dailyAnalysisPost.getBackgroundVolume());
                    }
                    if (dailyAnalysisPost.getOverallThoughts() != null) {
                        existingDailyAnalysisPost.setOverallThoughts(dailyAnalysisPost.getOverallThoughts());
                    }
                    if (dailyAnalysisPost.getWeeklyChart() != null) {
                        existingDailyAnalysisPost.setWeeklyChart(dailyAnalysisPost.getWeeklyChart());
                    }
                    if (dailyAnalysisPost.getWeeklyChartContentType() != null) {
                        existingDailyAnalysisPost.setWeeklyChartContentType(dailyAnalysisPost.getWeeklyChartContentType());
                    }
                    if (dailyAnalysisPost.getDailyChart() != null) {
                        existingDailyAnalysisPost.setDailyChart(dailyAnalysisPost.getDailyChart());
                    }
                    if (dailyAnalysisPost.getDailyChartContentType() != null) {
                        existingDailyAnalysisPost.setDailyChartContentType(dailyAnalysisPost.getDailyChartContentType());
                    }
                    if (dailyAnalysisPost.getOneHrChart() != null) {
                        existingDailyAnalysisPost.setOneHrChart(dailyAnalysisPost.getOneHrChart());
                    }
                    if (dailyAnalysisPost.getOneHrChartContentType() != null) {
                        existingDailyAnalysisPost.setOneHrChartContentType(dailyAnalysisPost.getOneHrChartContentType());
                    }
                    if (dailyAnalysisPost.getPlanForToday() != null) {
                        existingDailyAnalysisPost.setPlanForToday(dailyAnalysisPost.getPlanForToday());
                    }
                    if (dailyAnalysisPost.getMakePublicVisibleOnSite() != null) {
                        existingDailyAnalysisPost.setMakePublicVisibleOnSite(dailyAnalysisPost.getMakePublicVisibleOnSite());
                    }

                    return existingDailyAnalysisPost;
                }
            )
            .map(dailyAnalysisPostRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DailyAnalysisPost> findAll(Pageable pageable) {
        log.debug("Request to get all DailyAnalysisPosts");
        return dailyAnalysisPostRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DailyAnalysisPost> findOne(Long id) {
        log.debug("Request to get DailyAnalysisPost : {}", id);
        return dailyAnalysisPostRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DailyAnalysisPost : {}", id);
        dailyAnalysisPostRepository.deleteById(id);
    }
}
