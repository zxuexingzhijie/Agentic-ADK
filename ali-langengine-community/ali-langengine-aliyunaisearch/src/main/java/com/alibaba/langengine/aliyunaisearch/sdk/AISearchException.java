package com.alibaba.langengine.aliyunaisearch.sdk;


/**
 * Aliyun Web Search Exception Class
 */
public class AISearchException extends RuntimeException {
    private final String requestId;  // Exception request ID (for Aliyun technical support)
    private final String errorCode;  // Error code (e.g. InvalidParameter)
    private final Integer httpCode;  // HTTP status code (e.g. 400, 500)

    // Build exception from error response
    public AISearchException(WebSearchErrorResponse errorResponse) {
        super(String.format("Request failed (RequestId: %s): %s (Error code: %s, HTTP status code: %d)",
                errorResponse.getRequestId(),
                errorResponse.getMessage(),
                errorResponse.getCode(),
                errorResponse.getHttpCode()));
        this.requestId = errorResponse.getRequestId();
        this.errorCode = errorResponse.getCode();
        this.httpCode = errorResponse.getHttpCode();
    }

    // Custom exception message
    public AISearchException(String message, Throwable cause) {
        super(message, cause);
        this.requestId = null;
        this.errorCode = null;
        this.httpCode = null;
    }

    // Getter
    public String getRequestId() { return requestId; }
    public String getErrorCode() { return errorCode; }
    public Integer getHttpCode() { return httpCode; }
}