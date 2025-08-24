package com.alibaba.langengine.jina.sdk.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request object for reranking documents
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RerankRequest {

	/**
	 * Identifier of the model to use
	 */
	@JsonProperty("model")
	private String model;

	/**
	 * The search query
	 */
	@JsonProperty("query")
	private Object query;

	/**
	 * A list of strings, TextDocs, and/or images to rerank
	 */
	@JsonProperty("documents")
	private Object documents;

	/**
	 * The number of most relevant documents or indices to return
	 */
	@JsonProperty("top_n")
	private Integer topN;

	/**
	 * If false, returns only the index and relevance score without the document text
	 */
	@JsonProperty("return_documents")
	private Boolean returnDocuments;

	// Getters and setters
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Object getQuery() {
		return query;
	}

	public void setQuery(Object query) {
		this.query = query;
	}

	public Object getDocuments() {
		return documents;
	}

	public void setDocuments(Object documents) {
		this.documents = documents;
	}

	public Integer getTopN() {
		return topN;
	}

	public void setTopN(Integer topN) {
		this.topN = topN;
	}

	public Boolean getReturnDocuments() {
		return returnDocuments;
	}

	public void setReturnDocuments(Boolean returnDocuments) {
		this.returnDocuments = returnDocuments;
	}

}
