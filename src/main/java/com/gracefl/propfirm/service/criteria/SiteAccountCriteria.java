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
 * Criteria class for the {@link com.gracefl.propfirm.domain.SiteAccount} entity. This class is used
 * in {@link com.gracefl.propfirm.web.rest.SiteAccountResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /site-accounts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SiteAccountCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter accountName;

    private BooleanFilter inActive;

    private InstantFilter inActiveDate;

    private LongFilter userId;

    private LongFilter addressDetailsId;

    private LongFilter tradeChallengeId;

    public SiteAccountCriteria() {}

    public SiteAccountCriteria(SiteAccountCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.accountName = other.accountName == null ? null : other.accountName.copy();
        this.inActive = other.inActive == null ? null : other.inActive.copy();
        this.inActiveDate = other.inActiveDate == null ? null : other.inActiveDate.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.addressDetailsId = other.addressDetailsId == null ? null : other.addressDetailsId.copy();
        this.tradeChallengeId = other.tradeChallengeId == null ? null : other.tradeChallengeId.copy();
    }

    @Override
    public SiteAccountCriteria copy() {
        return new SiteAccountCriteria(this);
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

    public StringFilter getAccountName() {
        return accountName;
    }

    public StringFilter accountName() {
        if (accountName == null) {
            accountName = new StringFilter();
        }
        return accountName;
    }

    public void setAccountName(StringFilter accountName) {
        this.accountName = accountName;
    }

    public BooleanFilter getInActive() {
        return inActive;
    }

    public BooleanFilter inActive() {
        if (inActive == null) {
            inActive = new BooleanFilter();
        }
        return inActive;
    }

    public void setInActive(BooleanFilter inActive) {
        this.inActive = inActive;
    }

    public InstantFilter getInActiveDate() {
        return inActiveDate;
    }

    public InstantFilter inActiveDate() {
        if (inActiveDate == null) {
            inActiveDate = new InstantFilter();
        }
        return inActiveDate;
    }

    public void setInActiveDate(InstantFilter inActiveDate) {
        this.inActiveDate = inActiveDate;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getAddressDetailsId() {
        return addressDetailsId;
    }

    public LongFilter addressDetailsId() {
        if (addressDetailsId == null) {
            addressDetailsId = new LongFilter();
        }
        return addressDetailsId;
    }

    public void setAddressDetailsId(LongFilter addressDetailsId) {
        this.addressDetailsId = addressDetailsId;
    }

    public LongFilter getTradeChallengeId() {
        return tradeChallengeId;
    }

    public LongFilter tradeChallengeId() {
        if (tradeChallengeId == null) {
            tradeChallengeId = new LongFilter();
        }
        return tradeChallengeId;
    }

    public void setTradeChallengeId(LongFilter tradeChallengeId) {
        this.tradeChallengeId = tradeChallengeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SiteAccountCriteria that = (SiteAccountCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(accountName, that.accountName) &&
            Objects.equals(inActive, that.inActive) &&
            Objects.equals(inActiveDate, that.inActiveDate) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(addressDetailsId, that.addressDetailsId) &&
            Objects.equals(tradeChallengeId, that.tradeChallengeId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountName, inActive, inActiveDate, userId, addressDetailsId, tradeChallengeId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SiteAccountCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (accountName != null ? "accountName=" + accountName + ", " : "") +
            (inActive != null ? "inActive=" + inActive + ", " : "") +
            (inActiveDate != null ? "inActiveDate=" + inActiveDate + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (addressDetailsId != null ? "addressDetailsId=" + addressDetailsId + ", " : "") +
            (tradeChallengeId != null ? "tradeChallengeId=" + tradeChallengeId + ", " : "") +
            "}";
    }
}
