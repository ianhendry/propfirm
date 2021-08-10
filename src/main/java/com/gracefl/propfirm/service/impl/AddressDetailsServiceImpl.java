package com.gracefl.propfirm.service.impl;

import com.gracefl.propfirm.domain.AddressDetails;
import com.gracefl.propfirm.repository.AddressDetailsRepository;
import com.gracefl.propfirm.service.AddressDetailsService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AddressDetails}.
 */
@Service
@Transactional
public class AddressDetailsServiceImpl implements AddressDetailsService {

    private final Logger log = LoggerFactory.getLogger(AddressDetailsServiceImpl.class);

    private final AddressDetailsRepository addressDetailsRepository;

    public AddressDetailsServiceImpl(AddressDetailsRepository addressDetailsRepository) {
        this.addressDetailsRepository = addressDetailsRepository;
    }

    @Override
    public AddressDetails save(AddressDetails addressDetails) {
        log.debug("Request to save AddressDetails : {}", addressDetails);
        return addressDetailsRepository.save(addressDetails);
    }

    @Override
    public Optional<AddressDetails> partialUpdate(AddressDetails addressDetails) {
        log.debug("Request to partially update AddressDetails : {}", addressDetails);

        return addressDetailsRepository
            .findById(addressDetails.getId())
            .map(
                existingAddressDetails -> {
                    if (addressDetails.getContactName() != null) {
                        existingAddressDetails.setContactName(addressDetails.getContactName());
                    }
                    if (addressDetails.getAddress1() != null) {
                        existingAddressDetails.setAddress1(addressDetails.getAddress1());
                    }
                    if (addressDetails.getAddress2() != null) {
                        existingAddressDetails.setAddress2(addressDetails.getAddress2());
                    }
                    if (addressDetails.getAddress3() != null) {
                        existingAddressDetails.setAddress3(addressDetails.getAddress3());
                    }
                    if (addressDetails.getAddress4() != null) {
                        existingAddressDetails.setAddress4(addressDetails.getAddress4());
                    }
                    if (addressDetails.getAddress5() != null) {
                        existingAddressDetails.setAddress5(addressDetails.getAddress5());
                    }
                    if (addressDetails.getAddress6() != null) {
                        existingAddressDetails.setAddress6(addressDetails.getAddress6());
                    }
                    if (addressDetails.getDialCode() != null) {
                        existingAddressDetails.setDialCode(addressDetails.getDialCode());
                    }
                    if (addressDetails.getPhoneNumber() != null) {
                        existingAddressDetails.setPhoneNumber(addressDetails.getPhoneNumber());
                    }
                    if (addressDetails.getMessengerId() != null) {
                        existingAddressDetails.setMessengerId(addressDetails.getMessengerId());
                    }
                    if (addressDetails.getDateAdded() != null) {
                        existingAddressDetails.setDateAdded(addressDetails.getDateAdded());
                    }
                    if (addressDetails.getInActive() != null) {
                        existingAddressDetails.setInActive(addressDetails.getInActive());
                    }
                    if (addressDetails.getInActiveDate() != null) {
                        existingAddressDetails.setInActiveDate(addressDetails.getInActiveDate());
                    }

                    return existingAddressDetails;
                }
            )
            .map(addressDetailsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressDetails> findAll() {
        log.debug("Request to get all AddressDetails");
        return addressDetailsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AddressDetails> findOne(Long id) {
        log.debug("Request to get AddressDetails : {}", id);
        return addressDetailsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AddressDetails : {}", id);
        addressDetailsRepository.deleteById(id);
    }
}
