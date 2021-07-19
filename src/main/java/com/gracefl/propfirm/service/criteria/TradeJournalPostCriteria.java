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
 * Criteria class for the {@link com.gracefl.propfirm.domain.TradeJournalPost} entity. This class is used
 * in {@link com.gracefl.propfirm.web.rest.TradeJournalPostResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /trade-journal-posts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TradeJournalPostCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter postTitle;

    private InstantFilter dateAdded;

    private BooleanFilter makePublicVisibleOnSite;

    private LongFilter userId;

    private LongFilter mt4TradeId;

    private LongFilter userCommentId;

    public TradeJournalPostCriteria() {}

    public TradeJournalPostCriteria(TradeJournalPostCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.postTitle = other.postTitle == null ? null : other.postTitle.copy();
        this.dateAdded = other.dateAdded == null ? null : other.dateAdded.copy();
        this.makePublicVisibleOnSite = other.makePublicVisibleOnSite == null ? null : other.makePublicVisibleOnSite.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.mt4TradeId = other.mt4TradeId == null ? null : other.mt4TradeId.copy();
        this.userCommentId = other.userCommentId == null ? null : other.userCommentId.copy();
    }

    @Override
    public TradeJournalPostCriteria copy() {
        return new TradeJournalPostCriteria(this);
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

    public StringFilter getPostTitle() {
        return postTitle;
    }

    public StringFilter postTitle() {
        if (postTitle == null) {
            postTitle = new StringFilter();
        }
        return postTitle;
    }

    public void setPostTitle(StringFilter postTitle) {
        this.postTitle = postTitle;
    }

    public InstantFilter getDateAdded() {
        return dateAdded;
    }

    public InstantFilter dateAdded() {
        if (dateAdded == null) {
            dateAdded = new InstantFilter();
        }
        return dateAdded;
    }

    public void setDateAdded(InstantFilter dateAdded) {
        this.dateAdded = dateAdded;
    }

    public BooleanFilter getMakePublicVisibleOnSite() {
        return makePublicVisibleOnSite;
    }

    public BooleanFilter makePublicVisibleOnSite() {
        if (makePublicVisibleOnSite == null) {
            makePublicVisibleOnSite = new BooleanFilter();
        }
        return makePublicVisibleOnSite;
    }

    public void setMakePublicVisibleOnSite(BooleanFilter makePublicVisibleOnSite) {
        this.makePublicVisibleOnSite = makePublicVisibleOnSite;
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

    public LongFilter getMt4TradeId() {
        return mt4TradeId;
    }

    public LongFilter mt4TradeId() {
        if (mt4TradeId == null) {
            mt4TradeId = new LongFilter();
        }
        return mt4TradeId;
    }

    public void setMt4TradeId(LongFilter mt4TradeId) {
        this.mt4TradeId = mt4TradeId;
    }

    public LongFilter getUserCommentId() {
        return userCommentId;
    }

    public LongFilter userCommentId() {
        if (userCommentId == null) {
            userCommentId = new LongFilter();
        }
        return userCommentId;
    }

    public void setUserCommentId(LongFilter userCommentId) {
        this.userCommentId = userCommentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TradeJournalPostCriteria that = (TradeJournalPostCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(postTitle, that.postTitle) &&
            Objects.equals(dateAdded, that.dateAdded) &&
            Objects.equals(makePublicVisibleOnSite, that.makePublicVisibleOnSite) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(mt4TradeId, that.mt4TradeId) &&
            Objects.equals(userCommentId, that.userCommentId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, postTitle, dateAdded, makePublicVisibleOnSite, userId, mt4TradeId, userCommentId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TradeJournalPostCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (postTitle != null ? "postTitle=" + postTitle + ", " : "") +
            (dateAdded != null ? "dateAdded=" + dateAdded + ", " : "") +
            (makePublicVisibleOnSite != null ? "makePublicVisibleOnSite=" + makePublicVisibleOnSite + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (mt4TradeId != null ? "mt4TradeId=" + mt4TradeId + ", " : "") +
            (userCommentId != null ? "userCommentId=" + userCommentId + ", " : "") +
            "}";
    }
}
