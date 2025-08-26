package com.alibaba.langengine.firecrawl.sdk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtractResponse {

	/**
	 * Indicates if the request was successful
	 */
	@JsonProperty("success")
	private Boolean success;

	/**
	 * The ID of the extract job
	 */
	@JsonProperty("id")
	private String id;

	/**
	 * Array containing the invalid URLs that were specified in the request
	 */
	@JsonProperty("invalidURLs")
	private List<String> invalidURLs;

	// Getters and setters
	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getInvalidURLs() {
		return invalidURLs;
	}

	public void setInvalidURLs(List<String> invalidURLs) {
		this.invalidURLs = invalidURLs;
	}

}
