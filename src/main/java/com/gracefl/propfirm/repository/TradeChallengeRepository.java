package com.gracefl.propfirm.repository;

import com.gracefl.propfirm.domain.TradeChallenge;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TradeChallenge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TradeChallengeRepository extends JpaRepository<TradeChallenge, Long> {}
