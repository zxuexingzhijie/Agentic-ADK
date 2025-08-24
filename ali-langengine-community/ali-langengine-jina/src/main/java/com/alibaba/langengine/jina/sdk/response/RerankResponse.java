package com.alibaba.langengine.jina.sdk.response;

import com.alibaba.langengine.jina.sdk.Usage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response object for reranker API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RerankResponse {

	/**
	 * Model identifier
	 */
	@JsonProperty("model")
	private String model;

	/**
	 * Usage information
	 */
	@JsonProperty("usage")
	private Usage usage;

	/**
	 * Reranking results
	 */
	@JsonProperty("results")
	private List<RerankResult> results;

	// Getters and setters
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Usage getUsage() {
		return usage;
	}

	public void setUsage(Usage usage) {
		this.usage = usage;
	}

	public List<RerankResult> getResults() {
		return results;
	}

	public void setResults(List<RerankResult> results) {
		this.results = results;
	}

	/**
	 * Individual rerank result
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class RerankResult {

		/**
		 * Index of the document
		 */
		@JsonProperty("index")
		private Integer index;

		/**
		 * Relevance score of the document
		 */
		@JsonProperty("relevance_score")
		private Double relevanceScore;

		// Getters and setters
		public Integer getIndex() {
			return index;
		}

		public void setIndex(Integer index) {
			this.index = index;
		}

		public Double getRelevanceScore() {
			return relevanceScore;
		}

		public void setRelevanceScore(Double relevanceScore) {
			this.relevanceScore = relevanceScore;
		}

	}

}
