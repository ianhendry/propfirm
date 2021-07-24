package com.gracefl.propfirm.mt4.connection;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "_time_stamp", "_account_balance", "_account_equity", "_account_credit", "_account_free_margin",
		"_account_stopout_level", "_open_lots", "_orders_total" })
@Generated("jsonschema2pojo")
public class Mt4LiveAccountDetailJSON {

	@JsonProperty("_time_stamp")
	private String timeStamp;
	@JsonProperty("_account_balance")
	private Double accountBalance;
	@JsonProperty("_account_equity")
	private Double accountEquity;
	@JsonProperty("_account_credit")
	private Double accountCredit;
	@JsonProperty("_account_free_margin")
	private Double accountFreeMargin;
	@JsonProperty("_account_stopout_level")
	private Double accountStopoutLevel;
	@JsonProperty("_open_lots")
	private Double openLots;
	@JsonProperty("_orders_total")
	private Double ordersTotal;

	@JsonProperty("_time_stamp")
	public String getTimeStamp() {
		return timeStamp;
	}

	@JsonProperty("_time_stamp")
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	@JsonProperty("_account_balance")
	public Double getAccountBalance() {
		return accountBalance;
	}

	@JsonProperty("_account_balance")
	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
	}

	@JsonProperty("_account_equity")
	public Double getAccountEquity() {
		return accountEquity;
	}

	@JsonProperty("_account_equity")
	public void setAccountEquity(Double accountEquity) {
		this.accountEquity = accountEquity;
	}

	@JsonProperty("_account_credit")
	public Double getAccountCredit() {
		return accountCredit;
	}

	@JsonProperty("_account_credit")
	public void setAccountCredit(Double accountCredit) {
		this.accountCredit = accountCredit;
	}

	@JsonProperty("_account_free_margin")
	public Double getAccountFreeMargin() {
		return accountFreeMargin;
	}

	@JsonProperty("_account_free_margin")
	public void setAccountFreeMargin(Double accountFreeMargin) {
		this.accountFreeMargin = accountFreeMargin;
	}

	@JsonProperty("_account_stopout_level")
	public Double getAccountStopoutLevel() {
		return accountStopoutLevel;
	}

	@JsonProperty("_account_stopout_level")
	public void setAccountStopoutLevel(Double accountStopoutLevel) {
		this.accountStopoutLevel = accountStopoutLevel;
	}

	@JsonProperty("_open_lots")
	public Double getOpenLots() {
		return openLots;
	}

	@JsonProperty("_open_lots")
	public void setOpenLots(Double openLots) {
		this.openLots = openLots;
	}

	@JsonProperty("_orders_total")
	public Double getOrdersTotal() {
		return ordersTotal;
	}

	@JsonProperty("_orders_total")
	public void setOrdersTotal(Double ordersTotal) {
		this.ordersTotal = ordersTotal;
	}

}
