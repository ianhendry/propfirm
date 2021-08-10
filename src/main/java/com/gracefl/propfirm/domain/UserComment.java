package com.gracefl.propfirm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * not an ignored comment
 */
@ApiModel(description = "not an ignored comment")
@Entity
@Table(name = "user_comment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "comment_title", nullable = false)
    private String commentTitle;

    @Lob
    @Column(name = "comment_body")
    private String commentBody;

    @Lob
    @Column(name = "comment_media")
    private byte[] commentMedia;

    @Column(name = "comment_media_content_type")
    private String commentMediaContentType;

    @NotNull
    @Column(name = "date_added", nullable = false)
    private Instant dateAdded;

    @Column(name = "make_public_visible_on_site")
    private Boolean makePublicVisibleOnSite;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "mt4Trade", "userComments" }, allowSetters = true)
    private TradeJournalPost tradeJournalPost;

    @ManyToOne
    @JsonIgnoreProperties(value = { "instrument", "user", "userComments" }, allowSetters = true)
    private DailyAnalysisPost dailyAnalysisPost;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserComment id(Long id) {
        this.id = id;
        return this;
    }

    public String getCommentTitle() {
        return this.commentTitle;
    }

    public UserComment commentTitle(String commentTitle) {
        this.commentTitle = commentTitle;
        return this;
    }

    public void setCommentTitle(String commentTitle) {
        this.commentTitle = commentTitle;
    }

    public String getCommentBody() {
        return this.commentBody;
    }

    public UserComment commentBody(String commentBody) {
        this.commentBody = commentBody;
        return this;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public byte[] getCommentMedia() {
        return this.commentMedia;
    }

    public UserComment commentMedia(byte[] commentMedia) {
        this.commentMedia = commentMedia;
        return this;
    }

    public void setCommentMedia(byte[] commentMedia) {
        this.commentMedia = commentMedia;
    }

    public String getCommentMediaContentType() {
        return this.commentMediaContentType;
    }

    public UserComment commentMediaContentType(String commentMediaContentType) {
        this.commentMediaContentType = commentMediaContentType;
        return this;
    }

    public void setCommentMediaContentType(String commentMediaContentType) {
        this.commentMediaContentType = commentMediaContentType;
    }

    public Instant getDateAdded() {
        return this.dateAdded;
    }

    public UserComment dateAdded(Instant dateAdded) {
        this.dateAdded = dateAdded;
        return this;
    }

    public void setDateAdded(Instant dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Boolean getMakePublicVisibleOnSite() {
        return this.makePublicVisibleOnSite;
    }

    public UserComment makePublicVisibleOnSite(Boolean makePublicVisibleOnSite) {
        this.makePublicVisibleOnSite = makePublicVisibleOnSite;
        return this;
    }

    public void setMakePublicVisibleOnSite(Boolean makePublicVisibleOnSite) {
        this.makePublicVisibleOnSite = makePublicVisibleOnSite;
    }

    public TradeJournalPost getTradeJournalPost() {
        return this.tradeJournalPost;
    }

    public UserComment tradeJournalPost(TradeJournalPost tradeJournalPost) {
        this.setTradeJournalPost(tradeJournalPost);
        return this;
    }

    public void setTradeJournalPost(TradeJournalPost tradeJournalPost) {
        this.tradeJournalPost = tradeJournalPost;
    }

    public DailyAnalysisPost getDailyAnalysisPost() {
        return this.dailyAnalysisPost;
    }

    public UserComment dailyAnalysisPost(DailyAnalysisPost dailyAnalysisPost) {
        this.setDailyAnalysisPost(dailyAnalysisPost);
        return this;
    }

    public void setDailyAnalysisPost(DailyAnalysisPost dailyAnalysisPost) {
        this.dailyAnalysisPost = dailyAnalysisPost;
    }

    public User getUser() {
        return this.user;
    }

    public UserComment user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserComment)) {
            return false;
        }
        return id != null && id.equals(((UserComment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserComment{" +
            "id=" + getId() +
            ", commentTitle='" + getCommentTitle() + "'" +
            ", commentBody='" + getCommentBody() + "'" +
            ", commentMedia='" + getCommentMedia() + "'" +
            ", commentMediaContentType='" + getCommentMediaContentType() + "'" +
            ", dateAdded='" + getDateAdded() + "'" +
            ", makePublicVisibleOnSite='" + getMakePublicVisibleOnSite() + "'" +
            "}";
    }
}
