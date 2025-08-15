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

    public static String ALIYUN_AI_SEARCH_API_HOST = WorkPropertiesUtils.get("aliyun_ai_search_api_host");

    public static String ALIYUN_AI_SEARCH_API_WORKSPACE = WorkPropertiesUtils.get("aliyun_ai_search_api_workspace");

    public static String ALIYUN_AI_SEARCH_API_SERVICE_ID = WorkPropertiesUtils.get("aliyun_ai_search_api_service_id");

    public static String ALIYUN_AI_SEARCH_API_TIME_OUT = WorkPropertiesUtils.get("aliyun_ai_search_api_time_out",30);

    public static String ALIYUN_AI_SEARCH_API_READ_TIME_OUT = WorkPropertiesUtils.get("aliyun_ai_search_api_read_time_out",60);


}