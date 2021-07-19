package com.gracefl.propfirm.repository;

import com.gracefl.propfirm.domain.AddressDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AddressDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressDetailsRepository extends JpaRepository<AddressDetails, Long> {}
