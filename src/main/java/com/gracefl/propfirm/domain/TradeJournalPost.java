package com.gracefl.propfirm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TradeJournalPost.
 */
@Entity
@Table(name = "trade_journal_post")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TradeJournalPost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "post_title", nullable = false)
    private String postTitle;

    @NotNull
    @Column(name = "date_added", nullable = false)
    private Instant dateAdded;

    @Lob
    @Column(name = "thoughts_on_psychology")
    private String thoughtsOnPsychology;

    @Lob
    @Column(name = "thoughts_on_trade_process_accuracy")
    private String thoughtsOnTradeProcessAccuracy;

    @Lob
    @Column(name = "thoughts_on_areas_of_strength")
    private String thoughtsOnAreasOfStrength;

    @Lob
    @Column(name = "thoughts_on_areas_for_improvement")
    private String thoughtsOnAreasForImprovement;

    @Lob
    @Column(name = "area_of_focus_for_tomorrow")
    private String areaOfFocusForTomorrow;

    @Column(name = "make_public_visible_on_site")
    private Boolean makePublicVisibleOnSite;

    @Lob
    @Column(name = "any_media")
    private byte[] anyMedia;

    @Column(name = "any_media_content_type")
    private String anyMediaContentType;

    @Lob
    @Column(name = "any_image")
    private byte[] anyImage;

    @Column(name = "any_image_content_type")
    private String anyImageContentType;

    @ManyToOne
    private User user;

    @JsonIgnoreProperties(value = { "tradeJournalPost", "mt4Account", "instrument" }, allowSetters = true)
    @OneToOne(mappedBy = "tradeJournalPost")
    private Mt4Trade mt4Trade;

    @OneToMany(mappedBy = "tradeJournalPost")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tradeJournalPost", "dailyAnalysisPost", "user" }, allowSetters = true)
    private Set<UserComment> userComments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TradeJournalPost id(Long id) {
        this.id = id;
        return this;
    }

    public String getPostTitle() {
        return this.postTitle;
    }

    public TradeJournalPost postTitle(String postTitle) {
        this.postTitle = postTitle;
        return this;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public Instant getDateAdded() {
        return this.dateAdded;
    }

    public TradeJournalPost dateAdded(Instant dateAdded) {
        this.dateAdded = dateAdded;
        return this;
    }

    public void setDateAdded(Instant dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getThoughtsOnPsychology() {
        return this.thoughtsOnPsychology;
    }

    public TradeJournalPost thoughtsOnPsychology(String thoughtsOnPsychology) {
        this.thoughtsOnPsychology = thoughtsOnPsychology;
        return this;
    }

    public void setThoughtsOnPsychology(String thoughtsOnPsychology) {
        this.thoughtsOnPsychology = thoughtsOnPsychology;
    }

    public String getThoughtsOnTradeProcessAccuracy() {
        return this.thoughtsOnTradeProcessAccuracy;
    }

    public TradeJournalPost thoughtsOnTradeProcessAccuracy(String thoughtsOnTradeProcessAccuracy) {
        this.thoughtsOnTradeProcessAccuracy = thoughtsOnTradeProcessAccuracy;
        return this;
    }

    public void setThoughtsOnTradeProcessAccuracy(String thoughtsOnTradeProcessAccuracy) {
        this.thoughtsOnTradeProcessAccuracy = thoughtsOnTradeProcessAccuracy;
    }

    public String getThoughtsOnAreasOfStrength() {
        return this.thoughtsOnAreasOfStrength;
    }

    public TradeJournalPost thoughtsOnAreasOfStrength(String thoughtsOnAreasOfStrength) {
        this.thoughtsOnAreasOfStrength = thoughtsOnAreasOfStrength;
        return this;
    }

    public void setThoughtsOnAreasOfStrength(String thoughtsOnAreasOfStrength) {
        this.thoughtsOnAreasOfStrength = thoughtsOnAreasOfStrength;
    }

    public String getThoughtsOnAreasForImprovement() {
        return this.thoughtsOnAreasForImprovement;
    }

    public TradeJournalPost thoughtsOnAreasForImprovement(String thoughtsOnAreasForImprovement) {
        this.thoughtsOnAreasForImprovement = thoughtsOnAreasForImprovement;
        return this;
    }

    public void setThoughtsOnAreasForImprovement(String thoughtsOnAreasForImprovement) {
        this.thoughtsOnAreasForImprovement = thoughtsOnAreasForImprovement;
    }

    public String getAreaOfFocusForTomorrow() {
        return this.areaOfFocusForTomorrow;
    }

    public TradeJournalPost areaOfFocusForTomorrow(String areaOfFocusForTomorrow) {
        this.areaOfFocusForTomorrow = areaOfFocusForTomorrow;
        return this;
    }

    public void setAreaOfFocusForTomorrow(String areaOfFocusForTomorrow) {
        this.areaOfFocusForTomorrow = areaOfFocusForTomorrow;
    }

    public Boolean getMakePublicVisibleOnSite() {
        return this.makePublicVisibleOnSite;
    }

    public TradeJournalPost makePublicVisibleOnSite(Boolean makePublicVisibleOnSite) {
        this.makePublicVisibleOnSite = makePublicVisibleOnSite;
        return this;
    }

    public void setMakePublicVisibleOnSite(Boolean makePublicVisibleOnSite) {
        this.makePublicVisibleOnSite = makePublicVisibleOnSite;
    }

    public byte[] getAnyMedia() {
        return this.anyMedia;
    }

    public TradeJournalPost anyMedia(byte[] anyMedia) {
        this.anyMedia = anyMedia;
        return this;
    }

    public void setAnyMedia(byte[] anyMedia) {
        this.anyMedia = anyMedia;
    }

    public String getAnyMediaContentType() {
        return this.anyMediaContentType;
    }

    public TradeJournalPost anyMediaContentType(String anyMediaContentType) {
        this.anyMediaContentType = anyMediaContentType;
        return this;
    }

    public void setAnyMediaContentType(String anyMediaContentType) {
        this.anyMediaContentType = anyMediaContentType;
    }

    public byte[] getAnyImage() {
        return this.anyImage;
    }

    public TradeJournalPost anyImage(byte[] anyImage) {
        this.anyImage = anyImage;
        return this;
    }

    public void setAnyImage(byte[] anyImage) {
        this.anyImage = anyImage;
    }

    public String getAnyImageContentType() {
        return this.anyImageContentType;
    }

    public TradeJournalPost anyImageContentType(String anyImageContentType) {
        this.anyImageContentType = anyImageContentType;
        return this;
    }

    public void setAnyImageContentType(String anyImageContentType) {
        this.anyImageContentType = anyImageContentType;
    }

    public User getUser() {
        return this.user;
    }

    public TradeJournalPost user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Mt4Trade getMt4Trade() {
        return this.mt4Trade;
    }

    public TradeJournalPost mt4Trade(Mt4Trade mt4Trade) {
        this.setMt4Trade(mt4Trade);
        return this;
    }

    public void setMt4Trade(Mt4Trade mt4Trade) {
        if (this.mt4Trade != null) {
            this.mt4Trade.setTradeJournalPost(null);
        }
        if (mt4Trade != null) {
            mt4Trade.setTradeJournalPost(this);
        }
        this.mt4Trade = mt4Trade;
    }

    public Set<UserComment> getUserComments() {
        return this.userComments;
    }

    public TradeJournalPost userComments(Set<UserComment> userComments) {
        this.setUserComments(userComments);
        return this;
    }

    public TradeJournalPost addUserComment(UserComment userComment) {
        this.userComments.add(userComment);
        userComment.setTradeJournalPost(this);
        return this;
    }

    public TradeJournalPost removeUserComment(UserComment userComment) {
        this.userComments.remove(userComment);
        userComment.setTradeJournalPost(null);
        return this;
    }

    public void setUserComments(Set<UserComment> userComments) {
        if (this.userComments != null) {
            this.userComments.forEach(i -> i.setTradeJournalPost(null));
        }
        if (userComments != null) {
            userComments.forEach(i -> i.setTradeJournalPost(this));
        }
        this.userComments = userComments;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TradeJournalPost)) {
            return false;
        }
        return id != null && id.equals(((TradeJournalPost) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TradeJournalPost{" +
            "id=" + getId() +
            ", postTitle='" + getPostTitle() + "'" +
            ", dateAdded='" + getDateAdded() + "'" +
            ", thoughtsOnPsychology='" + getThoughtsOnPsychology() + "'" +
            ", thoughtsOnTradeProcessAccuracy='" + getThoughtsOnTradeProcessAccuracy() + "'" +
            ", thoughtsOnAreasOfStrength='" + getThoughtsOnAreasOfStrength() + "'" +
            ", thoughtsOnAreasForImprovement='" + getThoughtsOnAreasForImprovement() + "'" +
            ", areaOfFocusForTomorrow='" + getAreaOfFocusForTomorrow() + "'" +
            ", makePublicVisibleOnSite='" + getMakePublicVisibleOnSite() + "'" +
            ", anyMedia='" + getAnyMedia() + "'" +
            ", anyMediaContentType='" + getAnyMediaContentType() + "'" +
            ", anyImage='" + getAnyImage() + "'" +
            ", anyImageContentType='" + getAnyImageContentType() + "'" +
            "}";
    }
}
