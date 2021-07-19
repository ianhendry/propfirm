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
 * A DailyAnalysisPost.
 */
@Entity
@Table(name = "daily_analysis_post")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DailyAnalysisPost implements Serializable {

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
    @Column(name = "background_volume")
    private String backgroundVolume;

    @Lob
    @Column(name = "overall_thoughts")
    private String overallThoughts;

    @Lob
    @Column(name = "weekly_chart")
    private byte[] weeklyChart;

    @Column(name = "weekly_chart_content_type")
    private String weeklyChartContentType;

    @Lob
    @Column(name = "daily_chart")
    private byte[] dailyChart;

    @Column(name = "daily_chart_content_type")
    private String dailyChartContentType;

    @Lob
    @Column(name = "one_hr_chart")
    private byte[] oneHrChart;

    @Column(name = "one_hr_chart_content_type")
    private String oneHrChartContentType;

    @Lob
    @Column(name = "plan_for_today")
    private String planForToday;

    @Column(name = "make_public_visible_on_site")
    private Boolean makePublicVisibleOnSite;

    @ManyToOne
    @JsonIgnoreProperties(value = { "mt4Trades", "dailyAnalysisPosts" }, allowSetters = true)
    private Instrument instrument;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "dailyAnalysisPost")
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

    public DailyAnalysisPost id(Long id) {
        this.id = id;
        return this;
    }

    public String getPostTitle() {
        return this.postTitle;
    }

    public DailyAnalysisPost postTitle(String postTitle) {
        this.postTitle = postTitle;
        return this;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public Instant getDateAdded() {
        return this.dateAdded;
    }

    public DailyAnalysisPost dateAdded(Instant dateAdded) {
        this.dateAdded = dateAdded;
        return this;
    }

    public void setDateAdded(Instant dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getBackgroundVolume() {
        return this.backgroundVolume;
    }

    public DailyAnalysisPost backgroundVolume(String backgroundVolume) {
        this.backgroundVolume = backgroundVolume;
        return this;
    }

    public void setBackgroundVolume(String backgroundVolume) {
        this.backgroundVolume = backgroundVolume;
    }

    public String getOverallThoughts() {
        return this.overallThoughts;
    }

    public DailyAnalysisPost overallThoughts(String overallThoughts) {
        this.overallThoughts = overallThoughts;
        return this;
    }

    public void setOverallThoughts(String overallThoughts) {
        this.overallThoughts = overallThoughts;
    }

    public byte[] getWeeklyChart() {
        return this.weeklyChart;
    }

    public DailyAnalysisPost weeklyChart(byte[] weeklyChart) {
        this.weeklyChart = weeklyChart;
        return this;
    }

    public void setWeeklyChart(byte[] weeklyChart) {
        this.weeklyChart = weeklyChart;
    }

    public String getWeeklyChartContentType() {
        return this.weeklyChartContentType;
    }

    public DailyAnalysisPost weeklyChartContentType(String weeklyChartContentType) {
        this.weeklyChartContentType = weeklyChartContentType;
        return this;
    }

    public void setWeeklyChartContentType(String weeklyChartContentType) {
        this.weeklyChartContentType = weeklyChartContentType;
    }

    public byte[] getDailyChart() {
        return this.dailyChart;
    }

    public DailyAnalysisPost dailyChart(byte[] dailyChart) {
        this.dailyChart = dailyChart;
        return this;
    }

    public void setDailyChart(byte[] dailyChart) {
        this.dailyChart = dailyChart;
    }

    public String getDailyChartContentType() {
        return this.dailyChartContentType;
    }

    public DailyAnalysisPost dailyChartContentType(String dailyChartContentType) {
        this.dailyChartContentType = dailyChartContentType;
        return this;
    }

    public void setDailyChartContentType(String dailyChartContentType) {
        this.dailyChartContentType = dailyChartContentType;
    }

    public byte[] getOneHrChart() {
        return this.oneHrChart;
    }

    public DailyAnalysisPost oneHrChart(byte[] oneHrChart) {
        this.oneHrChart = oneHrChart;
        return this;
    }

    public void setOneHrChart(byte[] oneHrChart) {
        this.oneHrChart = oneHrChart;
    }

    public String getOneHrChartContentType() {
        return this.oneHrChartContentType;
    }

    public DailyAnalysisPost oneHrChartContentType(String oneHrChartContentType) {
        this.oneHrChartContentType = oneHrChartContentType;
        return this;
    }

    public void setOneHrChartContentType(String oneHrChartContentType) {
        this.oneHrChartContentType = oneHrChartContentType;
    }

    public String getPlanForToday() {
        return this.planForToday;
    }

    public DailyAnalysisPost planForToday(String planForToday) {
        this.planForToday = planForToday;
        return this;
    }

    public void setPlanForToday(String planForToday) {
        this.planForToday = planForToday;
    }

    public Boolean getMakePublicVisibleOnSite() {
        return this.makePublicVisibleOnSite;
    }

    public DailyAnalysisPost makePublicVisibleOnSite(Boolean makePublicVisibleOnSite) {
        this.makePublicVisibleOnSite = makePublicVisibleOnSite;
        return this;
    }

    public void setMakePublicVisibleOnSite(Boolean makePublicVisibleOnSite) {
        this.makePublicVisibleOnSite = makePublicVisibleOnSite;
    }

    public Instrument getInstrument() {
        return this.instrument;
    }

    public DailyAnalysisPost instrument(Instrument instrument) {
        this.setInstrument(instrument);
        return this;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public User getUser() {
        return this.user;
    }

    public DailyAnalysisPost user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<UserComment> getUserComments() {
        return this.userComments;
    }

    public DailyAnalysisPost userComments(Set<UserComment> userComments) {
        this.setUserComments(userComments);
        return this;
    }

    public DailyAnalysisPost addUserComment(UserComment userComment) {
        this.userComments.add(userComment);
        userComment.setDailyAnalysisPost(this);
        return this;
    }

    public DailyAnalysisPost removeUserComment(UserComment userComment) {
        this.userComments.remove(userComment);
        userComment.setDailyAnalysisPost(null);
        return this;
    }

    public void setUserComments(Set<UserComment> userComments) {
        if (this.userComments != null) {
            this.userComments.forEach(i -> i.setDailyAnalysisPost(null));
        }
        if (userComments != null) {
            userComments.forEach(i -> i.setDailyAnalysisPost(this));
        }
        this.userComments = userComments;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DailyAnalysisPost)) {
            return false;
        }
        return id != null && id.equals(((DailyAnalysisPost) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DailyAnalysisPost{" +
            "id=" + getId() +
            ", postTitle='" + getPostTitle() + "'" +
            ", dateAdded='" + getDateAdded() + "'" +
            ", backgroundVolume='" + getBackgroundVolume() + "'" +
            ", overallThoughts='" + getOverallThoughts() + "'" +
            ", weeklyChart='" + getWeeklyChart() + "'" +
            ", weeklyChartContentType='" + getWeeklyChartContentType() + "'" +
            ", dailyChart='" + getDailyChart() + "'" +
            ", dailyChartContentType='" + getDailyChartContentType() + "'" +
            ", oneHrChart='" + getOneHrChart() + "'" +
            ", oneHrChartContentType='" + getOneHrChartContentType() + "'" +
            ", planForToday='" + getPlanForToday() + "'" +
            ", makePublicVisibleOnSite='" + getMakePublicVisibleOnSite() + "'" +
            "}";
    }
}
