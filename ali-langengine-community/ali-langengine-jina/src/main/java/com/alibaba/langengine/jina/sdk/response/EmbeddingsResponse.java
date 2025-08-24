package com.alibaba.langengine.jina.sdk.response;

import com.alibaba.langengine.jina.sdk.Usage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response object for embeddings API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmbeddingsResponse {

	/**
	 * Array of embedding data
	 */
	@JsonProperty("data")
	private List<EmbeddingData> data;

	/**
	 * Usage information
	 */
	@JsonProperty("usage")
	private Usage usage;

	// Getters and setters
	public List<EmbeddingData> getData() {
		return data;
	}

	public void setData(List<EmbeddingData> data) {
		this.data = data;
	}

	public Usage getUsage() {
		return usage;
	}

	public void setUsage(Usage usage) {
		this.usage = usage;
	}

	/**
	 * Embedding data object
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class EmbeddingData {

		/**
		 * The embedding vector
		 */
		@JsonProperty("embedding")
		private Object embedding;

		// Getters and setters
		public Object getEmbedding() {
			return embedding;
		}

		public void setEmbedding(Object embedding) {
			this.embedding = embedding;
		}

	}

}
