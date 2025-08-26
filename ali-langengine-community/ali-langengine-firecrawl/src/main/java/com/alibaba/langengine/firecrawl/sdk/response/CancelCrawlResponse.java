package com.alibaba.langengine.firecrawl.sdk.response;

// CancelCrawlResponse.java

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CancelCrawlResponse {

	@JsonProperty("status")
	private String status;

	// Getters and setters
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
