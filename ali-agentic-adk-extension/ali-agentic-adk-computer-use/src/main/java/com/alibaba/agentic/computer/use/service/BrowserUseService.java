package com.alibaba.agentic.computer.use.service;

import com.alibaba.agentic.computer.use.domain.BrowserUseRequest;
import com.alibaba.agentic.computer.use.domain.BrowserUseResponse;


public interface BrowserUseService {

//    void submitUploadScriptTask(BrowserUseRequest browserUseRequest);
//
    BrowserUseResponse submitExecuteScriptTask(BrowserUseRequest browserUseRequest);

//    void submitUploadAndExecuteScriptTask(BrowserUseRequest browserUseRequest);
}
