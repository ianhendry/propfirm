package com.gracefl.propfirm.service.criteria;

import com.gracefl.propfirm.domain.enumeration.ACCOUNTTYPE;
import com.gracefl.propfirm.domain.enumeration.BROKER;
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
 * Criteria class for the {@link com.gracefl.propfirm.domain.Mt4Account} entity. This class is used
 * in {@link com.gracefl.propfirm.web.rest.Mt4AccountResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /mt-4-accounts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class Mt4AccountCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ACCOUNTTYPE
     */
    public static class ACCOUNTTYPEFilter extends Filter<ACCOUNTTYPE> {

        public ACCOUNTTYPEFilter() {}

        public ACCOUNTTYPEFilter(ACCOUNTTYPEFilter filter) {
            super(filter);
        }

        @Override
        public ACCOUNTTYPEFilter copy() {
            return new ACCOUNTTYPEFilter(this);
        }
    }

    /**
     * Class for filtering BROKER
     */
    public static class BROKERFilter extends Filter<BROKER> {

        public BROKERFilter() {}

        public BROKERFilter(BROKERFilter filter) {
            super(filter);
        }

        @Override
        public BROKERFilter copy() {
            return new BROKERFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ACCOUNTTYPEFilter accountType;

    private BROKERFilter accountBroker;

    private StringFilter accountLogin;

    private StringFilter accountPassword;

    private StringFilter accountInvestorLogin;

    private StringFilter accountInvestorPassword;

    private BooleanFilter accountReal;

    private DoubleFilter accountInfoDouble;

    private IntegerFilter accountInfoInteger;

    private StringFilter accountInfoString;

    private DoubleFilter accountBalance;

    private DoubleFilter accountCredit;

    private StringFilter accountCompany;

    private StringFilter accountCurrency;

    private DoubleFilter accountEquity;

    private DoubleFilter accountFreeMargin;

    private DoubleFilter accountFreeMarginCheck;

    private DoubleFilter accountFreeMarginMode;

    private IntegerFilter accountLeverage;

    private DoubleFilter accountMargin;

    private StringFilter accountName;

    private IntegerFilter accountNumber;

    private DoubleFilter accountProfit;

    private StringFilter accountServer;

    private IntegerFilter accountStopoutLevel;

    private IntegerFilter accountStopoutMode;

    private BooleanFilter inActive;

    private InstantFilter inActiveDate;

    private LongFilter tradeChallengeId;

    private LongFilter mt4TradeId;

    private LongFilter accountDataTimeSeriesId;

    public Mt4AccountCriteria() {}

    public Mt4AccountCriteria(Mt4AccountCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.accountType = other.accountType == null ? null : other.accountType.copy();
        this.accountBroker = other.accountBroker == null ? null : other.accountBroker.copy();
        this.accountLogin = other.accountLogin == null ? null : other.accountLogin.copy();
        this.accountPassword = other.accountPassword == null ? null : other.accountPassword.copy();
        this.accountInvestorLogin = other.accountInvestorLogin == null ? null : other.accountInvestorLogin.copy();
        this.accountInvestorPassword = other.accountInvestorPassword == null ? null : other.accountInvestorPassword.copy();
        this.accountReal = other.accountReal == null ? null : other.accountReal.copy();
        this.accountInfoDouble = other.accountInfoDouble == null ? null : other.accountInfoDouble.copy();
        this.accountInfoInteger = other.accountInfoInteger == null ? null : other.accountInfoInteger.copy();
        this.accountInfoString = other.accountInfoString == null ? null : other.accountInfoString.copy();
        this.accountBalance = other.accountBalance == null ? null : other.accountBalance.copy();
        this.accountCredit = other.accountCredit == null ? null : other.accountCredit.copy();
        this.accountCompany = other.accountCompany == null ? null : other.accountCompany.copy();
        this.accountCurrency = other.accountCurrency == null ? null : other.accountCurrency.copy();
        this.accountEquity = other.accountEquity == null ? null : other.accountEquity.copy();
        this.accountFreeMargin = other.accountFreeMargin == null ? null : other.accountFreeMargin.copy();
        this.accountFreeMarginCheck = other.accountFreeMarginCheck == null ? null : other.accountFreeMarginCheck.copy();
        this.accountFreeMarginMode = other.accountFreeMarginMode == null ? null : other.accountFreeMarginMode.copy();
        this.accountLeverage = other.accountLeverage == null ? null : other.accountLeverage.copy();
        this.accountMargin = other.accountMargin == null ? null : other.accountMargin.copy();
        this.accountName = other.accountName == null ? null : other.accountName.copy();
        this.accountNumber = other.accountNumber == null ? null : other.accountNumber.copy();
        this.accountProfit = other.accountProfit == null ? null : other.accountProfit.copy();
        this.accountServer = other.accountServer == null ? null : other.accountServer.copy();
        this.accountStopoutLevel = other.accountStopoutLevel == null ? null : other.accountStopoutLevel.copy();
        this.accountStopoutMode = other.accountStopoutMode == null ? null : other.accountStopoutMode.copy();
        this.inActive = other.inActive == null ? null : other.inActive.copy();
        this.inActiveDate = other.inActiveDate == null ? null : other.inActiveDate.copy();
        this.tradeChallengeId = other.tradeChallengeId == null ? null : other.tradeChallengeId.copy();
        this.mt4TradeId = other.mt4TradeId == null ? null : other.mt4TradeId.copy();
        this.accountDataTimeSeriesId = other.accountDataTimeSeriesId == null ? null : other.accountDataTimeSeriesId.copy();
    }

    @Override
    public Mt4AccountCriteria copy() {
        return new Mt4AccountCriteria(this);
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

    public ACCOUNTTYPEFilter getAccountType() {
        return accountType;
    }

    public ACCOUNTTYPEFilter accountType() {
        if (accountType == null) {
            accountType = new ACCOUNTTYPEFilter();
        }
        return accountType;
    }

    public void setAccountType(ACCOUNTTYPEFilter accountType) {
        this.accountType = accountType;
    }

    public BROKERFilter getAccountBroker() {
        return accountBroker;
    }

    public BROKERFilter accountBroker() {
        if (accountBroker == null) {
            accountBroker = new BROKERFilter();
        }
        return accountBroker;
    }

    public void setAccountBroker(BROKERFilter accountBroker) {
        this.accountBroker = accountBroker;
    }

    public StringFilter getAccountLogin() {
        return accountLogin;
    }

    public StringFilter accountLogin() {
        if (accountLogin == null) {
            accountLogin = new StringFilter();
        }
        return accountLogin;
    }

    public void setAccountLogin(StringFilter accountLogin) {
        this.accountLogin = accountLogin;
    }

    public StringFilter getAccountPassword() {
        return accountPassword;
    }

    public StringFilter accountPassword() {
        if (accountPassword == null) {
            accountPassword = new StringFilter();
        }
        return accountPassword;
    }

    public void setAccountPassword(StringFilter accountPassword) {
        this.accountPassword = accountPassword;
    }

    public StringFilter getAccountInvestorLogin() {
        return accountInvestorLogin;
    }

    public StringFilter accountInvestorLogin() {
        if (accountInvestorLogin == null) {
            accountInvestorLogin = new StringFilter();
        }
        return accountInvestorLogin;
    }

    public void setAccountInvestorLogin(StringFilter accountInvestorLogin) {
        this.accountInvestorLogin = accountInvestorLogin;
    }

    public StringFilter getAccountInvestorPassword() {
        return accountInvestorPassword;
    }

    public StringFilter accountInvestorPassword() {
        if (accountInvestorPassword == null) {
            accountInvestorPassword = new StringFilter();
        }
        return accountInvestorPassword;
    }

    public void setAccountInvestorPassword(StringFilter accountInvestorPassword) {
        this.accountInvestorPassword = accountInvestorPassword;
    }

    public BooleanFilter getAccountReal() {
        return accountReal;
    }

    public BooleanFilter accountReal() {
        if (accountReal == null) {
            accountReal = new BooleanFilter();
        }
        return accountReal;
    }

    public void setAccountReal(BooleanFilter accountReal) {
        this.accountReal = accountReal;
    }

    public DoubleFilter getAccountInfoDouble() {
        return accountInfoDouble;
    }

    public DoubleFilter accountInfoDouble() {
        if (accountInfoDouble == null) {
            accountInfoDouble = new DoubleFilter();
        }
        return accountInfoDouble;
    }

    public void setAccountInfoDouble(DoubleFilter accountInfoDouble) {
        this.accountInfoDouble = accountInfoDouble;
    }

    public IntegerFilter getAccountInfoInteger() {
        return accountInfoInteger;
    }

    public IntegerFilter accountInfoInteger() {
        if (accountInfoInteger == null) {
            accountInfoInteger = new IntegerFilter();
        }
        return accountInfoInteger;
    }

    public void setAccountInfoInteger(IntegerFilter accountInfoInteger) {
        this.accountInfoInteger = accountInfoInteger;
    }

    public StringFilter getAccountInfoString() {
        return accountInfoString;
    }

    public StringFilter accountInfoString() {
        if (accountInfoString == null) {
            accountInfoString = new StringFilter();
        }
        return accountInfoString;
    }

    public void setAccountInfoString(StringFilter accountInfoString) {
        this.accountInfoString = accountInfoString;
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

    public StringFilter getAccountCompany() {
        return accountCompany;
    }

    public StringFilter accountCompany() {
        if (accountCompany == null) {
            accountCompany = new StringFilter();
        }
        return accountCompany;
    }

    public void setAccountCompany(StringFilter accountCompany) {
        this.accountCompany = accountCompany;
    }

    public StringFilter getAccountCurrency() {
        return accountCurrency;
    }

    public StringFilter accountCurrency() {
        if (accountCurrency == null) {
            accountCurrency = new StringFilter();
        }
        return accountCurrency;
    }

    public void setAccountCurrency(StringFilter accountCurrency) {
        this.accountCurrency = accountCurrency;
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

    public DoubleFilter getAccountFreeMarginCheck() {
        return accountFreeMarginCheck;
    }

    public DoubleFilter accountFreeMarginCheck() {
        if (accountFreeMarginCheck == null) {
            accountFreeMarginCheck = new DoubleFilter();
        }
        return accountFreeMarginCheck;
    }

    public void setAccountFreeMarginCheck(DoubleFilter accountFreeMarginCheck) {
        this.accountFreeMarginCheck = accountFreeMarginCheck;
    }

    public DoubleFilter getAccountFreeMarginMode() {
        return accountFreeMarginMode;
    }

    public DoubleFilter accountFreeMarginMode() {
        if (accountFreeMarginMode == null) {
            accountFreeMarginMode = new DoubleFilter();
        }
        return accountFreeMarginMode;
    }

    public void setAccountFreeMarginMode(DoubleFilter accountFreeMarginMode) {
        this.accountFreeMarginMode = accountFreeMarginMode;
    }

    public IntegerFilter getAccountLeverage() {
        return accountLeverage;
    }

    public IntegerFilter accountLeverage() {
        if (accountLeverage == null) {
            accountLeverage = new IntegerFilter();
        }
        return accountLeverage;
    }

    public void setAccountLeverage(IntegerFilter accountLeverage) {
        this.accountLeverage = accountLeverage;
    }

    public DoubleFilter getAccountMargin() {
        return accountMargin;
    }

    public DoubleFilter accountMargin() {
        if (accountMargin == null) {
            accountMargin = new DoubleFilter();
        }
        return accountMargin;
    }

    public void setAccountMargin(DoubleFilter accountMargin) {
        this.accountMargin = accountMargin;
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

    public IntegerFilter getAccountNumber() {
        return accountNumber;
    }

    public IntegerFilter accountNumber() {
        if (accountNumber == null) {
            accountNumber = new IntegerFilter();
        }
        return accountNumber;
    }

    public void setAccountNumber(IntegerFilter accountNumber) {
        this.accountNumber = accountNumber;
    }

    public DoubleFilter getAccountProfit() {
        return accountProfit;
    }

    public DoubleFilter accountProfit() {
        if (accountProfit == null) {
            accountProfit = new DoubleFilter();
        }
        return accountProfit;
    }

    public void setAccountProfit(DoubleFilter accountProfit) {
        this.accountProfit = accountProfit;
    }

    public StringFilter getAccountServer() {
        return accountServer;
    }

    public StringFilter accountServer() {
        if (accountServer == null) {
            accountServer = new StringFilter();
        }
        return accountServer;
    }

    public void setAccountServer(StringFilter accountServer) {
        this.accountServer = accountServer;
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

    public IntegerFilter getAccountStopoutMode() {
        return accountStopoutMode;
    }

    public IntegerFilter accountStopoutMode() {
        if (accountStopoutMode == null) {
            accountStopoutMode = new IntegerFilter();
        }
        return accountStopoutMode;
    }

    public void setAccountStopoutMode(IntegerFilter accountStopoutMode) {
        this.accountStopoutMode = accountStopoutMode;
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

    public LongFilter getAccountDataTimeSeriesId() {
        return accountDataTimeSeriesId;
    }

    public LongFilter accountDataTimeSeriesId() {
        if (accountDataTimeSeriesId == null) {
            accountDataTimeSeriesId = new LongFilter();
        }
        return accountDataTimeSeriesId;
    }

    public void setAccountDataTimeSeriesId(LongFilter accountDataTimeSeriesId) {
        this.accountDataTimeSeriesId = accountDataTimeSeriesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Mt4AccountCriteria that = (Mt4AccountCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(accountType, that.accountType) &&
            Objects.equals(accountBroker, that.accountBroker) &&
            Objects.equals(accountLogin, that.accountLogin) &&
            Objects.equals(accountPassword, that.accountPassword) &&
            Objects.equals(accountInvestorLogin, that.accountInvestorLogin) &&
            Objects.equals(accountInvestorPassword, that.accountInvestorPassword) &&
            Objects.equals(accountReal, that.accountReal) &&
            Objects.equals(accountInfoDouble, that.accountInfoDouble) &&
            Objects.equals(accountInfoInteger, that.accountInfoInteger) &&
            Objects.equals(accountInfoString, that.accountInfoString) &&
            Objects.equals(accountBalance, that.accountBalance) &&
            Objects.equals(accountCredit, that.accountCredit) &&
            Objects.equals(accountCompany, that.accountCompany) &&
            Objects.equals(accountCurrency, that.accountCurrency) &&
            Objects.equals(accountEquity, that.accountEquity) &&
            Objects.equals(accountFreeMargin, that.accountFreeMargin) &&
            Objects.equals(accountFreeMarginCheck, that.accountFreeMarginCheck) &&
            Objects.equals(accountFreeMarginMode, that.accountFreeMarginMode) &&
            Objects.equals(accountLeverage, that.accountLeverage) &&
            Objects.equals(accountMargin, that.accountMargin) &&
            Objects.equals(accountName, that.accountName) &&
            Objects.equals(accountNumber, that.accountNumber) &&
            Objects.equals(accountProfit, that.accountProfit) &&
            Objects.equals(accountServer, that.accountServer) &&
            Objects.equals(accountStopoutLevel, that.accountStopoutLevel) &&
            Objects.equals(accountStopoutMode, that.accountStopoutMode) &&
            Objects.equals(inActive, that.inActive) &&
            Objects.equals(inActiveDate, that.inActiveDate) &&
            Objects.equals(tradeChallengeId, that.tradeChallengeId) &&
            Objects.equals(mt4TradeId, that.mt4TradeId) &&
            Objects.equals(accountDataTimeSeriesId, that.accountDataTimeSeriesId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            accountType,
            accountBroker,
            accountLogin,
            accountPassword,
            accountInvestorLogin,
            accountInvestorPassword,
            accountReal,
            accountInfoDouble,
            accountInfoInteger,
            accountInfoString,
            accountBalance,
            accountCredit,
            accountCompany,
            accountCurrency,
            accountEquity,
            accountFreeMargin,
            accountFreeMarginCheck,
            accountFreeMarginMode,
            accountLeverage,
            accountMargin,
            accountName,
            accountNumber,
            accountProfit,
            accountServer,
            accountStopoutLevel,
            accountStopoutMode,
            inActive,
            inActiveDate,
            tradeChallengeId,
            mt4TradeId,
            accountDataTimeSeriesId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mt4AccountCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (accountType != null ? "accountType=" + accountType + ", " : "") +
            (accountBroker != null ? "accountBroker=" + accountBroker + ", " : "") +
            (accountLogin != null ? "accountLogin=" + accountLogin + ", " : "") +
            (accountPassword != null ? "accountPassword=" + accountPassword + ", " : "") +
            (accountInvestorLogin != null ? "accountInvestorLogin=" + accountInvestorLogin + ", " : "") +
            (accountInvestorPassword != null ? "accountInvestorPassword=" + accountInvestorPassword + ", " : "") +
            (accountReal != null ? "accountReal=" + accountReal + ", " : "") +
            (accountInfoDouble != null ? "accountInfoDouble=" + accountInfoDouble + ", " : "") +
            (accountInfoInteger != null ? "accountInfoInteger=" + accountInfoInteger + ", " : "") +
            (accountInfoString != null ? "accountInfoString=" + accountInfoString + ", " : "") +
            (accountBalance != null ? "accountBalance=" + accountBalance + ", " : "") +
            (accountCredit != null ? "accountCredit=" + accountCredit + ", " : "") +
            (accountCompany != null ? "accountCompany=" + accountCompany + ", " : "") +
            (accountCurrency != null ? "accountCurrency=" + accountCurrency + ", " : "") +
            (accountEquity != null ? "accountEquity=" + accountEquity + ", " : "") +
            (accountFreeMargin != null ? "accountFreeMargin=" + accountFreeMargin + ", " : "") +
            (accountFreeMarginCheck != null ? "accountFreeMarginCheck=" + accountFreeMarginCheck + ", " : "") +
            (accountFreeMarginMode != null ? "accountFreeMarginMode=" + accountFreeMarginMode + ", " : "") +
            (accountLeverage != null ? "accountLeverage=" + accountLeverage + ", " : "") +
            (accountMargin != null ? "accountMargin=" + accountMargin + ", " : "") +
            (accountName != null ? "accountName=" + accountName + ", " : "") +
            (accountNumber != null ? "accountNumber=" + accountNumber + ", " : "") +
            (accountProfit != null ? "accountProfit=" + accountProfit + ", " : "") +
            (accountServer != null ? "accountServer=" + accountServer + ", " : "") +
            (accountStopoutLevel != null ? "accountStopoutLevel=" + accountStopoutLevel + ", " : "") +
            (accountStopoutMode != null ? "accountStopoutMode=" + accountStopoutMode + ", " : "") +
            (inActive != null ? "inActive=" + inActive + ", " : "") +
            (inActiveDate != null ? "inActiveDate=" + inActiveDate + ", " : "") +
            (tradeChallengeId != null ? "tradeChallengeId=" + tradeChallengeId + ", " : "") +
            (mt4TradeId != null ? "mt4TradeId=" + mt4TradeId + ", " : "") +
            (accountDataTimeSeriesId != null ? "accountDataTimeSeriesId=" + accountDataTimeSeriesId + ", " : "") +
            "}";
    }
}
