package com.alibaba.agentic.computer.use.domain;

import lombok.Data;

import javax.annotation.Nullable;

@Data
public class BrowserUseRequest {

    private String command;

    private String regionId;

    private String endpoint;

    // 如为空，则从application.properties中获取
    @Nullable
    private String computerResourceId;

    private Integer timeout;
}
