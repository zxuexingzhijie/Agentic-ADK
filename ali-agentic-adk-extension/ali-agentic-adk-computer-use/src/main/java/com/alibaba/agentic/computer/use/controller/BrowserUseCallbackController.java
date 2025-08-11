package com.alibaba.agentic.computer.use.controller;


import com.alibaba.agentic.computer.use.dto.DomCallbackRequest;
import com.alibaba.agentic.computer.use.service.BrowserUseServiceCaller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/adk/browser/_callback")
@RestController
public class BrowserUseCallbackController {

    @Autowired
    private BrowserUseServiceCaller browserUseServiceCaller;

    @PostMapping("/dom")
    public void domCallback(@RequestBody DomCallbackRequest request ) {
//        log.warn("domCallback, fileName: {}, domJson: {}", request.getFileName(), request.getDomJson());
        browserUseServiceCaller.handleCallback(request.getFileName(), request.getDomJson());
    }

}
