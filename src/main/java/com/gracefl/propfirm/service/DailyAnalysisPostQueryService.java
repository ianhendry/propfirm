package com.gracefl.propfirm.service;

import com.gracefl.propfirm.domain.*; // for static metamodels
import com.gracefl.propfirm.domain.DailyAnalysisPost;
import com.gracefl.propfirm.repository.DailyAnalysisPostRepository;
import com.gracefl.propfirm.service.criteria.DailyAnalysisPostCriteria;
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
 * Service for executing complex queries for {@link DailyAnalysisPost} entities in the database.
 * The main input is a {@link DailyAnalysisPostCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DailyAnalysisPost} or a {@link Page} of {@link DailyAnalysisPost} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DailyAnalysisPostQueryService extends QueryService<DailyAnalysisPost> {

    private final Logger log = LoggerFactory.getLogger(DailyAnalysisPostQueryService.class);

    private final DailyAnalysisPostRepository dailyAnalysisPostRepository;

    public DailyAnalysisPostQueryService(DailyAnalysisPostRepository dailyAnalysisPostRepository) {
        this.dailyAnalysisPostRepository = dailyAnalysisPostRepository;
    }

    /**
     * Return a {@link List} of {@link DailyAnalysisPost} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DailyAnalysisPost> findByCriteria(DailyAnalysisPostCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DailyAnalysisPost> specification = createSpecification(criteria);
        return dailyAnalysisPostRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DailyAnalysisPost} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DailyAnalysisPost> findByCriteria(DailyAnalysisPostCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DailyAnalysisPost> specification = createSpecification(criteria);
        return dailyAnalysisPostRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DailyAnalysisPostCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DailyAnalysisPost> specification = createSpecification(criteria);
        return dailyAnalysisPostRepository.count(specification);
    }

    /**
     * Function to convert {@link DailyAnalysisPostCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DailyAnalysisPost> createSpecification(DailyAnalysisPostCriteria criteria) {
        Specification<DailyAnalysisPost> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DailyAnalysisPost_.id));
            }
            if (criteria.getPostTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostTitle(), DailyAnalysisPost_.postTitle));
            }
            if (criteria.getDateAdded() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateAdded(), DailyAnalysisPost_.dateAdded));
            }
            if (criteria.getMakePublicVisibleOnSite() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMakePublicVisibleOnSite(), DailyAnalysisPost_.makePublicVisibleOnSite)
                    );
            }
            if (criteria.getInstrumentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInstrumentId(),
                            root -> root.join(DailyAnalysisPost_.instrument, JoinType.LEFT).get(Instrument_.id)
                        )
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(DailyAnalysisPost_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getUserCommentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUserCommentId(),
                            root -> root.join(DailyAnalysisPost_.userComments, JoinType.LEFT).get(UserComment_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
