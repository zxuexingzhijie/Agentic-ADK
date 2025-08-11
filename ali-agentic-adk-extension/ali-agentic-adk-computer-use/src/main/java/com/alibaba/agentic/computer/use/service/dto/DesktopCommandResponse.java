package com.alibaba.agentic.computer.use.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DesktopCommandResponse implements Serializable {
    private static final long serialVersionUID = -4569620632167589077L;
    private String output;

    private String computerId;

    private String finishTime;

    private String invocationStatus;

    private Integer dropped;

}
