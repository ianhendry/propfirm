package com.gracefl.propfirm.service.impl;

import com.gracefl.propfirm.domain.ChallengeType;
import com.gracefl.propfirm.repository.ChallengeTypeRepository;
import com.gracefl.propfirm.service.ChallengeTypeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ChallengeType}.
 */
@Service
@Transactional
public class ChallengeTypeServiceImpl implements ChallengeTypeService {

    private final Logger log = LoggerFactory.getLogger(ChallengeTypeServiceImpl.class);

    private final ChallengeTypeRepository challengeTypeRepository;

    public ChallengeTypeServiceImpl(ChallengeTypeRepository challengeTypeRepository) {
        this.challengeTypeRepository = challengeTypeRepository;
    }

    @Override
    public ChallengeType save(ChallengeType challengeType) {
        log.debug("Request to save ChallengeType : {}", challengeType);
        return challengeTypeRepository.save(challengeType);
    }

    @Override
    public Optional<ChallengeType> partialUpdate(ChallengeType challengeType) {
        log.debug("Request to partially update ChallengeType : {}", challengeType);

        return challengeTypeRepository
            .findById(challengeType.getId())
            .map(
                existingChallengeType -> {
                    if (challengeType.getChallengeTypeName() != null) {
                        existingChallengeType.setChallengeTypeName(challengeType.getChallengeTypeName());
                    }
                    if (challengeType.getPrice() != null) {
                        existingChallengeType.setPrice(challengeType.getPrice());
                    }
                    if (challengeType.getPriceContentType() != null) {
                        existingChallengeType.setPriceContentType(challengeType.getPriceContentType());
                    }
                    if (challengeType.getRefundAfterComplete() != null) {
                        existingChallengeType.setRefundAfterComplete(challengeType.getRefundAfterComplete());
                    }
                    if (challengeType.getAccountSize() != null) {
                        existingChallengeType.setAccountSize(challengeType.getAccountSize());
                    }
                    if (challengeType.getProfitTargetPhaseOne() != null) {
                        existingChallengeType.setProfitTargetPhaseOne(challengeType.getProfitTargetPhaseOne());
                    }
                    if (challengeType.getProfitTargetPhaseTwo() != null) {
                        existingChallengeType.setProfitTargetPhaseTwo(challengeType.getProfitTargetPhaseTwo());
                    }
                    if (challengeType.getDurationDaysPhaseOne() != null) {
                        existingChallengeType.setDurationDaysPhaseOne(challengeType.getDurationDaysPhaseOne());
                    }
                    if (challengeType.getDurationDaysPhaseTwo() != null) {
                        existingChallengeType.setDurationDaysPhaseTwo(challengeType.getDurationDaysPhaseTwo());
                    }
                    if (challengeType.getMaxDailyDrawdown() != null) {
                        existingChallengeType.setMaxDailyDrawdown(challengeType.getMaxDailyDrawdown());
                    }
                    if (challengeType.getMaxTotalDrawDown() != null) {
                        existingChallengeType.setMaxTotalDrawDown(challengeType.getMaxTotalDrawDown());
                    }
                    if (challengeType.getProfitSplitRatio() != null) {
                        existingChallengeType.setProfitSplitRatio(challengeType.getProfitSplitRatio());
                    }
                    if (challengeType.getFreeRetry() != null) {
                        existingChallengeType.setFreeRetry(challengeType.getFreeRetry());
                    }
                    if (challengeType.getUserBio() != null) {
                        existingChallengeType.setUserBio(challengeType.getUserBio());
                    }
                    if (challengeType.getInActive() != null) {
                        existingChallengeType.setInActive(challengeType.getInActive());
                    }
                    if (challengeType.getInActiveDate() != null) {
                        existingChallengeType.setInActiveDate(challengeType.getInActiveDate());
                    }

                    return existingChallengeType;
                }
            )
            .map(challengeTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChallengeType> findAll(Pageable pageable) {
        log.debug("Request to get all ChallengeTypes");
        return challengeTypeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChallengeType> findOne(Long id) {
        log.debug("Request to get ChallengeType : {}", id);
        return challengeTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ChallengeType : {}", id);
        challengeTypeRepository.deleteById(id);
    }
}
