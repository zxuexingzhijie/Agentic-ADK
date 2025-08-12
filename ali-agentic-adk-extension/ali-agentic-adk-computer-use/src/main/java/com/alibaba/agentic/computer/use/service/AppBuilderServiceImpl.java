package com.alibaba.agentic.computer.use.service;


import com.alibaba.agentic.computer.use.AtomicOperations;
import com.alibaba.agentic.computer.use.domain.BrowserUseRequest;
import com.alibaba.agentic.computer.use.domain.BrowserUseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppBuilderServiceImpl implements BrowserUseService{

    @Autowired
    private AtomicOperations atomicOperations;

//    public void submitUploadScriptTask(BrowserUseRequest browserUseRequest) {
//        atomicOperations.doScriptUpload(scriptStr, savePath);
//    }

    public BrowserUseResponse submitExecuteScriptTask(BrowserUseRequest browserUseRequest) {
        return atomicOperations.doScriptExecute(browserUseRequest);
    }

//    public void submitUploadAndExecuteScriptTask(BrowserUseRequest browserUseRequest) {
//        atomicOperations.doScriptUpload(scriptStr, savePath);
//        atomicOperations.doScriptExecute(new BrowserUseRequest());
//    }
}
