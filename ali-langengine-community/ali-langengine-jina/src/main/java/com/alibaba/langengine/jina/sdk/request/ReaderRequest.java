package com.alibaba.langengine.jina.sdk.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request object for reader API
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReaderRequest {

	/**
	 * URL to read
	 */
	@JsonProperty("url")
	private String url;

	/**
	 * Viewport settings for browser rendering
	 */
	@JsonProperty("viewport")
	private Viewport viewport;

	/**
	 * JavaScript code to inject before reading
	 */
	@JsonProperty("injectPageScript")
	private String injectPageScript;

	// Getters and setters
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Viewport getViewport() {
		return viewport;
	}

	public void setViewport(Viewport viewport) {
		this.viewport = viewport;
	}

	public String getInjectPageScript() {
		return injectPageScript;
	}

	public void setInjectPageScript(String injectPageScript) {
		this.injectPageScript = injectPageScript;
	}

	/**
	 * Viewport dimensions
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Viewport {

		/**
		 * Width of the viewport
		 */
		@JsonProperty("width")
		private Integer width;

		/**
		 * Height of the viewport
		 */
		@JsonProperty("height")
		private Integer height;

		// Getters and setters
		public Integer getWidth() {
			return width;
		}

		public void setWidth(Integer width) {
			this.width = width;
		}

		public Integer getHeight() {
			return height;
		}

		public void setHeight(Integer height) {
			this.height = height;
		}

	}

}
