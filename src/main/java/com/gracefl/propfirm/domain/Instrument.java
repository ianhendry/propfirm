package com.gracefl.propfirm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gracefl.propfirm.domain.enumeration.INSTRUMENTTYPE;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Instrument.
 */
@Entity
@Table(name = "instrument")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Instrument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticker")
    private String ticker;

    @Enumerated(EnumType.STRING)
    @Column(name = "instrument_type")
    private INSTRUMENTTYPE instrumentType;

    @Column(name = "exchange")
    private String exchange;

    @Column(name = "average_spread")
    private Double averageSpread;

    @Lob
    @Column(name = "trade_restrictions")
    private String tradeRestrictions;

    @Column(name = "in_active")
    private Boolean inActive;

    @Column(name = "in_active_date")
    private Instant inActiveDate;

    @OneToMany(mappedBy = "instrument")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tradeJournalPost", "mt4Account", "instrument" }, allowSetters = true)
    private Set<Mt4Trade> mt4Trades = new HashSet<>();

    @OneToMany(mappedBy = "instrument")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "instrument", "user", "userComments" }, allowSetters = true)
    private Set<DailyAnalysisPost> dailyAnalysisPosts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instrument id(Long id) {
        this.id = id;
        return this;
    }

    public String getTicker() {
        return this.ticker;
    }

    public Instrument ticker(String ticker) {
        this.ticker = ticker;
        return this;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public INSTRUMENTTYPE getInstrumentType() {
        return this.instrumentType;
    }

    public Instrument instrumentType(INSTRUMENTTYPE instrumentType) {
        this.instrumentType = instrumentType;
        return this;
    }

    public void setInstrumentType(INSTRUMENTTYPE instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getExchange() {
        return this.exchange;
    }

    public Instrument exchange(String exchange) {
        this.exchange = exchange;
        return this;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Double getAverageSpread() {
        return this.averageSpread;
    }

    public Instrument averageSpread(Double averageSpread) {
        this.averageSpread = averageSpread;
        return this;
    }

    public void setAverageSpread(Double averageSpread) {
        this.averageSpread = averageSpread;
    }

    public String getTradeRestrictions() {
        return this.tradeRestrictions;
    }

    public Instrument tradeRestrictions(String tradeRestrictions) {
        this.tradeRestrictions = tradeRestrictions;
        return this;
    }

    public void setTradeRestrictions(String tradeRestrictions) {
        this.tradeRestrictions = tradeRestrictions;
    }

    public Boolean getInActive() {
        return this.inActive;
    }

    public Instrument inActive(Boolean inActive) {
        this.inActive = inActive;
        return this;
    }

    public void setInActive(Boolean inActive) {
        this.inActive = inActive;
    }

    public Instant getInActiveDate() {
        return this.inActiveDate;
    }

    public Instrument inActiveDate(Instant inActiveDate) {
        this.inActiveDate = inActiveDate;
        return this;
    }

    public void setInActiveDate(Instant inActiveDate) {
        this.inActiveDate = inActiveDate;
    }

    public Set<Mt4Trade> getMt4Trades() {
        return this.mt4Trades;
    }

    public Instrument mt4Trades(Set<Mt4Trade> mt4Trades) {
        this.setMt4Trades(mt4Trades);
        return this;
    }

    public Instrument addMt4Trade(Mt4Trade mt4Trade) {
        this.mt4Trades.add(mt4Trade);
        mt4Trade.setInstrument(this);
        return this;
    }

    public Instrument removeMt4Trade(Mt4Trade mt4Trade) {
        this.mt4Trades.remove(mt4Trade);
        mt4Trade.setInstrument(null);
        return this;
    }

    public void setMt4Trades(Set<Mt4Trade> mt4Trades) {
        if (this.mt4Trades != null) {
            this.mt4Trades.forEach(i -> i.setInstrument(null));
        }
        if (mt4Trades != null) {
            mt4Trades.forEach(i -> i.setInstrument(this));
        }
        this.mt4Trades = mt4Trades;
    }

    public Set<DailyAnalysisPost> getDailyAnalysisPosts() {
        return this.dailyAnalysisPosts;
    }

    public Instrument dailyAnalysisPosts(Set<DailyAnalysisPost> dailyAnalysisPosts) {
        this.setDailyAnalysisPosts(dailyAnalysisPosts);
        return this;
    }

    public Instrument addDailyAnalysisPost(DailyAnalysisPost dailyAnalysisPost) {
        this.dailyAnalysisPosts.add(dailyAnalysisPost);
        dailyAnalysisPost.setInstrument(this);
        return this;
    }

    public Instrument removeDailyAnalysisPost(DailyAnalysisPost dailyAnalysisPost) {
        this.dailyAnalysisPosts.remove(dailyAnalysisPost);
        dailyAnalysisPost.setInstrument(null);
        return this;
    }

    public void setDailyAnalysisPosts(Set<DailyAnalysisPost> dailyAnalysisPosts) {
        if (this.dailyAnalysisPosts != null) {
            this.dailyAnalysisPosts.forEach(i -> i.setInstrument(null));
        }
        if (dailyAnalysisPosts != null) {
            dailyAnalysisPosts.forEach(i -> i.setInstrument(this));
        }
        this.dailyAnalysisPosts = dailyAnalysisPosts;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Instrument)) {
            return false;
        }
        return id != null && id.equals(((Instrument) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Instrument{" +
            "id=" + getId() +
            ", ticker='" + getTicker() + "'" +
            ", instrumentType='" + getInstrumentType() + "'" +
            ", exchange='" + getExchange() + "'" +
            ", averageSpread=" + getAverageSpread() +
            ", tradeRestrictions='" + getTradeRestrictions() + "'" +
            ", inActive='" + getInActive() + "'" +
            ", inActiveDate='" + getInActiveDate() + "'" +
            "}";
    }
}
