package com.alibaba.langengine.aliyunaisearch;

import com.alibaba.langengine.core.util.WorkPropertiesUtils;

/**
 * Aliyun AI Search Configuration
 * Retrieves configuration from work properties
 */
public class AliyunAiSearchConfiguration {
    /**
     * API Key for Aliyun AI Search
     * Retrieved from work properties with key "aliyun_ai_search_api_key"
     */
    public static String ALIYUN_AI_SEARCH_API_KEY = WorkPropertiesUtils.get("aliyun_ai_search_api_key");
}