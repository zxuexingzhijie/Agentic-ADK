/**
 * Copyright (C) 2024 AIDC-AI
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.langengine.firecrawl;

import com.alibaba.langengine.firecrawl.sdk.FireCrawlClient;
import com.alibaba.langengine.firecrawl.sdk.FireCrawlException;
import com.alibaba.langengine.firecrawl.sdk.request.BatchScrapeRequest;
import com.alibaba.langengine.firecrawl.sdk.request.MapRequest;
import com.alibaba.langengine.firecrawl.sdk.request.ScrapeRequest;
import com.alibaba.langengine.firecrawl.sdk.request.SearchRequest;
import com.alibaba.langengine.firecrawl.sdk.response.BatchScrapeResponse;
import com.alibaba.langengine.firecrawl.sdk.response.MapResponse;
import com.alibaba.langengine.firecrawl.sdk.response.ScrapeResponse;
import com.alibaba.langengine.firecrawl.sdk.response.SearchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@EnabledIfEnvironmentVariable(named = "FIRECRAWL_API_KEY", matches = ".*")
public class FireCrawlClientTest {
    
    private FireCrawlClient client;

    private static final String TEST_URL = "https://example.com";
    
    @BeforeEach
    public void setUp() {
        String apiKey = System.getenv("FIRECRAWL_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            // Disable tests if API key is not set
            throw new IllegalStateException("FIRECRAWL_API_KEY environment variable must be set to run tests");
        }
        client = new FireCrawlClient(apiKey);
    }
    
    @Test
    public void testScrapeWithDefaultOptions() throws FireCrawlException {
        ScrapeRequest request = new ScrapeRequest();
        request.setUrl(TEST_URL);
        
        ScrapeResponse response = client.scrape(request);
        
        assertNotNull(response);
        assertTrue(response.getSuccess());
        assertNotNull(response.getData());
        assertNotNull(response.getData().getMarkdown());
        assertFalse(response.getData().getMarkdown().isEmpty());
        assertNotNull(response.getData().getMetadata());
        assertNotNull(response.getData().getMetadata().getTitle());
    }
    
    @Test
    public void testScrapeWithRawHtmlFormat() throws FireCrawlException {
        ScrapeRequest request = new ScrapeRequest();
        request.setUrl(TEST_URL);
        
        // Set format to rawHtml
        List<Object> formats = new ArrayList<>();
        formats.add("rawHtml");
        request.setFormats(formats);
        
        ScrapeResponse response = client.scrape(request);
        
        assertNotNull(response);
        assertTrue(response.getSuccess());
        assertNotNull(response.getData());
        assertNotNull(response.getData().getRawHtml());
        assertFalse(response.getData().getRawHtml().isEmpty());
    }
    
    @Test
    public void testScrapeWithLinksFormat() throws FireCrawlException {
        ScrapeRequest request = new ScrapeRequest();
        request.setUrl(TEST_URL);
        
        // Set format to links
        List<Object> formats = new ArrayList<>();
        formats.add("links");
        request.setFormats(formats);
        
        ScrapeResponse response = client.scrape(request);
        
        assertNotNull(response);
        assertTrue(response.getSuccess());
        assertNotNull(response.getData());
        assertNotNull(response.getData().getLinks());
    }
    
    @Test
    public void testScrapeWithMultipleFormats() throws FireCrawlException {
        ScrapeRequest request = new ScrapeRequest();
        request.setUrl(TEST_URL);
        
        // Set multiple formats
        List<Object> formats = new ArrayList<>();
        formats.add("markdown");
        formats.add("html");
        formats.add("links");
        request.setFormats(formats);
        
        ScrapeResponse response = client.scrape(request);
        
        assertNotNull(response);
        assertTrue(response.getSuccess());
        assertNotNull(response.getData());
        // Check that all formats are returned
        assertNotNull(response.getData().getMarkdown());
        assertFalse(response.getData().getMarkdown().isEmpty());
        assertNotNull(response.getData().getHtml());
        assertFalse(response.getData().getHtml().isEmpty());
        assertNotNull(response.getData().getLinks());
    }

    @Test
    public void testBatchScrapeWithFormats() throws FireCrawlException {

        BatchScrapeRequest request = new BatchScrapeRequest();
        request.setUrls(Collections.singletonList("https://example.com"));
        request.setFormats(Arrays.asList("markdown", "html"));
        request.setIgnoreInvalidURLs(true);

        BatchScrapeResponse response = client.batchScrape(request);

        assertNotNull("Response should not be null", response);
        assertTrue("Response should be successful", response.getSuccess());
        assertNotNull("Response ID should not be null", response.getId());
    }

    @Test
    public void testGetBatchScrapeStatusWithInvalidId() {
        try {
            // Test with an invalid ID to check error handling
            client.getBatchScrapeStatus("invalid-id");
            fail("Expected FireCrawlException to be thrown");
        } catch (FireCrawlException e) {
            // Expected exception
            assertNotNull(e);
        }
    }

    @Test
    public void testCancelBatchScrapeWithInvalidId() {
        try {
            // Test with an invalid ID to check error handling
            client.cancelBatchScrape("invalid-id");
            fail("Expected FireCrawlException to be thrown");
        } catch (FireCrawlException e) {
            // Expected exception
            assertNotNull(e);
        }
    }

    @Test
    public void testSearchWeb() throws FireCrawlException {
        SearchRequest request = new SearchRequest();
        request.setQuery("firecrawl");
        request.setLimit(5);

        SearchResponse response = client.search(request);

        assertNotNull("Response should not be null", response);
        assertTrue("Request should be successful", response.getSuccess());
        assertNotNull("Data should not be null", response.getData());
        assertNotNull("Web results should not be null", response.getData().getWeb());
        assertTrue("Should have at least one web result", response.getData().getWeb().length > 0);

        SearchResponse.WebResult firstResult = response.getData().getWeb()[0];
        assertNotNull("First result title should not be null", firstResult.getTitle());
        assertNotNull("First result URL should not be null", firstResult.getUrl());
        assertNotNull("First result description should not be null", firstResult.getDescription());
    }

    @Test
    public void testSearchWithScrapeOptions() throws FireCrawlException {
        SearchRequest request = new SearchRequest();
        request.setQuery("firecrawl java sdk");
        request.setLimit(3);

        SearchRequest.ScrapeOptions scrapeOptions = new SearchRequest.ScrapeOptions();
        scrapeOptions.setOnlyMainContent(true);
        request.setScrapeOptions(scrapeOptions);

        SearchResponse response = client.search(request);

        assertNotNull("Response should not be null", response);
        assertTrue("Request should be successful", response.getSuccess());
        assertNotNull("Data should not be null", response.getData());
        assertNotNull("Web results should not be null", response.getData().getWeb());
    }

    @Test
    public void testMap() throws FireCrawlException {
        MapRequest request = new MapRequest();
        request.setUrl("https://firecrawl.dev");
        request.setLimit(10);

        MapResponse response = client.map(request);

        assertNotNull("Response should not be null", response);
        assertTrue("Request should be successful", response.getSuccess());
        assertNotNull("Links should not be null", response.getLinks());
        assertFalse("Links should not be empty", response.getLinks().isEmpty());

        MapResponse.Link firstLink = response.getLinks().get(0);
        assertNotNull("URL should not be null", firstLink.getUrl());
    }

}
