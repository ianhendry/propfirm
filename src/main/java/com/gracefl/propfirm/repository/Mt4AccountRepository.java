package com.gracefl.propfirm.repository;

import com.gracefl.propfirm.domain.Mt4Account;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Mt4Account entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Mt4AccountRepository extends JpaRepository<Mt4Account, Long>, JpaSpecificationExecutor<Mt4Account> {}
