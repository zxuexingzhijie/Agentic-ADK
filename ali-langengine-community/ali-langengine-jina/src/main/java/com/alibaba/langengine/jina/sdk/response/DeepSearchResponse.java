package com.alibaba.langengine.jina.sdk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response object for deep search API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeepSearchResponse {

	/**
	 * Response ID
	 */
	@JsonProperty("id")
	private String id;

	/**
	 * Object type
	 */
	@JsonProperty("object")
	private String object;

	/**
	 * Creation timestamp
	 */
	@JsonProperty("created")
	private Long created;

	/**
	 * Model used
	 */
	@JsonProperty("model")
	private String model;

	/**
	 * System fingerprint
	 */
	@JsonProperty("system_fingerprint")
	private String systemFingerprint;

	/**
	 * Response choices
	 */
	@JsonProperty("choices")
	private Object choices;

	// Getters and setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSystemFingerprint() {
		return systemFingerprint;
	}

	public void setSystemFingerprint(String systemFingerprint) {
		this.systemFingerprint = systemFingerprint;
	}

	public Object getChoices() {
		return choices;
	}

	public void setChoices(Object choices) {
		this.choices = choices;
	}

}
