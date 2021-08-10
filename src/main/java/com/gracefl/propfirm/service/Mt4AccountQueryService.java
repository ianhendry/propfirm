package com.gracefl.propfirm.service;

import com.gracefl.propfirm.domain.*; // for static metamodels
import com.gracefl.propfirm.domain.Mt4Account;
import com.gracefl.propfirm.repository.Mt4AccountRepository;
import com.gracefl.propfirm.service.criteria.Mt4AccountCriteria;
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
 * Service for executing complex queries for {@link Mt4Account} entities in the database.
 * The main input is a {@link Mt4AccountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Mt4Account} or a {@link Page} of {@link Mt4Account} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class Mt4AccountQueryService extends QueryService<Mt4Account> {

    private final Logger log = LoggerFactory.getLogger(Mt4AccountQueryService.class);

    private final Mt4AccountRepository mt4AccountRepository;

    public Mt4AccountQueryService(Mt4AccountRepository mt4AccountRepository) {
        this.mt4AccountRepository = mt4AccountRepository;
    }

    /**
     * Return a {@link List} of {@link Mt4Account} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Mt4Account> findByCriteria(Mt4AccountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Mt4Account> specification = createSpecification(criteria);
        return mt4AccountRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Mt4Account} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Mt4Account> findByCriteria(Mt4AccountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Mt4Account> specification = createSpecification(criteria);
        return mt4AccountRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(Mt4AccountCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Mt4Account> specification = createSpecification(criteria);
        return mt4AccountRepository.count(specification);
    }

    /**
     * Function to convert {@link Mt4AccountCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Mt4Account> createSpecification(Mt4AccountCriteria criteria) {
        Specification<Mt4Account> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Mt4Account_.id));
            }
            if (criteria.getAccountType() != null) {
                specification = specification.and(buildSpecification(criteria.getAccountType(), Mt4Account_.accountType));
            }
            if (criteria.getAccountBroker() != null) {
                specification = specification.and(buildSpecification(criteria.getAccountBroker(), Mt4Account_.accountBroker));
            }
            if (criteria.getAccountLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccountLogin(), Mt4Account_.accountLogin));
            }
            if (criteria.getAccountPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccountPassword(), Mt4Account_.accountPassword));
            }
            if (criteria.getAccountInvestorLogin() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAccountInvestorLogin(), Mt4Account_.accountInvestorLogin));
            }
            if (criteria.getAccountInvestorPassword() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAccountInvestorPassword(), Mt4Account_.accountInvestorPassword));
            }
            if (criteria.getAccountReal() != null) {
                specification = specification.and(buildSpecification(criteria.getAccountReal(), Mt4Account_.accountReal));
            }
            if (criteria.getAccountInfoDouble() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAccountInfoDouble(), Mt4Account_.accountInfoDouble));
            }
            if (criteria.getAccountInfoInteger() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAccountInfoInteger(), Mt4Account_.accountInfoInteger));
            }
            if (criteria.getAccountInfoString() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccountInfoString(), Mt4Account_.accountInfoString));
            }
            if (criteria.getAccountBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAccountBalance(), Mt4Account_.accountBalance));
            }
            if (criteria.getAccountCredit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAccountCredit(), Mt4Account_.accountCredit));
            }
            if (criteria.getAccountCompany() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccountCompany(), Mt4Account_.accountCompany));
            }
            if (criteria.getAccountCurrency() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccountCurrency(), Mt4Account_.accountCurrency));
            }
            if (criteria.getAccountEquity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAccountEquity(), Mt4Account_.accountEquity));
            }
            if (criteria.getAccountFreeMargin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAccountFreeMargin(), Mt4Account_.accountFreeMargin));
            }
            if (criteria.getAccountFreeMarginCheck() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAccountFreeMarginCheck(), Mt4Account_.accountFreeMarginCheck));
            }
            if (criteria.getAccountFreeMarginMode() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAccountFreeMarginMode(), Mt4Account_.accountFreeMarginMode));
            }
            if (criteria.getAccountLeverage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAccountLeverage(), Mt4Account_.accountLeverage));
            }
            if (criteria.getAccountMargin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAccountMargin(), Mt4Account_.accountMargin));
            }
            if (criteria.getAccountName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccountName(), Mt4Account_.accountName));
            }
            if (criteria.getAccountNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAccountNumber(), Mt4Account_.accountNumber));
            }
            if (criteria.getAccountProfit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAccountProfit(), Mt4Account_.accountProfit));
            }
            if (criteria.getAccountServer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccountServer(), Mt4Account_.accountServer));
            }
            if (criteria.getAccountStopoutLevel() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAccountStopoutLevel(), Mt4Account_.accountStopoutLevel));
            }
            if (criteria.getAccountStopoutMode() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAccountStopoutMode(), Mt4Account_.accountStopoutMode));
            }
            if (criteria.getInActive() != null) {
                specification = specification.and(buildSpecification(criteria.getInActive(), Mt4Account_.inActive));
            }
            if (criteria.getInActiveDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInActiveDate(), Mt4Account_.inActiveDate));
            }
            if (criteria.getTradeChallengeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTradeChallengeId(),
                            root -> root.join(Mt4Account_.tradeChallenge, JoinType.LEFT).get(TradeChallenge_.id)
                        )
                    );
            }
            if (criteria.getMt4TradeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMt4TradeId(),
                            root -> root.join(Mt4Account_.mt4Trades, JoinType.LEFT).get(Mt4Trade_.id)
                        )
                    );
            }
            if (criteria.getAccountDataTimeSeriesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAccountDataTimeSeriesId(),
                            root -> root.join(Mt4Account_.accountDataTimeSeries, JoinType.LEFT).get(AccountDataTimeSeries_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
