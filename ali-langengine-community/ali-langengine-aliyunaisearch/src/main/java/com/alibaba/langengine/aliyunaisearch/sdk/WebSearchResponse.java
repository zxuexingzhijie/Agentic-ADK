package com.alibaba.langengine.aliyunaisearch.sdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Normal response result (corresponds to "normal response example" in documentation)
 */
public class WebSearchResponse {
    @SerializedName("result")
    private Result result;
    @SerializedName("usage")
    private Usage usage;

    // Inner class: Result (corresponds to result parameter in documentation)
    public static class Result {
        @SerializedName("search_result")
        private List<SearchResult> searchResult;

        // Getter
        public List<SearchResult> getSearchResult() { return searchResult; }
    }

    // Getter
    public Result getResult() { return result; }
    public Usage getUsage() { return usage; }
}