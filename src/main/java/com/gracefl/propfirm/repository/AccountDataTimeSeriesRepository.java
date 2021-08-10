package com.gracefl.propfirm.repository;

import com.gracefl.propfirm.domain.AccountDataTimeSeries;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AccountDataTimeSeries entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountDataTimeSeriesRepository
    extends JpaRepository<AccountDataTimeSeries, Long>, JpaSpecificationExecutor<AccountDataTimeSeries> {}
