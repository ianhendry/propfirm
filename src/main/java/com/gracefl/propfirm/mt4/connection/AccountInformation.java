package com.gracefl.propfirm.mt4.connection;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "accountBalance", "accountProfit", "accountEquity", "accountMargin", "accountMarginFree",
		"accountMarginLevel", "accountSoCall", "accountSoSo" })
//@XStreamAlias("accountInformation")
public class AccountInformation {

	@JsonProperty("accountBalance")
	private Double accountBalance;
	@JsonProperty("accountProfit")
	private Double accountProfit;
	@JsonProperty("accountEquity")
	private Double accountEquity;
	@JsonProperty("accountMargin")
	private Double accountMargin;
	@JsonProperty("accountMarginFree")
	private Double accountMarginFree;
	@JsonProperty("accountMarginLevel")
	private Double accountMarginLevel;
	@JsonProperty("accountSoCall")
	private Double accountSoCall;
	@JsonProperty("accountSoSo")
	private Double accountSoSo;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("accountBalance")
	public Double getAccountBalance() {
		return accountBalance;
	}

	@JsonProperty("accountBalance")
	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
	}

	@JsonProperty("accountProfit")
	public Double getAccountProfit() {
		return accountProfit;
	}

	@JsonProperty("accountProfit")
	public void setAccountProfit(Double accountProfit) {
		this.accountProfit = accountProfit;
	}

	@JsonProperty("accountEquity")
	public Double getAccountEquity() {
		return accountEquity;
	}

	@JsonProperty("accountEquity")
	public void setAccountEquity(Double accountEquity) {
		this.accountEquity = accountEquity;
	}

	@JsonProperty("accountMargin")
	public Double getAccountMargin() {
		return accountMargin;
	}

	@JsonProperty("accountMargin")
	public void setAccountMargin(Double accountMargin) {
		this.accountMargin = accountMargin;
	}

	@JsonProperty("accountMarginFree")
	public Double getAccountMarginFree() {
		return accountMarginFree;
	}

	@JsonProperty("accountMarginFree")
	public void setAccountMarginFree(Double accountMarginFree) {
		this.accountMarginFree = accountMarginFree;
	}

	@JsonProperty("accountMarginLevel")
	public Double getAccountMarginLevel() {
		return accountMarginLevel;
	}

	@JsonProperty("accountMarginLevel")
	public void setAccountMarginLevel(Double accountMarginLevel) {
		this.accountMarginLevel = accountMarginLevel;
	}

	@JsonProperty("accountSoCall")
	public Double getAccountSoCall() {
		return accountSoCall;
	}

	@JsonProperty("accountSoCall")
	public void setAccountSoCall(Double accountSoCall) {
		this.accountSoCall = accountSoCall;
	}

	@JsonProperty("accountSoSo")
	public Double getAccountSoSo() {
		return accountSoSo;
	}

	@JsonProperty("accountSoSo")
	public void setAccountSoSo(Double accountSoSo) {
		this.accountSoSo = accountSoSo;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public String toString() {
		return "AccountInformation [accountBalance=" + accountBalance + ", accountProfit=" + accountProfit
				+ ", accountEquity=" + accountEquity + ", accountMargin=" + accountMargin + ", accountMarginFree="
				+ accountMarginFree + ", accountMarginLevel=" + accountMarginLevel + ", accountSoCall=" + accountSoCall
				+ ", accountSoSo=" + accountSoSo + ", additionalProperties=" + additionalProperties + "]";
	}

}