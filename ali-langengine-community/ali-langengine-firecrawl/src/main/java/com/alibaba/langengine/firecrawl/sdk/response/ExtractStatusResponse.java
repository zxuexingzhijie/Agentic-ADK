package com.alibaba.langengine.firecrawl.sdk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtractStatusResponse {

	/**
	 * Indicates if the request was successful
	 */
	@JsonProperty("success")
	private Boolean success;

	/**
	 * Extracted data
	 */
	@JsonProperty("data")
	private Map<String, Object> data;

	/**
	 * The current status of the extract job
	 */
	@JsonProperty("status")
	private String status;

	/**
	 * Expiration date of the extract job
	 */
	@JsonProperty("expiresAt")
	private Date expiresAt;

	/**
	 * The number of tokens used by the extract job
	 */
	@JsonProperty("tokensUsed")
	private Integer tokensUsed;

	// Getters and setters
	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}

	public Integer getTokensUsed() {
		return tokensUsed;
	}

	public void setTokensUsed(Integer tokensUsed) {
		this.tokensUsed = tokensUsed;
	}

}
