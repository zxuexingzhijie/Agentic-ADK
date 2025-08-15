package com.alibaba.langengine.aliyunaisearch.sdk;

import java.util.Objects;

/**
 * Aliyun Web Search Client Configuration Class
 * Documentation: Getting Service Access Address, API-KEY Authentication
 */
public class ClientConfig {
    /** Service access address (public network or VPC, documentation provides example domain names) */
    private final String host;
    /** API-Key (format: Bearer OS-xxx, Authorization field in documentation) */
    private final String apiKey;
    /** Workspace name (default: default, workspace_name parameter in documentation) */
    private final String workspaceName;
    /** Service ID (system built-in: ops-web-search-001, service_id parameter in documentation) */
    private final String serviceId;
    /** Connection timeout (default: 30 seconds) */
    private final int connectTimeoutSeconds;
    /** Read timeout (default: 60 seconds) */
    private final int readTimeoutSeconds;

    // Constructor (Builder pattern for easy configuration extension)
    private ClientConfig(Builder builder) {
        this.host = Objects.requireNonNull(builder.host, "Host cannot be null (public/VPC address)");
        this.apiKey = Objects.requireNonNull(builder.apiKey, "API-Key cannot be null (format: Bearer OS-xxx)");
        this.workspaceName = builder.workspaceName == null ? "default" : builder.workspaceName;
        this.serviceId = builder.serviceId == null ? "ops-web-search-001" : builder.serviceId;
        this.connectTimeoutSeconds = builder.connectTimeoutSeconds <= 0 ? 30 : builder.connectTimeoutSeconds;
        this.readTimeoutSeconds = builder.readTimeoutSeconds <= 0 ? 60 : builder.readTimeoutSeconds;
    }

    // Builder static inner class
    public static class Builder {
        private String host;
        private String apiKey;
        private String workspaceName;
        private String serviceId;
        private int connectTimeoutSeconds;
        private int readTimeoutSeconds;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder workspaceName(String workspaceName) {
            this.workspaceName = workspaceName;
            return this;
        }

        public Builder serviceId(String serviceId) {
            this.serviceId = serviceId;
            return this;
        }

        public Builder connectTimeoutSeconds(int connectTimeoutSeconds) {
            this.connectTimeoutSeconds = connectTimeoutSeconds;
            return this;
        }

        public Builder readTimeoutSeconds(int readTimeoutSeconds) {
            this.readTimeoutSeconds = readTimeoutSeconds;
            return this;
        }

        public ClientConfig build() {
            return new ClientConfig(this);
        }
    }

    // Getter methods (configuration is immutable for security)
    public String getHost() { return host; }
    public String getApiKey() { return apiKey; }
    public String getWorkspaceName() { return workspaceName; }
    public String getServiceId() { return serviceId; }
    public int getConnectTimeoutSeconds() { return connectTimeoutSeconds; }
    public int getReadTimeoutSeconds() { return readTimeoutSeconds; }
}