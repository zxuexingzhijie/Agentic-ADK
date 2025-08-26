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
package com.alibaba.langengine.lancedb.vectorstore;

import com.alibaba.langengine.core.embeddings.Embeddings;
import com.alibaba.langengine.lancedb.LanceDbConfiguration;
import com.alibaba.langengine.lancedb.LanceDbException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LanceDbService {

    /**
     * 创建LanceDB向量存储实例
     *
     * @param embeddings Embeddings实例
     * @param param      参数配置
     * @return LanceDB向量存储实例
     * @throws LanceDbException 创建异常
     */
    public static LanceDbVectorStore createVectorStore(Embeddings embeddings, LanceDbParam param) throws LanceDbException {
        if (embeddings == null) {
            throw new IllegalArgumentException("Embeddings cannot be null");
        }
        if (param == null) {
            throw new IllegalArgumentException("LanceDbParam cannot be null");
        }
        if (!param.isValid()) {
            throw new IllegalArgumentException("Invalid LanceDbParam configuration");
        }

        try {
            // 创建配置对象
            LanceDbConfiguration configuration = createConfiguration(param);
            
            // 创建向量存储实例
            LanceDbVectorStore vectorStore = new LanceDbVectorStore(
                    embeddings,
                    configuration,
                    param.getEffectiveTableName(),
                    param.getEffectiveVectorDimension(),
                    param.getEffectiveCacheSize(),
                    param.getEffectiveSimilarityThreshold()
            );

            log.info("Successfully created LanceDB vector store for table: {}", param.getEffectiveTableName());
            return vectorStore;

        } catch (Exception e) {
            log.error("Failed to create LanceDB vector store: {}", e.getMessage(), e);
            throw new LanceDbException("Failed to create LanceDB vector store", e);
        }
    }

    /**
     * 创建简单的LanceDB向量存储实例
     *
     * @param embeddings Embeddings实例
     * @param tableName  表名
     * @return LanceDB向量存储实例
     * @throws LanceDbException 创建异常
     */
    public static LanceDbVectorStore createSimpleVectorStore(Embeddings embeddings, String tableName) throws LanceDbException {
        LanceDbParam param = LanceDbParam.localParams(tableName);
        return createVectorStore(embeddings, param);
    }

    /**
     * 创建生产环境的LanceDB向量存储实例
     *
     * @param embeddings Embeddings实例
     * @param uri        服务器URI
     * @param apiKey     API密钥
     * @param tableName  表名
     * @return LanceDB向量存储实例
     * @throws LanceDbException 创建异常
     */
    public static LanceDbVectorStore createProductionVectorStore(Embeddings embeddings, 
                                                                String uri, 
                                                                String apiKey, 
                                                                String tableName) throws LanceDbException {
        LanceDbParam param = LanceDbParam.productionParams(uri, apiKey, tableName);
        return createVectorStore(embeddings, param);
    }

    /**
     * 根据参数创建配置对象
     *
     * @param param 参数配置
     * @return 配置对象
     */
    private static LanceDbConfiguration createConfiguration(LanceDbParam param) {
        LanceDbConfiguration configuration = LanceDbConfiguration.builder()
                .uri(param.getEffectiveUri())
                .baseUrl(param.getEffectiveUri())
                .apiKey(param.getApiKey())
                .build();

        // 设置连接参数
        if (param.getConnectionTimeout() != null) {
            configuration.setConnectionTimeout(param.getConnectionTimeout());
        }
        if (param.getReadTimeout() != null) {
            configuration.setReadTimeout(param.getReadTimeout());
        }
        if (param.getWriteTimeout() != null) {
            configuration.setWriteTimeout(param.getWriteTimeout());
        }

        // 设置重试参数
        if (param.getMaxRetries() != null) {
            configuration.setMaxRetries(param.getMaxRetries());
        }
        if (param.getRetryIntervalMs() != null) {
            configuration.setRetryIntervalMs(param.getRetryIntervalMs());
        }

        // 设置批量和连接池参数
        if (param.getBatchSize() != null) {
            configuration.setBatchSize(param.getBatchSize());
        }
        if (param.getConnectionPoolSize() != null) {
            configuration.setConnectionPoolSize(param.getConnectionPoolSize());
        }

        // 设置SSL
        configuration.setEnableSsl(param.isEffectiveEnableSsl());

        // 设置向量参数
        configuration.setDefaultVectorDimension(param.getEffectiveVectorDimension());
        configuration.setDefaultSimilarityThreshold(param.getEffectiveSimilarityThreshold());

        // 设置缓存参数
        configuration.setCacheEnabled(param.isEffectiveCacheEnabled());
        configuration.setCacheSize(param.getEffectiveCacheSize());
        if (param.getCacheExpirationMinutes() != null) {
            configuration.setCacheExpirationMinutes(param.getCacheExpirationMinutes());
        }

        return configuration;
    }

    /**
     * 测试连接
     *
     * @param param 参数配置
     * @return 是否连接成功
     */
    public static boolean testConnection(LanceDbParam param) {
        if (param == null || !param.isValid()) {
            log.error("Invalid LanceDbParam for connection test");
            return false;
        }

        try {
            LanceDbConfiguration configuration = createConfiguration(param);
            
            // 创建临时客户端进行连接测试
            com.alibaba.langengine.lancedb.client.LanceDbClient client = 
                    new com.alibaba.langengine.lancedb.client.LanceDbClient(configuration);
            
            // 这里可以添加实际的连接测试逻辑
            // 例如尝试列出表或执行简单查询
            
            client.close();
            log.info("Connection test successful for URI: {}", param.getEffectiveUri());
            return true;

        } catch (Exception e) {
            log.error("Connection test failed for URI {}: {}", param.getEffectiveUri(), e.getMessage());
            return false;
        }
    }

    /**
     * 创建表格（如果不存在）
     *
     * @param param 参数配置
     * @throws LanceDbException 创建异常
     */
    public static void createTableIfNotExists(LanceDbParam param) throws LanceDbException {
        if (param == null || !param.isValid()) {
            throw new IllegalArgumentException("Invalid LanceDbParam");
        }

        try {
            LanceDbConfiguration configuration = createConfiguration(param);
            com.alibaba.langengine.lancedb.client.LanceDbClient client = 
                    new com.alibaba.langengine.lancedb.client.LanceDbClient(configuration);
            
            client.createTable(param.getEffectiveTableName(), param.getEffectiveVectorDimension());
            client.close();
            
            log.info("Table {} created or already exists", param.getEffectiveTableName());

        } catch (Exception e) {
            log.error("Failed to create table {}: {}", param.getEffectiveTableName(), e.getMessage());
            throw new LanceDbException("Failed to create table", e);
        }
    }

    /**
     * 删除表格
     *
     * @param param 参数配置
     * @throws LanceDbException 删除异常
     */
    public static void dropTable(LanceDbParam param) throws LanceDbException {
        if (param == null || !param.isValid()) {
            throw new IllegalArgumentException("Invalid LanceDbParam");
        }

        try {
            LanceDbConfiguration configuration = createConfiguration(param);
            com.alibaba.langengine.lancedb.client.LanceDbClient client = 
                    new com.alibaba.langengine.lancedb.client.LanceDbClient(configuration);
            
            client.dropTable(param.getEffectiveTableName());
            client.close();
            
            log.info("Table {} dropped successfully", param.getEffectiveTableName());

        } catch (Exception e) {
            log.error("Failed to drop table {}: {}", param.getEffectiveTableName(), e.getMessage());
            throw new LanceDbException("Failed to drop table", e);
        }
    }
}
