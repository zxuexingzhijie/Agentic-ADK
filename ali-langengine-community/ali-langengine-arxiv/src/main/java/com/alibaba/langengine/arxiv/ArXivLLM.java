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
package com.alibaba.langengine.arxiv;

import com.alibaba.fastjson.JSON;
import com.alibaba.langengine.core.model.BaseLLM;
import com.alibaba.langengine.core.model.fastchat.completion.chat.ChatCompletionRequest;
import com.alibaba.langengine.core.model.fastchat.completion.chat.ChatMessage;
import com.alibaba.langengine.arxiv.model.ArXivPaper;
import com.alibaba.langengine.arxiv.model.ArXivSearchRequest;
import com.alibaba.langengine.arxiv.model.ArXivSearchResponse;
import com.alibaba.langengine.arxiv.service.ArXivApiService;
import com.alibaba.langengine.arxiv.service.impl.ArXivApiServiceImpl;
import com.alibaba.langengine.arxiv.sdk.ArXivException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.alibaba.langengine.arxiv.ArXivConfiguration.*;


@Data
@EqualsAndHashCode(callSuper = false)
@Slf4j
public class ArXivLLM extends BaseLLM<ChatCompletionRequest> {
    
    private ArXivApiService apiService;
    
    /**
     * Maximum number of results to return
     */
    private Integer maxResults;
    
    /**
     * Sort order for search results (relevance, lastUpdatedDate, submittedDate)
     */
    private String sortOrder;
    
    /**
     * Sort direction (ascending, descending)
     */
    private String sortDirection;
    
    /**
     * Whether to include abstract in results
     */
    private Boolean includeAbstract = true;
    
    /**
     * Whether to include PDF links in results
     */
    private Boolean includePdfLinks = true;
    
    /**
     * Whether to include full text links in results
     */
    private Boolean includeFullTextLinks = true;
    
    /**
     * Subject categories to filter by
     */
    private List<String> categories;
    
    /**
     * Authors to filter by
     */
    private List<String> authors;
    
    /**
     * Whether to enable vector database support for similarity search
     */
    private Boolean vectorDatabaseEnabled = false;
    
    /**
     * Custom date range filter - start date
     */
    private String startDate;
    
    /**
     * Custom date range filter - end date
     */
    private String endDate;
    
    public ArXivLLM() {
        this.apiService = new ArXivApiServiceImpl();
        initializeConfiguration();
    }
    
    public ArXivLLM(ArXivApiService apiService) {
        this.apiService = apiService;
        initializeConfiguration();
    }
    
    /**
     * Initialize configuration parameters
     */
    private void initializeConfiguration() {
        this.maxResults = getDefaultMaxResults();
        this.sortOrder = getDefaultSortOrder();
        this.sortDirection = getDefaultSortDirection();
    }
    
    /**
     * Set API service for testing
     * @param apiService API service
     */
    public void setApiService(ArXivApiService apiService) {
        this.apiService = apiService;
    }
    
    @Override
    public String run(String prompt, List<String> stops, Consumer<String> consumer, Map<String, Object> extraAttributes) {
        try {
            log.info("ArXiv 搜索开始，查询: {}", prompt);
            
            // Parse search parameters from prompt and extra attributes
            ArXivSearchRequest searchRequest = parseSearchQuery(prompt, extraAttributes);
            
            // Execute search
            ArXivSearchResponse response = apiService.searchPapers(searchRequest);
            
            // Format results for output
            String formattedResults = formatSearchResults(response, searchRequest.getQuery());
            
            log.info("ArXiv 搜索完成，返回{}个结果", response.getReturnedCount());
            
            if (consumer != null) {
                consumer.accept(formattedResults);
            }
            
            return formattedResults;
            
        } catch (ArXivException e) {
            log.error("ArXiv 搜索失败: {}", e.getMessage(), e);
            String errorMessage = "ArXiv 搜索失败: " + e.getMessage();
            
            if (consumer != null) {
                consumer.accept(errorMessage);
            }
            
            return errorMessage;
            
        } catch (IllegalArgumentException e) {
            log.error("ArXiv 搜索参数错误: {}", e.getMessage());
            String errorMessage = "搜索参数错误: " + e.getMessage();
            
            if (consumer != null) {
                consumer.accept(errorMessage);
            }
            
            return errorMessage;
            
        } catch (Exception e) {
            log.error("ArXiv 搜索过程中发生未知错误: {}", e.getMessage(), e);
            String errorMessage = "搜索过程中发生未知错误，请稍后重试";
            
            if (consumer != null) {
                consumer.accept(errorMessage);
            }
            
            return errorMessage;
        }
    }
    
