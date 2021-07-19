package com.gracefl.propfirm.service;

import com.gracefl.propfirm.domain.*; // for static metamodels
import com.gracefl.propfirm.domain.Mt4Trade;
import com.gracefl.propfirm.repository.Mt4TradeRepository;
import com.gracefl.propfirm.service.criteria.Mt4TradeCriteria;
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
 * Service for executing complex queries for {@link Mt4Trade} entities in the database.
 * The main input is a {@link Mt4TradeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Mt4Trade} or a {@link Page} of {@link Mt4Trade} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class Mt4TradeQueryService extends QueryService<Mt4Trade> {

    private final Logger log = LoggerFactory.getLogger(Mt4TradeQueryService.class);

    private final Mt4TradeRepository mt4TradeRepository;

    public Mt4TradeQueryService(Mt4TradeRepository mt4TradeRepository) {
        this.mt4TradeRepository = mt4TradeRepository;
    }

    /**
     * Return a {@link List} of {@link Mt4Trade} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Mt4Trade> findByCriteria(Mt4TradeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Mt4Trade> specification = createSpecification(criteria);
        return mt4TradeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Mt4Trade} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Mt4Trade> findByCriteria(Mt4TradeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Mt4Trade> specification = createSpecification(criteria);
        return mt4TradeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(Mt4TradeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Mt4Trade> specification = createSpecification(criteria);
        return mt4TradeRepository.count(specification);
    }

    /**
     * Function to convert {@link Mt4TradeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Mt4Trade> createSpecification(Mt4TradeCriteria criteria) {
        Specification<Mt4Trade> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Mt4Trade_.id));
            }
            if (criteria.getTicket() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTicket(), Mt4Trade_.ticket));
            }
            if (criteria.getOpenTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOpenTime(), Mt4Trade_.openTime));
            }
            if (criteria.getDirectionType() != null) {
                specification = specification.and(buildSpecification(criteria.getDirectionType(), Mt4Trade_.directionType));
            }
            if (criteria.getPositionSize() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPositionSize(), Mt4Trade_.positionSize));
            }
            if (criteria.getSymbol() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSymbol(), Mt4Trade_.symbol));
            }
            if (criteria.getOpenPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOpenPrice(), Mt4Trade_.openPrice));
            }
            if (criteria.getStopLossPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStopLossPrice(), Mt4Trade_.stopLossPrice));
            }
            if (criteria.getTakeProfitPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTakeProfitPrice(), Mt4Trade_.takeProfitPrice));
            }
            if (criteria.getCloseTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCloseTime(), Mt4Trade_.closeTime));
            }
            if (criteria.getClosePrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getClosePrice(), Mt4Trade_.closePrice));
            }
            if (criteria.getCommission() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCommission(), Mt4Trade_.commission));
            }
            if (criteria.getTaxes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTaxes(), Mt4Trade_.taxes));
            }
            if (criteria.getSwap() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSwap(), Mt4Trade_.swap));
            }
            if (criteria.getProfit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProfit(), Mt4Trade_.profit));
            }
            if (criteria.getTradeJournalPostId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTradeJournalPostId(),
                            root -> root.join(Mt4Trade_.tradeJournalPost, JoinType.LEFT).get(TradeJournalPost_.id)
                        )
                    );
            }
            if (criteria.getMt4AccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMt4AccountId(),
                            root -> root.join(Mt4Trade_.mt4Account, JoinType.LEFT).get(Mt4Account_.id)
                        )
                    );
            }
            if (criteria.getInstrumentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInstrumentId(),
                            root -> root.join(Mt4Trade_.instrument, JoinType.LEFT).get(Instrument_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
