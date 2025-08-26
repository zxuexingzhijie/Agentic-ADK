package com.alibaba.langengine.firecrawl.sdk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response object for credit usage API call
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditUsageResponse {

	/**
	 * Indicates if the request was successful
	 */
	@JsonProperty("success")
	private Boolean success;

	/**
	 * Contains the data payload with credit information
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
	 * Data payload containing credit usage information
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Data {

		/**
		 * Number of credits remaining for the team
		 */
		@JsonProperty("remainingCredits")
		private Integer remainingCredits;

		public Integer getRemainingCredits() {
			return remainingCredits;
		}

		public void setRemainingCredits(Integer remainingCredits) {
			this.remainingCredits = remainingCredits;
		}

	}

}