    /**
     * Parse search query and extract parameters
     */
    private ArXivSearchRequest parseSearchQuery(String prompt, Map<String, Object> extraAttributes) {
        // Validate input
        if (prompt == null || prompt.trim().isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }
        
        ArXivSearchRequest.Builder builder = ArXivSearchRequest.builder();
        
        // Try to parse JSON input first
        if (prompt.trim().startsWith("{")) {
            try {
                Map<String, Object> queryMap = JSON.parseObject(prompt, Map.class);
                return buildRequestFromMap(queryMap);
            } catch (Exception e) {
                log.debug("Failed to parse JSON input, treating as plain text query: {}", e.getMessage());
            }
        }
        
        // Extract main query
        String mainQuery = extractMainQuery(prompt);
        if (mainQuery == null || mainQuery.trim().isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }
        builder.query(mainQuery);
        
        // Apply configuration defaults
        builder.maxResults(maxResults != null ? maxResults : getDefaultMaxResults());
        builder.sortBy(sortOrder != null ? sortOrder : getDefaultSortOrder());
        builder.sortOrder(sortDirection != null ? sortDirection : getDefaultSortDirection());
        
        // Apply filters from configuration
        if (categories != null && !categories.isEmpty()) {
            builder.categories(categories);
        }
        
        if (authors != null && !authors.isEmpty()) {
            builder.authors(authors);
        }
        
        if (startDate != null || endDate != null) {
            builder.dateRange(startDate, endDate);
        }
        
        // Apply extra attributes if provided
        if (extraAttributes != null) {
            applyExtraAttributes(builder, extraAttributes);
        }
        
        // Set inclusion flags
        builder.includeAbstract(includeAbstract != null ? includeAbstract : true);
        builder.includeFullTextLink(includeFullTextLinks != null ? includeFullTextLinks : true);
        builder.includePdfLink(includePdfLinks != null ? includePdfLinks : true);
        
        return builder.build();
    }
    
    /**
     * Build request from JSON map
     */
    private ArXivSearchRequest buildRequestFromMap(Map<String, Object> queryMap) {
        ArXivSearchRequest.Builder builder = ArXivSearchRequest.builder();
        
        // Required query parameter
        Object queryObj = queryMap.get("query");
        if (queryObj == null) {
            queryObj = queryMap.get("q");
        }
        if (queryObj != null) {
            builder.query(queryObj.toString());
        }
        
        // Optional parameters
        if (queryMap.containsKey("max_results")) {
            builder.maxResults(Integer.parseInt(queryMap.get("max_results").toString()));
        } else if (queryMap.containsKey("maxResults")) {
            builder.maxResults(Integer.parseInt(queryMap.get("maxResults").toString()));
        }
        
        if (queryMap.containsKey("start")) {
            builder.start(Integer.parseInt(queryMap.get("start").toString()));
        }
        
        if (queryMap.containsKey("sort_by")) {
            builder.sortBy(queryMap.get("sort_by").toString());
        } else if (queryMap.containsKey("sortBy")) {
            builder.sortBy(queryMap.get("sortBy").toString());
        }
        
        if (queryMap.containsKey("sort_order")) {
            builder.sortOrder(queryMap.get("sort_order").toString());
        } else if (queryMap.containsKey("sortOrder")) {
            builder.sortOrder(queryMap.get("sortOrder").toString());
        }
        
        // Category filters
        Object categoriesObj = queryMap.get("categories");
        if (categoriesObj != null) {
            if (categoriesObj instanceof List) {
                builder.categories((List<String>) categoriesObj);
            } else {
                builder.categories(Arrays.asList(categoriesObj.toString().split(",")));
            }
        }
        
        // Author filters
        Object authorsObj = queryMap.get("authors");
        if (authorsObj != null) {
            if (authorsObj instanceof List) {
                builder.authors((List<String>) authorsObj);
            } else {
                builder.authors(Arrays.asList(authorsObj.toString().split(",")));
            }
        }
        
        // Date range
        if (queryMap.containsKey("start_date")) {
            builder.dateRange(queryMap.get("start_date").toString(), 
                             queryMap.get("end_date") != null ? queryMap.get("end_date").toString() : null);
        }
        
        return builder.build();
    }
    
    /**
     * Extract main query from natural language prompt
     */
    private String extractMainQuery(String prompt) {
        // Remove common prefixes
        String cleaned = prompt.trim();
        String[] prefixes = {"search for", "find", "look for", "get", "搜索", "查找", "找到"};
        
        for (String prefix : prefixes) {
            if (cleaned.toLowerCase().startsWith(prefix.toLowerCase())) {
                cleaned = cleaned.substring(prefix.length()).trim();
                break;
            }
        }
        
        // Remove common suffixes
        String[] suffixes = {"papers", "articles", "research", "论文", "文章", "研究"};
        for (String suffix : suffixes) {
            if (cleaned.toLowerCase().endsWith(suffix.toLowerCase())) {
                cleaned = cleaned.substring(0, cleaned.length() - suffix.length()).trim();
                break;
            }
        }
        
        return cleaned;
    }
    
