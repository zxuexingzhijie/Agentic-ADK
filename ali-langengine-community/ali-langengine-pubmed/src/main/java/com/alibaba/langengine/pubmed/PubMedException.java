package com.alibaba.langengine.pubmed;

public class PubMedException extends Exception {
    public PubMedException(String message) {
        super(message);
    }
    public PubMedException(String message, Throwable cause) {
        super(message, cause);
    }
}
