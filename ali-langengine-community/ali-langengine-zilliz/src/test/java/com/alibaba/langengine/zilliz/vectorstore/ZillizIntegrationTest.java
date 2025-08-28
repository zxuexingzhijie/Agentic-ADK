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
package com.alibaba.langengine.zilliz.vectorstore;

import com.alibaba.langengine.core.embeddings.FakeEmbeddings;
import com.alibaba.langengine.core.indexes.Document;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.util.List;

/**
 * Zilliz integration test - requires actual Zilliz Cloud credentials
 * 
 * To run this test, set the following environment variables:
 * - ZILLIZ_CLUSTER_ENDPOINT: Your Zilliz Cloud cluster endpoint
 * - ZILLIZ_API_KEY: Your Zilliz Cloud API key
 * - ZILLIZ_DATABASE_NAME: Your database name (optional, defaults to "default")
 *
 * @author xiaoxuan.lp
 */
public class ZillizIntegrationTest {

    @Test
    @EnabledIfEnvironmentVariable(named = "ZILLIZ_CLUSTER_ENDPOINT", matches = ".*")
    public void testZillizWorkflow() {
        // Create Zilliz instance
        Zilliz zilliz = new Zilliz("integration_test_collection");
        zilliz.setEmbedding(new FakeEmbeddings());
        
        try {
            // Initialize collection
            zilliz.init();
            
            // Add documents
            List<Document> documents = Lists.newArrayList(
                new Document("Artificial intelligence is transforming the world", null),
                new Document("Machine learning algorithms can learn from data", null),
                new Document("Deep learning uses neural networks with multiple layers", null),
                new Document("Natural language processing helps computers understand text", null),
                new Document("Computer vision enables machines to interpret visual information", null)
            );
            
            zilliz.addDocuments(documents);
            
            // Perform similarity search
            List<Document> results = zilliz.similaritySearch("AI and machine learning", 3);
            
            System.out.println("Search results:");
            for (Document doc : results) {
                System.out.println("- " + doc.getPageContent() + " (score: " + doc.getScore() + ")");
            }
            
        } finally {
            // Clean up
            zilliz.close();
        }
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "ZILLIZ_CLUSTER_ENDPOINT", matches = ".*")
    public void testZillizWithCustomParams() {
        // Create custom parameters
        ZillizParam param = new ZillizParam();
        param.getInitParam().setFieldEmbeddingsDimension(768);
        param.getInitParam().setFieldPageContentMaxLength(4096);
        param.getInitParam().setShardsNum(1);
        
        // Create Zilliz instance with custom parameters
        Zilliz zilliz = new Zilliz("custom_param_collection", "test_partition", param);
        zilliz.setEmbedding(new FakeEmbeddings());
        
        try {
            zilliz.init();
            
            List<Document> documents = Lists.newArrayList(
                new Document("Custom configuration test document", null)
            );
            
            zilliz.addDocuments(documents);
            List<Document> results = zilliz.similaritySearch("test", 1);
            
            System.out.println("Custom params test results: " + results.size());
            
        } finally {
            zilliz.close();
        }
    }
}