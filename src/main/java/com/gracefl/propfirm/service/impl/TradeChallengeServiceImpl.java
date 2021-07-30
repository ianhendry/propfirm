package com.gracefl.propfirm.service.impl;

import com.gracefl.propfirm.domain.TradeChallenge;
import com.gracefl.propfirm.repository.TradeChallengeRepository;
import com.gracefl.propfirm.service.TradeChallengeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TradeChallenge}.
 */
@Service
@Transactional
public class TradeChallengeServiceImpl implements TradeChallengeService {

    private final Logger log = LoggerFactory.getLogger(TradeChallengeServiceImpl.class);

    private final TradeChallengeRepository tradeChallengeRepository;

    public TradeChallengeServiceImpl(TradeChallengeRepository tradeChallengeRepository) {
        this.tradeChallengeRepository = tradeChallengeRepository;
    }

    @Override
    public TradeChallenge save(TradeChallenge tradeChallenge) {
        log.debug("Request to save TradeChallenge : {}", tradeChallenge);
        return tradeChallengeRepository.save(tradeChallenge);
    }

    @Override
    public Optional<TradeChallenge> partialUpdate(TradeChallenge tradeChallenge) {
        log.debug("Request to partially update TradeChallenge : {}", tradeChallenge);

        return tradeChallengeRepository
            .findById(tradeChallenge.getId())
            .map(
                existingTradeChallenge -> {
                    if (tradeChallenge.getTradeChallengeName() != null) {
                        existingTradeChallenge.setTradeChallengeName(tradeChallenge.getTradeChallengeName());
                    }
                    if (tradeChallenge.getStartDate() != null) {
                        existingTradeChallenge.setStartDate(tradeChallenge.getStartDate());
                    }
                    if (tradeChallenge.getRunningMaxTotalDrawdown() != null) {
                        existingTradeChallenge.setRunningMaxTotalDrawdown(tradeChallenge.getRunningMaxTotalDrawdown());
                    }
                    if (tradeChallenge.getRunningMaxDailyDrawdown() != null) {
                        existingTradeChallenge.setRunningMaxDailyDrawdown(tradeChallenge.getRunningMaxDailyDrawdown());
                    }
                    if (tradeChallenge.getRulesViolated() != null) {
                        existingTradeChallenge.setRulesViolated(tradeChallenge.getRulesViolated());
                    }
                    if (tradeChallenge.getRuleViolated() != null) {
                        existingTradeChallenge.setRuleViolated(tradeChallenge.getRuleViolated());
                    }
                    if (tradeChallenge.getRuleViolatedDate() != null) {
                        existingTradeChallenge.setRuleViolatedDate(tradeChallenge.getRuleViolatedDate());
                    }
                    if (tradeChallenge.getMaxTotalDrawdown() != null) {
                        existingTradeChallenge.setMaxTotalDrawdown(tradeChallenge.getMaxTotalDrawdown());
                    }
                    if (tradeChallenge.getMaxDailyDrawdown() != null) {
                        existingTradeChallenge.setMaxDailyDrawdown(tradeChallenge.getMaxDailyDrawdown());
                    }
                    if (tradeChallenge.getLastDailyResetDate() != null) {
                        existingTradeChallenge.setLastDailyResetDate(tradeChallenge.getLastDailyResetDate());
                    }
                    if (tradeChallenge.getEndDate() != null) {
                        existingTradeChallenge.setEndDate(tradeChallenge.getEndDate());
                    }

                    return existingTradeChallenge;
                }
            )
            .map(tradeChallengeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TradeChallenge> findAll(Pageable pageable) {
        log.debug("Request to get all TradeChallenges");
        return tradeChallengeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TradeChallenge> findOne(Long id) {
        log.debug("Request to get TradeChallenge : {}", id);
        return tradeChallengeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TradeChallenge : {}", id);
        tradeChallengeRepository.deleteById(id);
    }
}
