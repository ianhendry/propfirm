package com.gracefl.propfirm.service.impl;

import com.gracefl.propfirm.domain.SiteAccount;
import com.gracefl.propfirm.repository.SiteAccountRepository;
import com.gracefl.propfirm.service.SiteAccountService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SiteAccount}.
 */
@Service
@Transactional
public class SiteAccountServiceImpl implements SiteAccountService {

    private final Logger log = LoggerFactory.getLogger(SiteAccountServiceImpl.class);

    private final SiteAccountRepository siteAccountRepository;

    public SiteAccountServiceImpl(SiteAccountRepository siteAccountRepository) {
        this.siteAccountRepository = siteAccountRepository;
    }

    @Override
    public SiteAccount save(SiteAccount siteAccount) {
        log.debug("Request to save SiteAccount : {}", siteAccount);
        return siteAccountRepository.save(siteAccount);
    }

    @Override
    public Optional<SiteAccount> partialUpdate(SiteAccount siteAccount) {
        log.debug("Request to partially update SiteAccount : {}", siteAccount);

        return siteAccountRepository
            .findById(siteAccount.getId())
            .map(
                existingSiteAccount -> {
                    if (siteAccount.getAccountName() != null) {
                        existingSiteAccount.setAccountName(siteAccount.getAccountName());
                    }
                    if (siteAccount.getUserPicture() != null) {
                        existingSiteAccount.setUserPicture(siteAccount.getUserPicture());
                    }
                    if (siteAccount.getUserPictureContentType() != null) {
                        existingSiteAccount.setUserPictureContentType(siteAccount.getUserPictureContentType());
                    }
                    if (siteAccount.getUserBio() != null) {
                        existingSiteAccount.setUserBio(siteAccount.getUserBio());
                    }
                    if (siteAccount.getInActive() != null) {
                        existingSiteAccount.setInActive(siteAccount.getInActive());
                    }
                    if (siteAccount.getInActiveDate() != null) {
                        existingSiteAccount.setInActiveDate(siteAccount.getInActiveDate());
                    }

                    return existingSiteAccount;
                }
            )
            .map(siteAccountRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SiteAccount> findAll(Pageable pageable) {
        log.debug("Request to get all SiteAccounts");
        return siteAccountRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SiteAccount> findOne(Long id) {
        log.debug("Request to get SiteAccount : {}", id);
        return siteAccountRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SiteAccount : {}", id);
        siteAccountRepository.deleteById(id);
    }
}
