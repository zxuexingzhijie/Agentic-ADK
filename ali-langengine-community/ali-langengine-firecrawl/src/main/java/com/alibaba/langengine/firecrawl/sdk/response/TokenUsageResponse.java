package com.alibaba.langengine.firecrawl.sdk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response object for token usage API call
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenUsageResponse {

	/**
	 * Indicates if the request was successful
	 */
	@JsonProperty("success")
	private Boolean success;

	/**
	 * Contains the data payload with token information
	 */
	@JsonProperty("data")
	private Data data;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	/**
	 * Data payload containing token usage information
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Data {

		/**
		 * Number of tokens remaining for the team
		 */
		@JsonProperty("remainingTokens")
		private Integer remainingTokens;

		public Integer getRemainingTokens() {
			return remainingTokens;
		}

		public void setRemainingTokens(Integer remainingTokens) {
			this.remainingTokens = remainingTokens;
		}

	}

}
