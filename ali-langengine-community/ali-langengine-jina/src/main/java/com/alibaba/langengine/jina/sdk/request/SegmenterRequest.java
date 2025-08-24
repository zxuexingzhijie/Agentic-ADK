package com.alibaba.langengine.jina.sdk.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request object for segmenter API
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SegmenterRequest {

	/**
	 * Content to segment
	 */
	@JsonProperty("content")
	private String content;

	/**
	 * Tokenizer to use
	 */
	@JsonProperty("tokenizer")
	private String tokenizer;

	/**
	 * Whether to return tokens
	 */
	@JsonProperty("return_tokens")
	private Boolean returnTokens;

	/**
	 * Whether to return chunks
	 */
	@JsonProperty("return_chunks")
	private Boolean returnChunks;

	/**
	 * Maximum chunk length
	 */
	@JsonProperty("max_chunk_length")
	private Integer maxChunkLength;

	/**
	 * Number of tokens from head
	 */
	@JsonProperty("head")
	private Integer head;

	/**
	 * Number of tokens from tail
	 */
	@JsonProperty("tail")
	private Integer tail;

	// Getters and setters
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTokenizer() {
		return tokenizer;
	}

	public void setTokenizer(String tokenizer) {
		this.tokenizer = tokenizer;
	}

	public Boolean getReturnTokens() {
		return returnTokens;
	}

	public void setReturnTokens(Boolean returnTokens) {
		this.returnTokens = returnTokens;
	}

	public Boolean getReturnChunks() {
		return returnChunks;
	}

	public void setReturnChunks(Boolean returnChunks) {
		this.returnChunks = returnChunks;
	}

	public Integer getMaxChunkLength() {
		return maxChunkLength;
	}

	public void setMaxChunkLength(Integer maxChunkLength) {
		this.maxChunkLength = maxChunkLength;
	}

	public Integer getHead() {
		return head;
	}

	public void setHead(Integer head) {
		this.head = head;
	}

	public Integer getTail() {
		return tail;
	}

	public void setTail(Integer tail) {
		this.tail = tail;
	}

}
