package com.gracefl.propfirm.mt4.connection;

import java.util.List;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "_action", "_trades" })
@Generated("jsonschema2pojo")
public class Mt4ClientTerminalTradeObject {
	
	@JsonProperty("_action")
	private String action;
	@JsonProperty("_trades")
	private List<Mt4TradesObject> trades = null;

	@JsonProperty("_action")
	public String getAction() {
	return action;
	}

	@JsonProperty("_action")
	public void setAction(String action) {
	this.action = action;
	}

	@JsonProperty("_trades")
	public List<Mt4TradesObject> getTrades() {
	return trades;
	}

	@JsonProperty("_trades")
	public void setTrades(List<Mt4TradesObject> trades) {
	this.trades = trades;
	}

}