    /**
     * Apply extra attributes to the request builder
     */
    private void applyExtraAttributes(ArXivSearchRequest.Builder builder, Map<String, Object> extraAttributes) {
        if (extraAttributes.containsKey("maxResults")) {
            builder.maxResults(Integer.parseInt(extraAttributes.get("maxResults").toString()));
        }
        
        if (extraAttributes.containsKey("category")) {
            String category = extraAttributes.get("category").toString();
            builder.categories(Arrays.asList(category.split(",")));
        }
        
        if (extraAttributes.containsKey("author")) {
            String author = extraAttributes.get("author").toString();
            builder.authors(Arrays.asList(author.split(",")));
        }
        
        if (extraAttributes.containsKey("sortBy")) {
            builder.sortBy(extraAttributes.get("sortBy").toString());
        }
        
        if (extraAttributes.containsKey("sortOrder")) {
            builder.sortOrder(extraAttributes.get("sortOrder").toString());
        }
    }
    
    /**
     * Format search results for display
     */
    private String formatSearchResults(ArXivSearchResponse response, String originalQuery) {
        if (response == null) {
            return "搜索失败: 响应为空";
        }
        
        if (!response.isSuccessful()) {
            return "搜索失败: " + response.getErrorMessage();
        }
        
        if (response.isEmpty()) {
            return "未找到相关论文: " + originalQuery;
        }
        
        StringBuilder result = new StringBuilder();
        
        // Add summary
        result.append("=== ArXiv 论文搜索结果 ===\\n");
        result.append(response.getSummary()).append("\\n\\n");
        
        // Add papers
        List<ArXivPaper> papers = response.getPapers();
        for (int i = 0; i < papers.size(); i++) {
            ArXivPaper paper = papers.get(i);
            result.append(formatPaper(paper, i + 1));
            result.append("\\n");
        }
        
        // Add pagination info if available
        if (response.getHasMoreResults() != null && response.getHasMoreResults()) {
            result.append("\\n更多结果可用，可使用分页参数获取...");
        }
        
        // Add vector database notice if enabled
        if (vectorDatabaseEnabled) {
            result.append("\\n\\n💡 向量数据库支持已启用，可进行语义相似性搜索");
        }
        
        return result.toString();
    }
    
    /**
     * Format a single paper for display
     */
    private String formatPaper(ArXivPaper paper, int index) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(String.format("[%d] %s\\n", index, paper.getTitle()));
        
        if (paper.getAuthors() != null && !paper.getAuthors().isEmpty()) {
            sb.append("作者: ").append(formatAuthors(paper.getAuthors())).append("\\n");
        }
        
        if (paper.getCategories() != null && !paper.getCategories().isEmpty()) {
            sb.append("分类: ").append(String.join(", ", paper.getCategories())).append("\\n");
        }
        
        if (paper.getPublished() != null) {
            sb.append("发布: ").append(paper.getPublished().toLocalDate()).append("\\n");
        }
        
        if (includeAbstract && paper.getSummary() != null) {
            String summary = paper.getShortSummary();
            sb.append("摘要: ").append(summary).append("\\n");
        }
        
        if (paper.getArxivUrl() != null) {
            sb.append("链接: ").append(paper.getArxivUrl()).append("\\n");
        }
        
        if (includePdfLinks && paper.getPdfUrl() != null) {
            sb.append("PDF: ").append(paper.getPdfUrl()).append("\\n");
        }
        
        if (paper.getDoi() != null) {
            sb.append("DOI: ").append(paper.getDoi()).append("\\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Format authors list for display
     */
    private String formatAuthors(List<String> authors) {
        if (authors.size() <= 3) {
            return String.join(", ", authors);
        } else {
            return String.join(", ", authors.subList(0, 3)) + " et al.";
        }
    }
    
    @Override
    public ChatCompletionRequest buildRequest(List<ChatMessage> chatMessages, List<String> stops, 
                                            Consumer<String> consumer, Map<String, Object> extraAttributes) {
        // ArXiv LLM doesn't use ChatCompletionRequest directly
        return null;
    }
    
    @Override
    public String runRequest(ChatCompletionRequest request, List<String> stops, 
                           Consumer<String> consumer, Map<String, Object> extraAttributes) {
        // Not implemented for ArXiv LLM
        return "";
    }
    
    @Override
    public String runRequestStream(ChatCompletionRequest request, List<String> stops, 
                                 Consumer<String> consumer, Map<String, Object> extraAttributes) {
        // Not implemented for ArXiv LLM
        return "";
    }
}
