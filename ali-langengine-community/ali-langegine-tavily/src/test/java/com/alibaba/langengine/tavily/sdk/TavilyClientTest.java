package com.alibaba.langengine.tavily.sdk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TavilyClientTest {

    @Test
    void search() {
        // Test search functionality using the default API key
        TavilyClient client = new TavilyClient();
        assertDoesNotThrow(() -> {
            SearchResponse response = client.search("Artificial Intelligence");
            assertNotNull(response);
            // Additional assertions can be added to validate the response content
        });
    }

    @Test
    void testSearch() {
        // Test search functionality using a custom SearchRequest
        TavilyClient client = new TavilyClient();
        SearchRequest request = new SearchRequest();
        request.setQuery("Machine Learning");
        request.setSearchDepth("basic");
        request.setIncludeAnswer(true);
        
        assertDoesNotThrow(() -> {
            SearchResponse response = client.search(request);
            assertNotNull(response);
            // Additional assertions can be added to validate the response content
        });
    }
}