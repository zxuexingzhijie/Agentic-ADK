package com.alibaba.langengine.firecrawl.sdk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {
    @JsonProperty("error")
    private String error;

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("code")
    private String code;

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
