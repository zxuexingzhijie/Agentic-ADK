package com.alibaba.agentic.computer.use.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComputerDetailDTO {
    /**
     * 电脑的ComputerId
     */
    String desktopId;

    /**
     * 区域ID，默认值为"cn-hangzhou"
     */
    String regionId;

    Long resolutionWidth = 1920L;

    Long resolutionHeight = 1200L;

    Long monitorsConfig = 150L;

    String loginToken;

}
