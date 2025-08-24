package com.alibaba.langengine.jina.sdk.response;

import com.alibaba.langengine.jina.sdk.Usage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response object for classifier API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassifierResponse {

	/**
	 * Usage information
	 */
	@JsonProperty("usage")
	private Usage usage;

	/**
	 * Classification data
	 */
	@JsonProperty("data")
	private List<ClassificationData> data;

	// Getters and setters
	public Usage getUsage() {
		return usage;
	}

	public void setUsage(Usage usage) {
		this.usage = usage;
	}

	public List<ClassificationData> getData() {
		return data;
	}

	public void setData(List<ClassificationData> data) {
		this.data = data;
	}

	/**
	 * Classification data
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ClassificationData {

		/**
		 * Object type
		 */
		@JsonProperty("object")
		private String object;

		/**
		 * Index of the input
		 */
		@JsonProperty("index")
		private Integer index;

		/**
		 * Predicted label
		 */
		@JsonProperty("prediction")
		private String prediction;

		/**
		 * Confidence score
		 */
		@JsonProperty("score")
		private Double score;

		/**
		 * All predictions with scores
		 */
		@JsonProperty("predictions")
		private List<Prediction> predictions;

		// Getters and setters
		public String getObject() {
			return object;
		}

		public void setObject(String object) {
			this.object = object;
		}

		public Integer getIndex() {
			return index;
		}

		public void setIndex(Integer index) {
			this.index = index;
		}

		public String getPrediction() {
			return prediction;
		}

		public void setPrediction(String prediction) {
			this.prediction = prediction;
		}

		public Double getScore() {
			return score;
		}

		public void setScore(Double score) {
			this.score = score;
		}

		public List<Prediction> getPredictions() {
			return predictions;
		}

		public void setPredictions(List<Prediction> predictions) {
			this.predictions = predictions;
		}

	}

	/**
	 * Individual prediction
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Prediction {

		/**
		 * Label
		 */
		@JsonProperty("label")
		private String label;

		/**
		 * Score
		 */
		@JsonProperty("score")
		private Double score;

		// Getters and setters
		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public Double getScore() {
			return score;
		}

		public void setScore(Double score) {
			this.score = score;
		}

	}

}
