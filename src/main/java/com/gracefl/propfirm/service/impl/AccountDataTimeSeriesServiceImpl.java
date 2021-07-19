package com.gracefl.propfirm.service.impl;

import com.gracefl.propfirm.domain.AccountDataTimeSeries;
import com.gracefl.propfirm.repository.AccountDataTimeSeriesRepository;
import com.gracefl.propfirm.service.AccountDataTimeSeriesService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AccountDataTimeSeries}.
 */
@Service
@Transactional
public class AccountDataTimeSeriesServiceImpl implements AccountDataTimeSeriesService {

    private final Logger log = LoggerFactory.getLogger(AccountDataTimeSeriesServiceImpl.class);

    private final AccountDataTimeSeriesRepository accountDataTimeSeriesRepository;

    public AccountDataTimeSeriesServiceImpl(AccountDataTimeSeriesRepository accountDataTimeSeriesRepository) {
        this.accountDataTimeSeriesRepository = accountDataTimeSeriesRepository;
    }

    @Override
    public AccountDataTimeSeries save(AccountDataTimeSeries accountDataTimeSeries) {
        log.debug("Request to save AccountDataTimeSeries : {}", accountDataTimeSeries);
        return accountDataTimeSeriesRepository.save(accountDataTimeSeries);
    }

    @Override
    public Optional<AccountDataTimeSeries> partialUpdate(AccountDataTimeSeries accountDataTimeSeries) {
        log.debug("Request to partially update AccountDataTimeSeries : {}", accountDataTimeSeries);

        return accountDataTimeSeriesRepository
            .findById(accountDataTimeSeries.getId())
            .map(
                existingAccountDataTimeSeries -> {
                    if (accountDataTimeSeries.getDateStamp() != null) {
                        existingAccountDataTimeSeries.setDateStamp(accountDataTimeSeries.getDateStamp());
                    }
                    if (accountDataTimeSeries.getAccountBalance() != null) {
                        existingAccountDataTimeSeries.setAccountBalance(accountDataTimeSeries.getAccountBalance());
                    }
                    if (accountDataTimeSeries.getAccountEquity() != null) {
                        existingAccountDataTimeSeries.setAccountEquity(accountDataTimeSeries.getAccountEquity());
                    }
                    if (accountDataTimeSeries.getAccountCredit() != null) {
                        existingAccountDataTimeSeries.setAccountCredit(accountDataTimeSeries.getAccountCredit());
                    }
                    if (accountDataTimeSeries.getAccountFreeMargin() != null) {
                        existingAccountDataTimeSeries.setAccountFreeMargin(accountDataTimeSeries.getAccountFreeMargin());
                    }
                    if (accountDataTimeSeries.getAccountStopoutLevel() != null) {
                        existingAccountDataTimeSeries.setAccountStopoutLevel(accountDataTimeSeries.getAccountStopoutLevel());
                    }
                    if (accountDataTimeSeries.getOpenLots() != null) {
                        existingAccountDataTimeSeries.setOpenLots(accountDataTimeSeries.getOpenLots());
                    }
                    if (accountDataTimeSeries.getOpenNumberOfTrades() != null) {
                        existingAccountDataTimeSeries.setOpenNumberOfTrades(accountDataTimeSeries.getOpenNumberOfTrades());
                    }

                    return existingAccountDataTimeSeries;
                }
            )
            .map(accountDataTimeSeriesRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDataTimeSeries> findAll() {
        log.debug("Request to get all AccountDataTimeSeries");
        return accountDataTimeSeriesRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountDataTimeSeries> findOne(Long id) {
        log.debug("Request to get AccountDataTimeSeries : {}", id);
        return accountDataTimeSeriesRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccountDataTimeSeries : {}", id);
        accountDataTimeSeriesRepository.deleteById(id);
    }
}
