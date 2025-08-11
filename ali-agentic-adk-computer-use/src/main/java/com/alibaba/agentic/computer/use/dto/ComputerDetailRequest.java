package com.alibaba.agentic.computer.use.dto;

import lombok.Data;

@Data
public class ComputerDetailRequest {
    /**
     * 应用agentId
     */
    String agentId;


    /**
     * 工号
     */
    String workNo;

    /**
     * 是否获取authCode值
     */
    Boolean isGetAuthCode = true;
}
