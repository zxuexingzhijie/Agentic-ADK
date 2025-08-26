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
import com.alibaba.langengine.firecrawl.sdk.request.ScrapeRequest;
import com.alibaba.langengine.firecrawl.sdk.response.ScrapeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@EnabledIfEnvironmentVariable(named = "FIRECRAWL_API_KEY", matches = ".*")
public class FireCrawlClientTest {
    
    private FireCrawlClient client;

    private static final String TEST_URL = "https://example.com";
    
    @BeforeEach
    public void setUp() {
        String apiKey = System.getenv("FIRECRAWL_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            // 如果没有设置API密钥，则禁用测试
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
        
        // 设置格式为rawHtml
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
        
        // 设置格式为links
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
        
        // 设置多种格式
        List<Object> formats = new ArrayList<>();
        formats.add("markdown");
        formats.add("html");
        formats.add("links");
        request.setFormats(formats);
        
        ScrapeResponse response = client.scrape(request);
        
        assertNotNull(response);
        assertTrue(response.getSuccess());
        assertNotNull(response.getData());
        // 检查所有格式都返回了
        assertNotNull(response.getData().getMarkdown());
        assertFalse(response.getData().getMarkdown().isEmpty());
        assertNotNull(response.getData().getHtml());
        assertFalse(response.getData().getHtml().isEmpty());
        assertNotNull(response.getData().getLinks());
    }

}
