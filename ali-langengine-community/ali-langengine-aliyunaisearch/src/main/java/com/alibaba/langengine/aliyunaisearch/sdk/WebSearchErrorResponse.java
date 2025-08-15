package com.alibaba.langengine.aliyunaisearch.sdk;

import com.google.gson.annotations.SerializedName;

/**
 * Exception response result (corresponds to "exception response example" in documentation)
 */
public class WebSearchErrorResponse {
    @SerializedName("request_id")
    private String requestId;   // Request ID (for troubleshooting)
    @SerializedName("latency")
    private Double latency;     // Response latency (seconds)
    @SerializedName("code")
    private String code;        // Error code (e.g. InvalidParameter)
    @SerializedName("http_code")
    private Integer httpCode;   // HTTP status code
    @SerializedName("message")
    private String message;     // Error message

    // Getter
    public String getRequestId() { return requestId; }
    public Double getLatency() { return latency; }
    public String getCode() { return code; }
    public Integer getHttpCode() { return httpCode; }
    public String getMessage() { return message; }
}