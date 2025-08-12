package com.alibaba.agentic.computer.use.dto;

import com.google.genai.types.Content;
import lombok.Data;

@Data
public class BrowserAgentRunRequest {

    public String appName;
    public String userId;
    public String sessionId;
    public Content newMessage;
    public Boolean streaming = false;

}
