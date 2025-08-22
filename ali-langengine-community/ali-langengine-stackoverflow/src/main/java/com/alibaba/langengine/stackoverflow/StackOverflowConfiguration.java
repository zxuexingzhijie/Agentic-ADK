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

import com.alibaba.langengine.core.util.WorkPropertiesUtils;


public class StackOverflowConfiguration {
    
    /**
     * Stack Exchange API Key (optional, for higher rate limits)
     * Retrieved from work properties with key "stackoverflow_api_key"
     */
    public static String STACKOVERFLOW_API_KEY = WorkPropertiesUtils.get("stackoverflow_api_key");
    
    /**
     * Stack Exchange API Base URL
     */
    public static String STACKOVERFLOW_API_BASE_URL = WorkPropertiesUtils.get("stackoverflow_api_base_url", "https://api.stackexchange.com/2.3");
    
    /**
     * Default site for Stack Exchange API (stackoverflow, superuser, serverfault, etc.)
     */
    public static String STACKOVERFLOW_SITE = WorkPropertiesUtils.get("stackoverflow_site", "stackoverflow");
    
    /**
     * API timeout in seconds
     */
    public static String STACKOVERFLOW_API_TIMEOUT = WorkPropertiesUtils.get("stackoverflow_api_timeout", "30");
    
    /**
     * Read timeout in seconds
     */
    public static String STACKOVERFLOW_API_READ_TIMEOUT = WorkPropertiesUtils.get("stackoverflow_api_read_timeout", "60");
    
    /**
     * Maximum number of results to return per search
     */
    public static String STACKOVERFLOW_MAX_RESULTS = WorkPropertiesUtils.get("stackoverflow_max_results", "10");
    
    /**
     * Enable/disable web scraping fallback when API fails
     */
    public static String STACKOVERFLOW_ENABLE_SCRAPING = WorkPropertiesUtils.get("stackoverflow_enable_scraping", "true");
    
    /**
     * Sort order for search results (activity, votes, creation, relevance)
     */
    public static String STACKOVERFLOW_SORT_ORDER = WorkPropertiesUtils.get("stackoverflow_sort_order", "votes");
    
    /**
     * Filter for question quality (minimum score)
     */
    public static String STACKOVERFLOW_MIN_SCORE = WorkPropertiesUtils.get("stackoverflow_min_score", "0");
}
