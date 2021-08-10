package com.gracefl.propfirm.service;

import com.gracefl.propfirm.domain.*; // for static metamodels
import com.gracefl.propfirm.domain.TradeJournalPost;
import com.gracefl.propfirm.repository.TradeJournalPostRepository;
import com.gracefl.propfirm.service.criteria.TradeJournalPostCriteria;
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
 * Service for executing complex queries for {@link TradeJournalPost} entities in the database.
 * The main input is a {@link TradeJournalPostCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TradeJournalPost} or a {@link Page} of {@link TradeJournalPost} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TradeJournalPostQueryService extends QueryService<TradeJournalPost> {

    private final Logger log = LoggerFactory.getLogger(TradeJournalPostQueryService.class);

    private final TradeJournalPostRepository tradeJournalPostRepository;

    public TradeJournalPostQueryService(TradeJournalPostRepository tradeJournalPostRepository) {
        this.tradeJournalPostRepository = tradeJournalPostRepository;
    }

    /**
     * Return a {@link List} of {@link TradeJournalPost} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TradeJournalPost> findByCriteria(TradeJournalPostCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TradeJournalPost> specification = createSpecification(criteria);
        return tradeJournalPostRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TradeJournalPost} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TradeJournalPost> findByCriteria(TradeJournalPostCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TradeJournalPost> specification = createSpecification(criteria);
        return tradeJournalPostRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TradeJournalPostCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TradeJournalPost> specification = createSpecification(criteria);
        return tradeJournalPostRepository.count(specification);
    }

    /**
     * Function to convert {@link TradeJournalPostCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TradeJournalPost> createSpecification(TradeJournalPostCriteria criteria) {
        Specification<TradeJournalPost> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TradeJournalPost_.id));
            }
            if (criteria.getPostTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostTitle(), TradeJournalPost_.postTitle));
            }
            if (criteria.getDateAdded() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateAdded(), TradeJournalPost_.dateAdded));
            }
            if (criteria.getMakePublicVisibleOnSite() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getMakePublicVisibleOnSite(), TradeJournalPost_.makePublicVisibleOnSite));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(TradeJournalPost_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getMt4TradeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMt4TradeId(),
                            root -> root.join(TradeJournalPost_.mt4Trade, JoinType.LEFT).get(Mt4Trade_.id)
                        )
                    );
            }
            if (criteria.getUserCommentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUserCommentId(),
                            root -> root.join(TradeJournalPost_.userComments, JoinType.LEFT).get(UserComment_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
