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
package com.alibaba.langengine.stackoverflow;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class StackOverflowConfigurationTest {
    
    @Test
    void testDefaultConfiguration() {
        // Test that configuration values have proper defaults
        assertNotNull(StackOverflowConfiguration.STACKOVERFLOW_API_BASE_URL);
        assertEquals("https://api.stackexchange.com/2.3", StackOverflowConfiguration.STACKOVERFLOW_API_BASE_URL);
        
        assertNotNull(StackOverflowConfiguration.STACKOVERFLOW_SITE);
        assertEquals("stackoverflow", StackOverflowConfiguration.STACKOVERFLOW_SITE);
        
        assertNotNull(StackOverflowConfiguration.STACKOVERFLOW_API_TIMEOUT);
        assertEquals("30", StackOverflowConfiguration.STACKOVERFLOW_API_TIMEOUT);
        
        assertNotNull(StackOverflowConfiguration.STACKOVERFLOW_API_READ_TIMEOUT);
        assertEquals("60", StackOverflowConfiguration.STACKOVERFLOW_API_READ_TIMEOUT);
        
        assertNotNull(StackOverflowConfiguration.STACKOVERFLOW_MAX_RESULTS);
        assertEquals("10", StackOverflowConfiguration.STACKOVERFLOW_MAX_RESULTS);
        
        assertNotNull(StackOverflowConfiguration.STACKOVERFLOW_ENABLE_SCRAPING);
        assertEquals("true", StackOverflowConfiguration.STACKOVERFLOW_ENABLE_SCRAPING);
        
        assertNotNull(StackOverflowConfiguration.STACKOVERFLOW_SORT_ORDER);
        assertEquals("votes", StackOverflowConfiguration.STACKOVERFLOW_SORT_ORDER);
        
        assertNotNull(StackOverflowConfiguration.STACKOVERFLOW_MIN_SCORE);
        assertEquals("0", StackOverflowConfiguration.STACKOVERFLOW_MIN_SCORE);
    }
    
    @Test
    void testConfigurationConstants() {
        // Test that all configuration constants are accessible
        assertNotNull(StackOverflowConfiguration.class.getDeclaredFields());
        assertTrue(StackOverflowConfiguration.class.getDeclaredFields().length >= 8);
        
        // API key can be null (optional)
        // No assertion needed for STACKOVERFLOW_API_KEY as it might be null
    }
    
    @Test
    void testNumericConfigurationValues() {
        // Test that numeric configuration values can be parsed
        assertDoesNotThrow(() -> {
            Integer.parseInt(StackOverflowConfiguration.STACKOVERFLOW_API_TIMEOUT);
        });
        
        assertDoesNotThrow(() -> {
            Integer.parseInt(StackOverflowConfiguration.STACKOVERFLOW_API_READ_TIMEOUT);
        });
        
        assertDoesNotThrow(() -> {
            Integer.parseInt(StackOverflowConfiguration.STACKOVERFLOW_MAX_RESULTS);
        });
        
        assertDoesNotThrow(() -> {
            Integer.parseInt(StackOverflowConfiguration.STACKOVERFLOW_MIN_SCORE);
        });
        
        // Test that timeouts are reasonable values
        int timeout = Integer.parseInt(StackOverflowConfiguration.STACKOVERFLOW_API_TIMEOUT);
        assertTrue(timeout > 0 && timeout <= 300, "Timeout should be between 1 and 300 seconds");
        
        int readTimeout = Integer.parseInt(StackOverflowConfiguration.STACKOVERFLOW_API_READ_TIMEOUT);
        assertTrue(readTimeout > 0 && readTimeout <= 600, "Read timeout should be between 1 and 600 seconds");
        
        int maxResults = Integer.parseInt(StackOverflowConfiguration.STACKOVERFLOW_MAX_RESULTS);
        assertTrue(maxResults > 0 && maxResults <= 100, "Max results should be between 1 and 100");
        
        int minScore = Integer.parseInt(StackOverflowConfiguration.STACKOVERFLOW_MIN_SCORE);
        assertTrue(minScore >= 0, "Min score should be non-negative");
    }
    
    @Test
    void testBooleanConfigurationValues() {
        // Test that boolean configuration values can be parsed
        assertDoesNotThrow(() -> {
            Boolean.parseBoolean(StackOverflowConfiguration.STACKOVERFLOW_ENABLE_SCRAPING);
        });
        
        // Test that scraping is enabled by default
        assertTrue(Boolean.parseBoolean(StackOverflowConfiguration.STACKOVERFLOW_ENABLE_SCRAPING));
    }
    
    @Test
    void testValidSortOrderValues() {
        String sortOrder = StackOverflowConfiguration.STACKOVERFLOW_SORT_ORDER;
        assertTrue(
            "votes".equals(sortOrder) || 
            "activity".equals(sortOrder) || 
            "creation".equals(sortOrder) || 
            "relevance".equals(sortOrder),
            "Sort order should be one of: votes, activity, creation, relevance"
        );
    }
    
    @Test
    void testValidSiteValue() {
        String site = StackOverflowConfiguration.STACKOVERFLOW_SITE;
        assertNotNull(site);
        assertFalse(site.trim().isEmpty(), "Site should not be empty");
        
        // Common Stack Exchange sites
        assertTrue(
            "stackoverflow".equals(site) ||
            "superuser".equals(site) ||
            "serverfault".equals(site) ||
            "askubuntu".equals(site) ||
            "mathoverflow.net".equals(site),
            "Site should be a valid Stack Exchange site"
        );
    }
    
    @Test
    void testApiBaseUrlFormat() {
        String baseUrl = StackOverflowConfiguration.STACKOVERFLOW_API_BASE_URL;
        assertNotNull(baseUrl);
        assertTrue(baseUrl.startsWith("https://"), "API base URL should use HTTPS");
        assertTrue(baseUrl.contains("stackexchange.com") || baseUrl.contains("stackoverflow.com"), 
                  "API base URL should be a Stack Exchange API endpoint");
        assertFalse(baseUrl.endsWith("/"), "API base URL should not end with slash");
    }
}
