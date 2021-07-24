package com.gracefl.propfirm.mt4.connection;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "_ticket", "_magic", "_symbol", "_lots", "_type", "_open_price", "_open_time", "_SL", "_TP", "_pnl",
		"_comment" })
@Generated("jsonschema2pojo")
public class Mt4TradesObject {
	@JsonProperty("_ticket")
	private Double ticket;
	@JsonProperty("_magic")
	private Double magic;
	@JsonProperty("_symbol")
	private String symbol;
	@JsonProperty("_lots")
	private Double lots;
	@JsonProperty("_type")
	private Double type;
	@JsonProperty("_open_price")
	private Double openPrice;
	@JsonProperty("_open_time")
	private String openTime;
	@JsonProperty("_SL")
	private Double stopLoss;
	@JsonProperty("_TP")
	private Double takeProfit;
	@JsonProperty("_pnl")
	private Double profit;
	@JsonProperty("_comment")
	private String comment;
	@JsonProperty("_close_price")
	private Double closePrice;
	@JsonProperty("_close_time")
	private String closeTime;
	@JsonProperty("_commission")
	private Double commission;
	@JsonProperty("_swap")
	private Double swap;
	
	@JsonProperty("_ticket")
	public Double getTicket() {
	return ticket;
	}

	@JsonProperty("_ticket")
	public void setTicket(Double ticket) {
	this.ticket = ticket;
	}

	@JsonProperty("_magic")
	public Double getMagic() {
	return magic;
	}

	@JsonProperty("_magic")
	public void setMagic(Double magic) {
	this.magic = magic;
	}

	@JsonProperty("_symbol")
	public String getSymbol() {
	return symbol;
	}

	@JsonProperty("_symbol")
	public void setSymbol(String symbol) {
	this.symbol = symbol;
	}

	@JsonProperty("_lots")
	public Double getLots() {
	return lots;
	}

	@JsonProperty("_lots")
	public void setLots(Double lots) {
	this.lots = lots;
	}

	@JsonProperty("_type")
	public Double getType() {
	return type;
	}

	@JsonProperty("_type")
	public void setType(Double type) {
	this.type = type;
	}

	@JsonProperty("_open_price")
	public Double getOpenPrice() {
	return openPrice;
	}

	@JsonProperty("_open_price")
	public void setOpenPrice(Double openPrice) {
	this.openPrice = openPrice;
	}

	@JsonProperty("_open_time")
	public String getOpenTime() {
	return openTime;
	}

	@JsonProperty("_open_time")
	public void setOpenTime(String openTime) {
	this.openTime = openTime;
	}

	@JsonProperty("_SL")
	public Double getStopLoss() {
	return stopLoss;
	}

	@JsonProperty("_SL")
	public void setStopLoss(Double sl) {
	this.stopLoss = sl;
	}

	public Double getTakeProfit() {
		return takeProfit;
	}

	public void setTakeProfit(Double takeProfit) {
		this.takeProfit = takeProfit;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	@JsonProperty("_comment")
	public String getComment() {
	return comment;
	}

	@JsonProperty("_comment")
	public void setComment(String comment) {
	this.comment = comment;
	}

	public Double getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(Double closePrice) {
		this.closePrice = closePrice;
	}

	public String getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}

	public Double getSwap() {
		return swap;
	}

	public void setSwap(Double swap) {
		this.swap = swap;
	}

	
}
