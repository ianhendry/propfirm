package com.gracefl.propfirm.repository;

import com.gracefl.propfirm.domain.ChallengeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ChallengeType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChallengeTypeRepository extends JpaRepository<ChallengeType, Long> {}
