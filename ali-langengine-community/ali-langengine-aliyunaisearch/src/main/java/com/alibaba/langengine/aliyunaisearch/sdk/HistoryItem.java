package com.alibaba.langengine.aliyunaisearch.sdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

/**
 * Single element of conversation history (corresponds to List element of history parameter in documentation)
 */
public class HistoryItem {
    // Role enumeration (document limitation: system, user, assistant)
    public enum Role {
        @SerializedName("system") SYSTEM,
        @SerializedName("user") USER,
        @SerializedName("assistant") ASSISTANT
    }

    @SerializedName("role")
    private final Role role;
    @SerializedName("content")
    private final String content;

    public HistoryItem(Role role, String content) {
        this.role = Objects.requireNonNull(role, "Conversation role cannot be null");
        this.content = Objects.requireNonNull(content, "Conversation content cannot be null");
    }

    // Getter
    public Role getRole() { return role; }
    public String getContent() { return content; }
}

/**
 * Search result filtering mode (corresponds to optional values of way parameter in documentation)
 */
enum SearchWayEnum {
    @SerializedName("normal") NORMAL,
    @SerializedName("fast") FAST,
    @SerializedName("full") FULL
}