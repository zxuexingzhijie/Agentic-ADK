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
package com.alibaba.langengine.arxiv.sdk;


public interface ArXivConstant {
    
    /**
     * The base URL for the ArXiv API
     */
    String DEFAULT_BASE_URL = "http://export.arxiv.org/api/";
    
    /**
     * The endpoint for querying papers
     */
    String QUERY_ENDPOINT = "query";
    
    /**
     * The default timeout in seconds for API requests
     */
    int DEFAULT_TIMEOUT = 30;
    
    /**
     * Maximum results per request allowed by ArXiv API
     */
    int MAX_RESULTS_LIMIT = 100;
    
    /**
     * Sort orders supported by ArXiv API
     */
    interface SortOrder {
        String RELEVANCE = "relevance";
        String LAST_UPDATED_DATE = "lastUpdatedDate";
        String SUBMITTED_DATE = "submittedDate";
    }
    
    /**
     * Sort directions supported by ArXiv API
     */
    interface SortDirection {
        String ASCENDING = "ascending";
        String DESCENDING = "descending";
    }
    
    /**
     * Common ArXiv subject categories
     */
    interface Categories {
        String COMPUTER_SCIENCE = "cs";
        String MATHEMATICS = "math";
        String PHYSICS = "physics";
        String QUANTITATIVE_BIOLOGY = "q-bio";
        String QUANTITATIVE_FINANCE = "q-fin";
        String STATISTICS = "stat";
        String ELECTRICAL_ENGINEERING = "eess";
        String ECONOMICS = "econ";
    }
    
    /**
     * ArXiv namespace for parsing XML responses
     */
    String ARXIV_NAMESPACE = "http://www.w3.org/2005/Atom";
    
    /**
     * OpenSearch namespace for parsing XML responses
     */
    String OPENSEARCH_NAMESPACE = "http://a9.com/-/spec/opensearch/1.1/";
}
