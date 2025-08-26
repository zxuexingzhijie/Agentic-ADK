package com.alibaba.langengine.firecrawl.sdk.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MapRequest {

	/**
	 * The base URL to start crawling from
	 */
	@JsonProperty("url")
	private String url;

	/**
	 * Specify a search query to order the results by relevance
	 */
	@JsonProperty("search")
	private String search;

	/**
	 * Sitemap mode when mapping
	 */
	@JsonProperty("sitemap")
	private String sitemap;

	/**
	 * Include subdomains of the website
	 */
	@JsonProperty("includeSubdomains")
	private Boolean includeSubdomains;

	/**
	 * Do not return URLs with query parameters
	 */
	@JsonProperty("ignoreQueryParameters")
	private Boolean ignoreQueryParameters;

	/**
	 * Maximum number of links to return
	 */
	@JsonProperty("limit")
	private Integer limit;

	/**
	 * Timeout in milliseconds
	 */
	@JsonProperty("timeout")
	private Integer timeout;

	// Getters and setters
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getSitemap() {
		return sitemap;
	}

	public void setSitemap(String sitemap) {
		this.sitemap = sitemap;
	}

	public Boolean getIncludeSubdomains() {
		return includeSubdomains;
	}

	public void setIncludeSubdomains(Boolean includeSubdomains) {
		this.includeSubdomains = includeSubdomains;
	}

	public Boolean getIgnoreQueryParameters() {
		return ignoreQueryParameters;
	}

	public void setIgnoreQueryParameters(Boolean ignoreQueryParameters) {
		this.ignoreQueryParameters = ignoreQueryParameters;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

}
