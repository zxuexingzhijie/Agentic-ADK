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
package com.alibaba.langengine.github;


public class GitHubConfiguration {

    /**
     * GitHub API Base URL
     */
    public static final String GITHUB_API_URL = getEnvOrDefault("GITHUB_API_URL", "https://api.github.com");

    /**
     * GitHub API Token
     */
    public static final String GITHUB_API_TOKEN = System.getenv("GITHUB_API_TOKEN");

    /**
     * GitHub Search API URL
     */
    public static final String GITHUB_SEARCH_API_URL = GITHUB_API_URL + "/search";

    /**
     * 获取环境变量值，如果不存在则返回默认值
     *
     * @param key 环境变量键
     * @param defaultValue 默认值
     * @return 环境变量值或默认值
     */
    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return value != null ? value : defaultValue;
    }
}
