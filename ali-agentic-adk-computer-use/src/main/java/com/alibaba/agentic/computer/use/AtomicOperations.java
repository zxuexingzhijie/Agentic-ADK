package com.alibaba.agentic.computer.use;


import com.alibaba.agentic.computer.use.domain.BrowserUseRequest;
import com.alibaba.agentic.computer.use.domain.BrowserUseResponse;

public interface AtomicOperations {

    String doScriptUpload(String scriptStr, String savePath);

    BrowserUseResponse doScriptExecute(BrowserUseRequest request);
}
