package com.gracefl.propfirm.repository;

import com.gracefl.propfirm.domain.Mt4Trade;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Mt4Trade entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Mt4TradeRepository extends JpaRepository<Mt4Trade, Long>, JpaSpecificationExecutor<Mt4Trade> {}
