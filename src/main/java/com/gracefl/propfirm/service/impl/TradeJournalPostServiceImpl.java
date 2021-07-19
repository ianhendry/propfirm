package com.gracefl.propfirm.service.impl;

import com.gracefl.propfirm.domain.TradeJournalPost;
import com.gracefl.propfirm.repository.TradeJournalPostRepository;
import com.gracefl.propfirm.service.TradeJournalPostService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TradeJournalPost}.
 */
@Service
@Transactional
public class TradeJournalPostServiceImpl implements TradeJournalPostService {

    private final Logger log = LoggerFactory.getLogger(TradeJournalPostServiceImpl.class);

    private final TradeJournalPostRepository tradeJournalPostRepository;

    public TradeJournalPostServiceImpl(TradeJournalPostRepository tradeJournalPostRepository) {
        this.tradeJournalPostRepository = tradeJournalPostRepository;
    }

    @Override
    public TradeJournalPost save(TradeJournalPost tradeJournalPost) {
        log.debug("Request to save TradeJournalPost : {}", tradeJournalPost);
        return tradeJournalPostRepository.save(tradeJournalPost);
    }

    @Override
    public Optional<TradeJournalPost> partialUpdate(TradeJournalPost tradeJournalPost) {
        log.debug("Request to partially update TradeJournalPost : {}", tradeJournalPost);

        return tradeJournalPostRepository
            .findById(tradeJournalPost.getId())
            .map(
                existingTradeJournalPost -> {
                    if (tradeJournalPost.getPostTitle() != null) {
                        existingTradeJournalPost.setPostTitle(tradeJournalPost.getPostTitle());
                    }
                    if (tradeJournalPost.getDateAdded() != null) {
                        existingTradeJournalPost.setDateAdded(tradeJournalPost.getDateAdded());
                    }
                    if (tradeJournalPost.getThoughtsOnPsychology() != null) {
                        existingTradeJournalPost.setThoughtsOnPsychology(tradeJournalPost.getThoughtsOnPsychology());
                    }
                    if (tradeJournalPost.getThoughtsOnTradeProcessAccuracy() != null) {
                        existingTradeJournalPost.setThoughtsOnTradeProcessAccuracy(tradeJournalPost.getThoughtsOnTradeProcessAccuracy());
                    }
                    if (tradeJournalPost.getThoughtsOnAreasOfStrength() != null) {
                        existingTradeJournalPost.setThoughtsOnAreasOfStrength(tradeJournalPost.getThoughtsOnAreasOfStrength());
                    }
                    if (tradeJournalPost.getThoughtsOnAreasForImprovement() != null) {
                        existingTradeJournalPost.setThoughtsOnAreasForImprovement(tradeJournalPost.getThoughtsOnAreasForImprovement());
                    }
                    if (tradeJournalPost.getAreaOfFocusForTomorrow() != null) {
                        existingTradeJournalPost.setAreaOfFocusForTomorrow(tradeJournalPost.getAreaOfFocusForTomorrow());
                    }
                    if (tradeJournalPost.getMakePublicVisibleOnSite() != null) {
                        existingTradeJournalPost.setMakePublicVisibleOnSite(tradeJournalPost.getMakePublicVisibleOnSite());
                    }
                    if (tradeJournalPost.getAnyMedia() != null) {
                        existingTradeJournalPost.setAnyMedia(tradeJournalPost.getAnyMedia());
                    }
                    if (tradeJournalPost.getAnyMediaContentType() != null) {
                        existingTradeJournalPost.setAnyMediaContentType(tradeJournalPost.getAnyMediaContentType());
                    }
                    if (tradeJournalPost.getAnyImage() != null) {
                        existingTradeJournalPost.setAnyImage(tradeJournalPost.getAnyImage());
                    }
                    if (tradeJournalPost.getAnyImageContentType() != null) {
                        existingTradeJournalPost.setAnyImageContentType(tradeJournalPost.getAnyImageContentType());
                    }

                    return existingTradeJournalPost;
                }
            )
            .map(tradeJournalPostRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TradeJournalPost> findAll(Pageable pageable) {
        log.debug("Request to get all TradeJournalPosts");
        return tradeJournalPostRepository.findAll(pageable);
    }

    /**
     *  Get all the tradeJournalPosts where Mt4Trade is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TradeJournalPost> findAllWhereMt4TradeIsNull() {
        log.debug("Request to get all tradeJournalPosts where Mt4Trade is null");
        return StreamSupport
            .stream(tradeJournalPostRepository.findAll().spliterator(), false)
            .filter(tradeJournalPost -> tradeJournalPost.getMt4Trade() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TradeJournalPost> findOne(Long id) {
        log.debug("Request to get TradeJournalPost : {}", id);
        return tradeJournalPostRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TradeJournalPost : {}", id);
        tradeJournalPostRepository.deleteById(id);
    }
}
