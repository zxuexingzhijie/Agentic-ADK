package com.alibaba.agentic.computer.use.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class ResultDO<T> implements Serializable {
    private boolean success;
    private T data;
    private Integer code;
    private String message;
    private String detailMessage;

    public ResultDO() {
        this.success = false;
        this.data = null;
        this.code = null;
        this.message = null;
        this.detailMessage = null;
    }

    public ResultDO(boolean success, T data, Integer code, String message) {
        this(success, data, code, message, null);
    }

    public ResultDO(boolean success, T data, Integer code, String message, String detailMessage) {
        this.success = success;
        this.data = data;
        this.code = code;
        this.message = message;
        this.detailMessage = detailMessage;
    }

    public static <T> ResultDO<T> success() {
        return new ResultDO<T>(true, null, null, null);
    }

    public static <T> ResultDO<T> success(T data) {
        return new ResultDO<T>(true, data, null, null);
    }

    public static <T> ResultDO<T> error(String message) {
        return new ResultDO<T>(false, null, null, message);
    }

    public static <T> ResultDO<T> error(Integer code, String message) {
        return new ResultDO<T>(false, null, code, message);
    }
}
