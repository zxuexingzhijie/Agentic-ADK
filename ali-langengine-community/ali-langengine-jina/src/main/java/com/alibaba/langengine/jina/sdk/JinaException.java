package com.alibaba.langengine.jina.sdk;

public class JinaException extends RuntimeException {

	public JinaException(String message) {
		super(message);
	}

	public JinaException(String message, Throwable cause) {
		super(message, cause);
	}

}
