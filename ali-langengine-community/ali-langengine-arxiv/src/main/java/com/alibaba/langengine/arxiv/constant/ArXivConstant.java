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
package com.alibaba.langengine.arxiv.constant;


public class ArXivConstant {
    
    /**
     * ArXiv API base URL
     */
    public static final String ARXIV_API_BASE_URL = "http://export.arxiv.org/api/query";
    
    /**
     * ArXiv abstract base URL
     */
    public static final String ARXIV_ABSTRACT_BASE_URL = "https://arxiv.org/abs/";
    
    /**
     * ArXiv PDF base URL
     */
    public static final String ARXIV_PDF_BASE_URL = "https://arxiv.org/pdf/";
    
    /**
     * Default max results per query
     */
    public static final int DEFAULT_MAX_RESULTS = 10;
    
    /**
     * Default start index
     */
    public static final int DEFAULT_START = 0;
    
    /**
     * Default sort by
     */
    public static final String DEFAULT_SORT_BY = "relevance";
    
    /**
     * Default sort order
     */
    public static final String DEFAULT_SORT_ORDER = "descending";
}
