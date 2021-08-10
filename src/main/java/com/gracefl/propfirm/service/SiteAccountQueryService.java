package com.gracefl.propfirm.service;

import com.gracefl.propfirm.domain.*; // for static metamodels
import com.gracefl.propfirm.domain.SiteAccount;
import com.gracefl.propfirm.repository.SiteAccountRepository;
import com.gracefl.propfirm.service.criteria.SiteAccountCriteria;
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
 * Service for executing complex queries for {@link SiteAccount} entities in the database.
 * The main input is a {@link SiteAccountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SiteAccount} or a {@link Page} of {@link SiteAccount} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SiteAccountQueryService extends QueryService<SiteAccount> {

    private final Logger log = LoggerFactory.getLogger(SiteAccountQueryService.class);

    private final SiteAccountRepository siteAccountRepository;

    public SiteAccountQueryService(SiteAccountRepository siteAccountRepository) {
        this.siteAccountRepository = siteAccountRepository;
    }

    /**
     * Return a {@link List} of {@link SiteAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SiteAccount> findByCriteria(SiteAccountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SiteAccount> specification = createSpecification(criteria);
        return siteAccountRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link SiteAccount} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SiteAccount> findByCriteria(SiteAccountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SiteAccount> specification = createSpecification(criteria);
        return siteAccountRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SiteAccountCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SiteAccount> specification = createSpecification(criteria);
        return siteAccountRepository.count(specification);
    }

    /**
     * Function to convert {@link SiteAccountCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SiteAccount> createSpecification(SiteAccountCriteria criteria) {
        Specification<SiteAccount> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SiteAccount_.id));
            }
            if (criteria.getAccountName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccountName(), SiteAccount_.accountName));
            }
            if (criteria.getInActive() != null) {
                specification = specification.and(buildSpecification(criteria.getInActive(), SiteAccount_.inActive));
            }
            if (criteria.getInActiveDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInActiveDate(), SiteAccount_.inActiveDate));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(SiteAccount_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getAddressDetailsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAddressDetailsId(),
                            root -> root.join(SiteAccount_.addressDetails, JoinType.LEFT).get(AddressDetails_.id)
                        )
                    );
            }
            if (criteria.getTradeChallengeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTradeChallengeId(),
                            root -> root.join(SiteAccount_.tradeChallenges, JoinType.LEFT).get(TradeChallenge_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
