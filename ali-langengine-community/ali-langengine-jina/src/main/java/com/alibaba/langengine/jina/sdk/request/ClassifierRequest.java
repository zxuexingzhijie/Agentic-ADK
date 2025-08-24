package com.alibaba.langengine.jina.sdk.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Request object for classifier API
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassifierRequest {

	/**
	 * Model identifier
	 */
	@JsonProperty("model")
	private String model;

	/**
	 * Classifier identifier
	 */
	@JsonProperty("classifier_id")
	private String classifierId;

	/**
	 * Input data to classify
	 */
	@JsonProperty("input")
	private List<Object> input;

	/**
	 * Labels for classification
	 */
	@JsonProperty("labels")
	private List<String> labels;

	// Getters and setters
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getClassifierId() {
		return classifierId;
	}

	public void setClassifierId(String classifierId) {
		this.classifierId = classifierId;
	}

	public List<Object> getInput() {
		return input;
	}

	public void setInput(List<Object> input) {
		this.input = input;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

}
