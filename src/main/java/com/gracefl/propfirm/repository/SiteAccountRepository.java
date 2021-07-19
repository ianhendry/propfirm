package com.gracefl.propfirm.repository;

import com.gracefl.propfirm.domain.SiteAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SiteAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SiteAccountRepository extends JpaRepository<SiteAccount, Long>, JpaSpecificationExecutor<SiteAccount> {}
