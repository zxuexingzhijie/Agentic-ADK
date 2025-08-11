package com.alibaba.agentic.computer.use.domain;

import lombok.Data;

@Data
public class BrowserUseResponse {

    private Boolean isSuccess;

    // 执行状态信息
    private String message;

    private String browserUseOutput;

    private Integer dropped;

    public BrowserUseResponse(Boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public BrowserUseResponse(Boolean isSuccess, String message, String browserUseOutput, Integer dropped) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.browserUseOutput = browserUseOutput;
        this.dropped = dropped;
    }

}
