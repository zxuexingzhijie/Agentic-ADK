package com.alibaba.agentic.computer.use.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
@Component
public class BrowserUseServiceCaller {

    private Map<String, CompletableFuture<String>> pendingRequest = new ConcurrentHashMap<>();

    public String callAndWait(String requestId, Runnable runnable) {
        CompletableFuture<String> future = new CompletableFuture<>();
        pendingRequest.put(requestId, future);

        //执行下发
        runnable.run();

        try {
            return future.get(120, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("callAndWait fail, requestId: {}",requestId, e);
            return null;
        }
    }

    public void call(String requestId, Runnable runnable) {
        CompletableFuture<String> future = new CompletableFuture<>();
        pendingRequest.put(requestId, future);
        //执行下发
        runnable.run();
    }

    public void handleCallback(String requestId, String result) {
        CompletableFuture<String> future = pendingRequest.get(requestId);
        if (future != null && !future.isDone()) {
            future.complete(result);
        }
    }

    public String getByRequestId(String requestId) {
        if(MapUtils.isEmpty(pendingRequest) || !pendingRequest.containsKey(requestId)) {
            return null;
        }
        try {
            return pendingRequest.get(requestId).get(120, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("getByRequestId fail, requestId: {}",requestId, e);
            return null;
        }
    }

}
