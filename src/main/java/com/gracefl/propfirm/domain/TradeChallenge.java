package com.gracefl.propfirm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TradeChallenge.
 */
@Entity
@Table(name = "trade_challenge")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TradeChallenge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trade_challenge_name")
    private String tradeChallengeName;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "running_max_total_drawdown")
    private Double runningMaxTotalDrawdown;

    @Column(name = "running_max_daily_drawdown")
    private Double runningMaxDailyDrawdown;

    @Column(name = "rules_violated")
    private Boolean rulesViolated;

    @Column(name = "rule_violated")
    private String ruleViolated;

    @Column(name = "rule_violated_date")
    private Instant ruleViolatedDate;

    @JsonIgnoreProperties(value = { "tradeChallenge", "mt4Trades", "accountDataTimeSeries" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Mt4Account mt4Account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "user", "addressDetails", "tradeChallenges" }, allowSetters = true)
    private SiteAccount siteAccount;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tradeChallenges" }, allowSetters = true)
    private ChallengeType challengeType;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TradeChallenge id(Long id) {
        this.id = id;
        return this;
    }

    public String getTradeChallengeName() {
        return this.tradeChallengeName;
    }

    public TradeChallenge tradeChallengeName(String tradeChallengeName) {
        this.tradeChallengeName = tradeChallengeName;
        return this;
    }

    public void setTradeChallengeName(String tradeChallengeName) {
        this.tradeChallengeName = tradeChallengeName;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public TradeChallenge startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Double getRunningMaxTotalDrawdown() {
        return this.runningMaxTotalDrawdown;
    }

    public TradeChallenge runningMaxTotalDrawdown(Double runningMaxTotalDrawdown) {
        this.runningMaxTotalDrawdown = runningMaxTotalDrawdown;
        return this;
    }

    public void setRunningMaxTotalDrawdown(Double runningMaxTotalDrawdown) {
        this.runningMaxTotalDrawdown = runningMaxTotalDrawdown;
    }

    public Double getRunningMaxDailyDrawdown() {
        return this.runningMaxDailyDrawdown;
    }

    public TradeChallenge runningMaxDailyDrawdown(Double runningMaxDailyDrawdown) {
        this.runningMaxDailyDrawdown = runningMaxDailyDrawdown;
        return this;
    }

    public void setRunningMaxDailyDrawdown(Double runningMaxDailyDrawdown) {
        this.runningMaxDailyDrawdown = runningMaxDailyDrawdown;
    }

    public Boolean getRulesViolated() {
        return this.rulesViolated;
    }

    public TradeChallenge rulesViolated(Boolean rulesViolated) {
        this.rulesViolated = rulesViolated;
        return this;
    }

    public void setRulesViolated(Boolean rulesViolated) {
        this.rulesViolated = rulesViolated;
    }

    public String getRuleViolated() {
        return this.ruleViolated;
    }

    public TradeChallenge ruleViolated(String ruleViolated) {
        this.ruleViolated = ruleViolated;
        return this;
    }

    public void setRuleViolated(String ruleViolated) {
        this.ruleViolated = ruleViolated;
    }

    public Instant getRuleViolatedDate() {
        return this.ruleViolatedDate;
    }

    public TradeChallenge ruleViolatedDate(Instant ruleViolatedDate) {
        this.ruleViolatedDate = ruleViolatedDate;
        return this;
    }

    public void setRuleViolatedDate(Instant ruleViolatedDate) {
        this.ruleViolatedDate = ruleViolatedDate;
    }

    public Mt4Account getMt4Account() {
        return this.mt4Account;
    }

    public TradeChallenge mt4Account(Mt4Account mt4Account) {
        this.setMt4Account(mt4Account);
        return this;
    }

    public void setMt4Account(Mt4Account mt4Account) {
        this.mt4Account = mt4Account;
    }

    public SiteAccount getSiteAccount() {
        return this.siteAccount;
    }

    public TradeChallenge siteAccount(SiteAccount siteAccount) {
        this.setSiteAccount(siteAccount);
        return this;
    }

    public void setSiteAccount(SiteAccount siteAccount) {
        this.siteAccount = siteAccount;
    }

    public ChallengeType getChallengeType() {
        return this.challengeType;
    }

    public TradeChallenge challengeType(ChallengeType challengeType) {
        this.setChallengeType(challengeType);
        return this;
    }

    public void setChallengeType(ChallengeType challengeType) {
        this.challengeType = challengeType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TradeChallenge)) {
            return false;
        }
        return id != null && id.equals(((TradeChallenge) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TradeChallenge{" +
            "id=" + getId() +
            ", tradeChallengeName='" + getTradeChallengeName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", runningMaxTotalDrawdown=" + getRunningMaxTotalDrawdown() +
            ", runningMaxDailyDrawdown=" + getRunningMaxDailyDrawdown() +
            ", rulesViolated='" + getRulesViolated() + "'" +
            ", ruleViolated='" + getRuleViolated() + "'" +
            ", ruleViolatedDate='" + getRuleViolatedDate() + "'" +
            "}";
    }
}
