package com.gracefl.propfirm.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.gracefl.propfirm.domain.AccountDataTimeSeries} entity. This class is used
 * in {@link com.gracefl.propfirm.web.rest.AccountDataTimeSeriesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /account-data-time-series?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AccountDataTimeSeriesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dateStamp;

    private DoubleFilter accountBalance;

    private DoubleFilter accountEquity;

    private DoubleFilter accountCredit;

    private DoubleFilter accountFreeMargin;

    private IntegerFilter accountStopoutLevel;

    private DoubleFilter openLots;

    private IntegerFilter openNumberOfTrades;

    private LongFilter mt4AccountId;

    public AccountDataTimeSeriesCriteria() {}

    public AccountDataTimeSeriesCriteria(AccountDataTimeSeriesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dateStamp = other.dateStamp == null ? null : other.dateStamp.copy();
        this.accountBalance = other.accountBalance == null ? null : other.accountBalance.copy();
        this.accountEquity = other.accountEquity == null ? null : other.accountEquity.copy();
        this.accountCredit = other.accountCredit == null ? null : other.accountCredit.copy();
        this.accountFreeMargin = other.accountFreeMargin == null ? null : other.accountFreeMargin.copy();
        this.accountStopoutLevel = other.accountStopoutLevel == null ? null : other.accountStopoutLevel.copy();
        this.openLots = other.openLots == null ? null : other.openLots.copy();
        this.openNumberOfTrades = other.openNumberOfTrades == null ? null : other.openNumberOfTrades.copy();
        this.mt4AccountId = other.mt4AccountId == null ? null : other.mt4AccountId.copy();
    }

    @Override
    public AccountDataTimeSeriesCriteria copy() {
        return new AccountDataTimeSeriesCriteria(this);
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

    public InstantFilter getDateStamp() {
        return dateStamp;
    }

    public InstantFilter dateStamp() {
        if (dateStamp == null) {
            dateStamp = new InstantFilter();
        }
        return dateStamp;
    }

    public void setDateStamp(InstantFilter dateStamp) {
        this.dateStamp = dateStamp;
    }

    public DoubleFilter getAccountBalance() {
        return accountBalance;
    }

    public DoubleFilter accountBalance() {
        if (accountBalance == null) {
            accountBalance = new DoubleFilter();
        }
        return accountBalance;
    }

    public void setAccountBalance(DoubleFilter accountBalance) {
        this.accountBalance = accountBalance;
    }

    public DoubleFilter getAccountEquity() {
        return accountEquity;
    }

    public DoubleFilter accountEquity() {
        if (accountEquity == null) {
            accountEquity = new DoubleFilter();
        }
        return accountEquity;
    }

    public void setAccountEquity(DoubleFilter accountEquity) {
        this.accountEquity = accountEquity;
    }

    public DoubleFilter getAccountCredit() {
        return accountCredit;
    }

    public DoubleFilter accountCredit() {
        if (accountCredit == null) {
            accountCredit = new DoubleFilter();
        }
        return accountCredit;
    }

    public void setAccountCredit(DoubleFilter accountCredit) {
        this.accountCredit = accountCredit;
    }

    public DoubleFilter getAccountFreeMargin() {
        return accountFreeMargin;
    }

    public DoubleFilter accountFreeMargin() {
        if (accountFreeMargin == null) {
            accountFreeMargin = new DoubleFilter();
        }
        return accountFreeMargin;
    }

    public void setAccountFreeMargin(DoubleFilter accountFreeMargin) {
        this.accountFreeMargin = accountFreeMargin;
    }

    public IntegerFilter getAccountStopoutLevel() {
        return accountStopoutLevel;
    }

    public IntegerFilter accountStopoutLevel() {
        if (accountStopoutLevel == null) {
            accountStopoutLevel = new IntegerFilter();
        }
        return accountStopoutLevel;
    }

    public void setAccountStopoutLevel(IntegerFilter accountStopoutLevel) {
        this.accountStopoutLevel = accountStopoutLevel;
    }

    public DoubleFilter getOpenLots() {
        return openLots;
    }

    public DoubleFilter openLots() {
        if (openLots == null) {
            openLots = new DoubleFilter();
        }
        return openLots;
    }

    public void setOpenLots(DoubleFilter openLots) {
        this.openLots = openLots;
    }

    public IntegerFilter getOpenNumberOfTrades() {
        return openNumberOfTrades;
    }

    public IntegerFilter openNumberOfTrades() {
        if (openNumberOfTrades == null) {
            openNumberOfTrades = new IntegerFilter();
        }
        return openNumberOfTrades;
    }

    public void setOpenNumberOfTrades(IntegerFilter openNumberOfTrades) {
        this.openNumberOfTrades = openNumberOfTrades;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AccountDataTimeSeriesCriteria that = (AccountDataTimeSeriesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dateStamp, that.dateStamp) &&
            Objects.equals(accountBalance, that.accountBalance) &&
            Objects.equals(accountEquity, that.accountEquity) &&
            Objects.equals(accountCredit, that.accountCredit) &&
            Objects.equals(accountFreeMargin, that.accountFreeMargin) &&
            Objects.equals(accountStopoutLevel, that.accountStopoutLevel) &&
            Objects.equals(openLots, that.openLots) &&
            Objects.equals(openNumberOfTrades, that.openNumberOfTrades) &&
            Objects.equals(mt4AccountId, that.mt4AccountId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dateStamp,
            accountBalance,
            accountEquity,
            accountCredit,
            accountFreeMargin,
            accountStopoutLevel,
            openLots,
            openNumberOfTrades,
            mt4AccountId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountDataTimeSeriesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dateStamp != null ? "dateStamp=" + dateStamp + ", " : "") +
            (accountBalance != null ? "accountBalance=" + accountBalance + ", " : "") +
            (accountEquity != null ? "accountEquity=" + accountEquity + ", " : "") +
            (accountCredit != null ? "accountCredit=" + accountCredit + ", " : "") +
            (accountFreeMargin != null ? "accountFreeMargin=" + accountFreeMargin + ", " : "") +
            (accountStopoutLevel != null ? "accountStopoutLevel=" + accountStopoutLevel + ", " : "") +
            (openLots != null ? "openLots=" + openLots + ", " : "") +
            (openNumberOfTrades != null ? "openNumberOfTrades=" + openNumberOfTrades + ", " : "") +
            (mt4AccountId != null ? "mt4AccountId=" + mt4AccountId + ", " : "") +
            "}";
    }
}
