package com.alibaba.langengine.firecrawl.sdk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MapResponse {

	/**
	 * Indicates if the request was successful
	 */
	@JsonProperty("success")
	private Boolean success;

	/**
	 * List of discovered links
	 */
	@JsonProperty("links")
	private List<Link> links;

	// Getters and setters
	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Link {

		/**
		 * URL of the page
		 */
		@JsonProperty("url")
		private String url;

		/**
		 * Title of the page, if available
		 */
		@JsonProperty("title")
		private String title;

		/**
		 * Description of the page, if available
		 */
		@JsonProperty("description")
		private String description;

		// Getters and setters
		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

	}

}
