package com.gracefl.propfirm.mt4.connection;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "ACCOUNT_INFORMATION" })
@Generated("jsonschema2pojo")
public class Mt4LiveAccountJSON {

	@JsonProperty("ACCOUNT_INFORMATION")
	private Mt4LiveAccountDetailJSON accountInformation;

	@JsonProperty("ACCOUNT_INFORMATION")
	public Mt4LiveAccountDetailJSON getAccountInformation() {
		return accountInformation;
	}

	@JsonProperty("ACCOUNT_INFORMATION")
	public void setAccountInformation(Mt4LiveAccountDetailJSON accountInformation) {
		this.accountInformation = accountInformation;
	}

}