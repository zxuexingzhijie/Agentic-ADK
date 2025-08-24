package com.alibaba.langengine.jina.sdk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Usage information
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Usage {

	/**
	 * Total tokens used
	 */
	@JsonProperty("total_tokens")
	private Integer totalTokens;

	/**
	 * Tokens used
	 */
	@JsonProperty("tokens")
	private Integer tokens;

	// Getters and setters
	public Integer getTotalTokens() {
		return totalTokens;
	}

	public void setTotalTokens(Integer totalTokens) {
		this.totalTokens = totalTokens;
	}

	public Integer getTokens() {
		return tokens;
	}

	public void setTokens(Integer tokens) {
		this.tokens = tokens;
	}

}
