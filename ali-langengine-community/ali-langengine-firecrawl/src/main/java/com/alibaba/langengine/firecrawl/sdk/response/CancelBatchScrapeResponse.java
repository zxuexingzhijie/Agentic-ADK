package com.alibaba.langengine.firecrawl.sdk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response object for cancel batch scrape operation
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CancelBatchScrapeResponse {

	/**
	 * Indicates if the cancellation was successful
	 */
	@JsonProperty("success")
	private Boolean success;

	/**
	 * Message describing the result of the cancellation
	 */
	@JsonProperty("message")
	private String message;

	// Getters and setters
	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
