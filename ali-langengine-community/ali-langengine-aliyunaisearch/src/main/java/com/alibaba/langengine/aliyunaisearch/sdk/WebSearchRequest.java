package com.alibaba.langengine.aliyunaisearch.sdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

/**
 * Web search request body (corresponds to Body parameter in documentation)
 */
public class WebSearchRequest {
    // Search term (required)
    @SerializedName("query")
    private final String query;
    // Filtering mode (default: fast)
    @SerializedName("way")
    private final SearchWayEnum way;
    // Whether to enable LLM to rewrite query (default: true)
    @SerializedName("query_rewrite")
    private final Boolean queryRewrite;
    // Number of results to return (default: 5)
    @SerializedName("top_k")
    private final Integer topK;
    // Conversation history (default: null)
    @SerializedName("history")
    private final List<HistoryItem> history;

    // Builder pattern
    private WebSearchRequest(Builder builder) {
        this.query = Objects.requireNonNull(builder.query, "Search term (query) cannot be null");
        this.way = builder.way == null ? SearchWayEnum.FAST : builder.way;
        this.queryRewrite = builder.queryRewrite == null ? Boolean.TRUE : builder.queryRewrite;
        this.topK = builder.topK == null || builder.topK <= 0 ? 5 : builder.topK;
        this.history = builder.history;
    }

    public static class Builder {
        private String query;
        private SearchWayEnum way;
        private Boolean queryRewrite;
        private Integer topK;
        private List<HistoryItem> history;

        public Builder query(String query) {
            this.query = query;
            return this;
        }

        public Builder way(SearchWayEnum way) {
            this.way = way;
            return this;
        }

        public Builder queryRewrite(Boolean queryRewrite) {
            this.queryRewrite = queryRewrite;
            return this;
        }

        public Builder topK(Integer topK) {
            this.topK = topK;
            return this;
        }

        public Builder history(List<HistoryItem> history) {
            this.history = history;
            return this;
        }

        public WebSearchRequest build() {
            return new WebSearchRequest(this);
        }
    }

    // Getter
    public String getQuery() { return query; }
    public SearchWayEnum getWay() { return way; }
    public Boolean getQueryRewrite() { return queryRewrite; }
    public Integer getTopK() { return topK; }
    public List<HistoryItem> getHistory() { return history; }
}