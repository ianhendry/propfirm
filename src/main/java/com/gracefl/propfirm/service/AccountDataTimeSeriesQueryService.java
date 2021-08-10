package com.gracefl.propfirm.service;

import com.gracefl.propfirm.domain.*; // for static metamodels
import com.gracefl.propfirm.domain.AccountDataTimeSeries;
import com.gracefl.propfirm.repository.AccountDataTimeSeriesRepository;
import com.gracefl.propfirm.service.criteria.AccountDataTimeSeriesCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AccountDataTimeSeries} entities in the database.
 * The main input is a {@link AccountDataTimeSeriesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AccountDataTimeSeries} or a {@link Page} of {@link AccountDataTimeSeries} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AccountDataTimeSeriesQueryService extends QueryService<AccountDataTimeSeries> {

    private final Logger log = LoggerFactory.getLogger(AccountDataTimeSeriesQueryService.class);

    private final AccountDataTimeSeriesRepository accountDataTimeSeriesRepository;

    public AccountDataTimeSeriesQueryService(AccountDataTimeSeriesRepository accountDataTimeSeriesRepository) {
        this.accountDataTimeSeriesRepository = accountDataTimeSeriesRepository;
    }

    /**
     * Return a {@link List} of {@link AccountDataTimeSeries} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AccountDataTimeSeries> findByCriteria(AccountDataTimeSeriesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AccountDataTimeSeries> specification = createSpecification(criteria);
        return accountDataTimeSeriesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AccountDataTimeSeries} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AccountDataTimeSeries> findByCriteria(AccountDataTimeSeriesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AccountDataTimeSeries> specification = createSpecification(criteria);
        return accountDataTimeSeriesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AccountDataTimeSeriesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AccountDataTimeSeries> specification = createSpecification(criteria);
        return accountDataTimeSeriesRepository.count(specification);
    }

    /**
     * Function to convert {@link AccountDataTimeSeriesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AccountDataTimeSeries> createSpecification(AccountDataTimeSeriesCriteria criteria) {
        Specification<AccountDataTimeSeries> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AccountDataTimeSeries_.id));
            }
            if (criteria.getDateStamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateStamp(), AccountDataTimeSeries_.dateStamp));
            }
            if (criteria.getAccountBalance() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAccountBalance(), AccountDataTimeSeries_.accountBalance));
            }
            if (criteria.getAccountEquity() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAccountEquity(), AccountDataTimeSeries_.accountEquity));
            }
            if (criteria.getAccountCredit() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAccountCredit(), AccountDataTimeSeries_.accountCredit));
            }
            if (criteria.getAccountFreeMargin() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAccountFreeMargin(), AccountDataTimeSeries_.accountFreeMargin));
            }
            if (criteria.getAccountStopoutLevel() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getAccountStopoutLevel(), AccountDataTimeSeries_.accountStopoutLevel)
                    );
            }
            if (criteria.getOpenLots() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOpenLots(), AccountDataTimeSeries_.openLots));
            }
            if (criteria.getOpenNumberOfTrades() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getOpenNumberOfTrades(), AccountDataTimeSeries_.openNumberOfTrades));
            }
            if (criteria.getMt4AccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMt4AccountId(),
                            root -> root.join(AccountDataTimeSeries_.mt4Account, JoinType.LEFT).get(Mt4Account_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
