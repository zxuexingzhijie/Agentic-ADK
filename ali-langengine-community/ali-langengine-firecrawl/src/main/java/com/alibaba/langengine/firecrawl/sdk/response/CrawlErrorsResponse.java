package com.alibaba.langengine.firecrawl.sdk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CrawlErrorsResponse {

	@JsonProperty("errors")
	private List<Error> errors;

	@JsonProperty("robotsBlocked")
	private List<String> robotsBlocked;

	// Getters and setters
	public List<Error> getErrors() {
		return errors;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

	public List<String> getRobotsBlocked() {
		return robotsBlocked;
	}

	public void setRobotsBlocked(List<String> robotsBlocked) {
		this.robotsBlocked = robotsBlocked;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Error {

		@JsonProperty("id")
		private String id;

		@JsonProperty("timestamp")
		private String timestamp;

		@JsonProperty("url")
		private String url;

		@JsonProperty("error")
		private String error;

		// Getters and setters
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

	}

}
