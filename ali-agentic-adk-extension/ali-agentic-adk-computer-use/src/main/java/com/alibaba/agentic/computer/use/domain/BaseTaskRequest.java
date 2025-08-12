package com.alibaba.agentic.computer.use.domain;

import lombok.Data;

@Data
public abstract class BaseTaskRequest {

    private String uploadPath;

    private Object content;

}
