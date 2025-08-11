package com.alibaba.agentic.computer.use.enums;

public enum ScriptExecuteStatusEnum {

    SUCCESS("Success"),
    FAILED("Failed"),
    TIMEOUT("Timeout");

    private String message;

    ScriptExecuteStatusEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
