package com.gracefl.propfirm.service.criteria;

import com.gracefl.propfirm.domain.enumeration.TRADEDIRECTION;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.gracefl.propfirm.domain.Mt4Trade} entity. This class is used
 * in {@link com.gracefl.propfirm.web.rest.Mt4TradeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /mt-4-trades?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class Mt4TradeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TRADEDIRECTION
     */
    public static class TRADEDIRECTIONFilter extends Filter<TRADEDIRECTION> {

        public TRADEDIRECTIONFilter() {}

        public TRADEDIRECTIONFilter(TRADEDIRECTIONFilter filter) {
            super(filter);
        }

        @Override
        public TRADEDIRECTIONFilter copy() {
            return new TRADEDIRECTIONFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter ticket;

    private InstantFilter openTime;

    private TRADEDIRECTIONFilter directionType;

    private DoubleFilter positionSize;

    private StringFilter symbol;

    private DoubleFilter openPrice;

    private DoubleFilter stopLossPrice;

    private DoubleFilter takeProfitPrice;

    private InstantFilter closeTime;

    private DoubleFilter closePrice;

    private DoubleFilter commission;

    private DoubleFilter taxes;

    private DoubleFilter swap;

    private DoubleFilter profit;

    private LongFilter tradeJournalPostId;

    private LongFilter mt4AccountId;

    private LongFilter instrumentId;

    public Mt4TradeCriteria() {}

    public Mt4TradeCriteria(Mt4TradeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.ticket = other.ticket == null ? null : other.ticket.copy();
        this.openTime = other.openTime == null ? null : other.openTime.copy();
        this.directionType = other.directionType == null ? null : other.directionType.copy();
        this.positionSize = other.positionSize == null ? null : other.positionSize.copy();
        this.symbol = other.symbol == null ? null : other.symbol.copy();
        this.openPrice = other.openPrice == null ? null : other.openPrice.copy();
        this.stopLossPrice = other.stopLossPrice == null ? null : other.stopLossPrice.copy();
        this.takeProfitPrice = other.takeProfitPrice == null ? null : other.takeProfitPrice.copy();
        this.closeTime = other.closeTime == null ? null : other.closeTime.copy();
        this.closePrice = other.closePrice == null ? null : other.closePrice.copy();
        this.commission = other.commission == null ? null : other.commission.copy();
        this.taxes = other.taxes == null ? null : other.taxes.copy();
        this.swap = other.swap == null ? null : other.swap.copy();
        this.profit = other.profit == null ? null : other.profit.copy();
        this.tradeJournalPostId = other.tradeJournalPostId == null ? null : other.tradeJournalPostId.copy();
        this.mt4AccountId = other.mt4AccountId == null ? null : other.mt4AccountId.copy();
        this.instrumentId = other.instrumentId == null ? null : other.instrumentId.copy();
    }

    @Override
    public Mt4TradeCriteria copy() {
        return new Mt4TradeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BigDecimalFilter getTicket() {
        return ticket;
    }

    public BigDecimalFilter ticket() {
        if (ticket == null) {
            ticket = new BigDecimalFilter();
        }
        return ticket;
    }

    public void setTicket(BigDecimalFilter ticket) {
        this.ticket = ticket;
    }

    public InstantFilter getOpenTime() {
        return openTime;
    }

    public InstantFilter openTime() {
        if (openTime == null) {
            openTime = new InstantFilter();
        }
        return openTime;
    }

    public void setOpenTime(InstantFilter openTime) {
        this.openTime = openTime;
    }

    public TRADEDIRECTIONFilter getDirectionType() {
        return directionType;
    }

    public TRADEDIRECTIONFilter directionType() {
        if (directionType == null) {
            directionType = new TRADEDIRECTIONFilter();
        }
        return directionType;
    }

    public void setDirectionType(TRADEDIRECTIONFilter directionType) {
        this.directionType = directionType;
    }

    public DoubleFilter getPositionSize() {
        return positionSize;
    }

    public DoubleFilter positionSize() {
        if (positionSize == null) {
            positionSize = new DoubleFilter();
        }
        return positionSize;
    }

    public void setPositionSize(DoubleFilter positionSize) {
        this.positionSize = positionSize;
    }

    public StringFilter getSymbol() {
        return symbol;
    }

    public StringFilter symbol() {
        if (symbol == null) {
            symbol = new StringFilter();
        }
        return symbol;
    }

    public void setSymbol(StringFilter symbol) {
        this.symbol = symbol;
    }

    public DoubleFilter getOpenPrice() {
        return openPrice;
    }

    public DoubleFilter openPrice() {
        if (openPrice == null) {
            openPrice = new DoubleFilter();
        }
        return openPrice;
    }

    public void setOpenPrice(DoubleFilter openPrice) {
        this.openPrice = openPrice;
    }

    public DoubleFilter getStopLossPrice() {
        return stopLossPrice;
    }

    public DoubleFilter stopLossPrice() {
        if (stopLossPrice == null) {
            stopLossPrice = new DoubleFilter();
        }
        return stopLossPrice;
    }

    public void setStopLossPrice(DoubleFilter stopLossPrice) {
        this.stopLossPrice = stopLossPrice;
    }

    public DoubleFilter getTakeProfitPrice() {
        return takeProfitPrice;
    }

    public DoubleFilter takeProfitPrice() {
        if (takeProfitPrice == null) {
            takeProfitPrice = new DoubleFilter();
        }
        return takeProfitPrice;
    }

    public void setTakeProfitPrice(DoubleFilter takeProfitPrice) {
        this.takeProfitPrice = takeProfitPrice;
    }

    public InstantFilter getCloseTime() {
        return closeTime;
    }

    public InstantFilter closeTime() {
        if (closeTime == null) {
            closeTime = new InstantFilter();
        }
        return closeTime;
    }

    public void setCloseTime(InstantFilter closeTime) {
        this.closeTime = closeTime;
    }

    public DoubleFilter getClosePrice() {
        return closePrice;
    }

    public DoubleFilter closePrice() {
        if (closePrice == null) {
            closePrice = new DoubleFilter();
        }
        return closePrice;
    }

    public void setClosePrice(DoubleFilter closePrice) {
        this.closePrice = closePrice;
    }

    public DoubleFilter getCommission() {
        return commission;
    }

    public DoubleFilter commission() {
        if (commission == null) {
            commission = new DoubleFilter();
        }
        return commission;
    }

    public void setCommission(DoubleFilter commission) {
        this.commission = commission;
    }

    public DoubleFilter getTaxes() {
        return taxes;
    }

    public DoubleFilter taxes() {
        if (taxes == null) {
            taxes = new DoubleFilter();
        }
        return taxes;
    }

    public void setTaxes(DoubleFilter taxes) {
        this.taxes = taxes;
    }

    public DoubleFilter getSwap() {
        return swap;
    }

    public DoubleFilter swap() {
        if (swap == null) {
            swap = new DoubleFilter();
        }
        return swap;
    }

    public void setSwap(DoubleFilter swap) {
        this.swap = swap;
    }

    public DoubleFilter getProfit() {
        return profit;
    }

    public DoubleFilter profit() {
        if (profit == null) {
            profit = new DoubleFilter();
        }
        return profit;
    }

    public void setProfit(DoubleFilter profit) {
        this.profit = profit;
    }

    public LongFilter getTradeJournalPostId() {
        return tradeJournalPostId;
    }

    public LongFilter tradeJournalPostId() {
        if (tradeJournalPostId == null) {
            tradeJournalPostId = new LongFilter();
        }
        return tradeJournalPostId;
    }

    public void setTradeJournalPostId(LongFilter tradeJournalPostId) {
        this.tradeJournalPostId = tradeJournalPostId;
    }

    public LongFilter getMt4AccountId() {
        return mt4AccountId;
    }

    public LongFilter mt4AccountId() {
        if (mt4AccountId == null) {
            mt4AccountId = new LongFilter();
        }
        return mt4AccountId;
    }

    public void setMt4AccountId(LongFilter mt4AccountId) {
        this.mt4AccountId = mt4AccountId;
    }

    public LongFilter getInstrumentId() {
        return instrumentId;
    }

    public LongFilter instrumentId() {
        if (instrumentId == null) {
            instrumentId = new LongFilter();
        }
        return instrumentId;
    }

    public void setInstrumentId(LongFilter instrumentId) {
        this.instrumentId = instrumentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Mt4TradeCriteria that = (Mt4TradeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(ticket, that.ticket) &&
            Objects.equals(openTime, that.openTime) &&
            Objects.equals(directionType, that.directionType) &&
            Objects.equals(positionSize, that.positionSize) &&
            Objects.equals(symbol, that.symbol) &&
            Objects.equals(openPrice, that.openPrice) &&
            Objects.equals(stopLossPrice, that.stopLossPrice) &&
            Objects.equals(takeProfitPrice, that.takeProfitPrice) &&
            Objects.equals(closeTime, that.closeTime) &&
            Objects.equals(closePrice, that.closePrice) &&
            Objects.equals(commission, that.commission) &&
            Objects.equals(taxes, that.taxes) &&
            Objects.equals(swap, that.swap) &&
            Objects.equals(profit, that.profit) &&
            Objects.equals(tradeJournalPostId, that.tradeJournalPostId) &&
            Objects.equals(mt4AccountId, that.mt4AccountId) &&
            Objects.equals(instrumentId, that.instrumentId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            ticket,
            openTime,
            directionType,
            positionSize,
            symbol,
            openPrice,
            stopLossPrice,
            takeProfitPrice,
            closeTime,
            closePrice,
            commission,
            taxes,
            swap,
            profit,
            tradeJournalPostId,
            mt4AccountId,
            instrumentId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mt4TradeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (ticket != null ? "ticket=" + ticket + ", " : "") +
            (openTime != null ? "openTime=" + openTime + ", " : "") +
            (directionType != null ? "directionType=" + directionType + ", " : "") +
            (positionSize != null ? "positionSize=" + positionSize + ", " : "") +
            (symbol != null ? "symbol=" + symbol + ", " : "") +
            (openPrice != null ? "openPrice=" + openPrice + ", " : "") +
            (stopLossPrice != null ? "stopLossPrice=" + stopLossPrice + ", " : "") +
            (takeProfitPrice != null ? "takeProfitPrice=" + takeProfitPrice + ", " : "") +
            (closeTime != null ? "closeTime=" + closeTime + ", " : "") +
            (closePrice != null ? "closePrice=" + closePrice + ", " : "") +
            (commission != null ? "commission=" + commission + ", " : "") +
            (taxes != null ? "taxes=" + taxes + ", " : "") +
            (swap != null ? "swap=" + swap + ", " : "") +
            (profit != null ? "profit=" + profit + ", " : "") +
            (tradeJournalPostId != null ? "tradeJournalPostId=" + tradeJournalPostId + ", " : "") +
            (mt4AccountId != null ? "mt4AccountId=" + mt4AccountId + ", " : "") +
            (instrumentId != null ? "instrumentId=" + instrumentId + ", " : "") +
            "}";
    }
}
