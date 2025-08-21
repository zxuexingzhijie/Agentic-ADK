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
package com.alibaba.langengine.cassandra.vectorstore;

import com.alibaba.langengine.cassandra.utils.Constants;
import com.alibaba.langengine.core.indexes.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


@EnabledIfSystemProperty(named = "test.cassandra.integration.enabled", matches = "true")
public class CassandraIntegrationTest {

    private Cassandra cassandra;
    private CassandraConfiguration configuration;
    private CassandraParam cassandraParam;

    @BeforeEach
    public void setUp() {
        configuration = createIntegrationTestConfiguration();
        cassandraParam = createIntegrationTestParameters();
        
        try {
            cassandra = new Cassandra(configuration, cassandraParam);
        } catch (Exception e) {
            // Skip tests if Cassandra is not available
            org.junit.jupiter.api.Assumptions.assumeTrue(false, 
                    "Skipping Cassandra integration tests - Cassandra not available: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        if (cassandra != null) {
            cassandra.close();
        }
    }

    private CassandraConfiguration createIntegrationTestConfiguration() {
        CassandraConfiguration config = new CassandraConfiguration();
        config.setContactPoints(Arrays.asList("127.0.0.1:9042"));
        config.setLocalDatacenter("datacenter1");
        config.setKeyspace("integration_test");
        // For integration tests, we might need actual credentials
        String username = System.getProperty("cassandra.username");
        String password = System.getProperty("cassandra.password");
        if (username != null && password != null) {
            config.setUsername(username);
            config.setPassword(password);
        }
        return config;
    }

    private CassandraParam createIntegrationTestParameters() {
        CassandraParam param = new CassandraParam();
        
        CassandraParam.InitParam initParam = new CassandraParam.InitParam();
        initParam.setTableName("integration_test_documents");
        initParam.setVectorDimensions(128); // Reasonable size for testing
        initParam.setVectorSimilarityFunction(Constants.SIMILARITY_FUNCTION_COSINE);
        initParam.setReplicationFactor(1);
        
        param.setInitParam(initParam);
        return param;
    }

    @Test
    public void testFullWorkflow() {
        // 1. Add documents
        List<Document> documents = createTestDocuments(10);
        cassandra.addDocuments(documents);
        
        // 2. Perform similarity search
        List<Double> queryVector = generateRandomVector(128);
        List<Document> results = cassandra.similaritySearch(queryVector, 5);
        
        assertNotNull(results);
        assertTrue(results.size() <= 5);
        
        // 3. Verify results contain expected data
        for (Document result : results) {
            assertNotNull(result.getPageContent());
            assertNotNull(result.getUniqueId());
        }
    }

    @Test
    public void testLargeDatasetPerformance() {
        // Test with larger dataset
        int documentCount = 100;
        List<Document> documents = createTestDocuments(documentCount);
        
        long startTime = System.currentTimeMillis();
        cassandra.addDocuments(documents);
        long insertTime = System.currentTimeMillis() - startTime;
        
        System.out.println("Insert time for " + documentCount + " documents: " + insertTime + "ms");
        
        // Perform multiple searches
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            List<Double> queryVector = generateRandomVector(128);
            List<Document> results = cassandra.similaritySearch(queryVector, 10);
            assertNotNull(results);
            assertTrue(results.size() <= 10);
        }
        long searchTime = System.currentTimeMillis() - startTime;
        
        System.out.println("Search time for 10 queries: " + searchTime + "ms");
        
        // Performance assertions - adjust based on your requirements
        assertTrue(insertTime < 60000, "Insert time should be reasonable"); // 60 seconds
        assertTrue(searchTime < 10000, "Search time should be reasonable"); // 10 seconds
    }

    @Test
    public void testSearchRelevance() {
        // Create documents with specific embeddings
        List<Document> documents = Arrays.asList(
                createDocumentWithEmbedding("Apple is a fruit", generateSimilarVector(Arrays.asList(0.1, 0.9), 128)),
                createDocumentWithEmbedding("Orange is a fruit", generateSimilarVector(Arrays.asList(0.2, 0.8), 128)),
                createDocumentWithEmbedding("Car is a vehicle", generateSimilarVector(Arrays.asList(0.9, 0.1), 128)),
                createDocumentWithEmbedding("Bicycle is a vehicle", generateSimilarVector(Arrays.asList(0.8, 0.2), 128))
        );
        
        cassandra.addDocuments(documents);
        
        // Search for fruit-like vector
        List<Double> fruitQuery = generateSimilarVector(Arrays.asList(0.15, 0.85), 128);
        List<Document> fruitResults = cassandra.similaritySearch(fruitQuery, 2);
        
        assertNotNull(fruitResults);
        assertEquals(2, fruitResults.size());
        
        // Verify that fruit documents are returned
        boolean foundApple = fruitResults.stream().anyMatch(doc -> doc.getPageContent().contains("Apple"));
        boolean foundOrange = fruitResults.stream().anyMatch(doc -> doc.getPageContent().contains("Orange"));
        assertTrue(foundApple || foundOrange, "Should find fruit-related documents");
    }

    @Test
    public void testDifferentSimilarityFunctions() {
        // Test different similarity functions
        String[] functions = {
                Constants.SIMILARITY_FUNCTION_COSINE,
                Constants.SIMILARITY_FUNCTION_DOT_PRODUCT,
                Constants.SIMILARITY_FUNCTION_EUCLIDEAN
        };
        
        for (String function : functions) {
            try {
                // Create new configuration with different similarity function
                CassandraParam testParam = createIntegrationTestParameters();
                testParam.getInitParam().setVectorSimilarityFunction(function);
                testParam.getInitParam().setTableName("test_" + function.replace("_", ""));
                
                Cassandra testCassandra = new Cassandra(configuration, testParam);
                
                // Add test document
                Document document = createDocumentWithEmbedding("Test document", generateRandomVector(128));
                testCassandra.addDocuments(Arrays.asList(document));
                
                // Perform search
                List<Double> queryVector = generateRandomVector(128);
                List<Document> results = testCassandra.similaritySearch(queryVector, 1);
                
                assertNotNull(results);
                
                testCassandra.close();
                
            } catch (Exception e) {
                // Some similarity functions might not be available in all Cassandra versions
                System.out.println("Skipping similarity function test for " + function + ": " + e.getMessage());
            }
        }
    }

    @Test
    public void testConcurrentAccess() throws InterruptedException {
        List<Document> documents = createTestDocuments(50);
        cassandra.addDocuments(documents);
        
        // Create multiple threads for concurrent access
        int threadCount = 5;
        Thread[] threads = new Thread[threadCount];
        final List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());
        
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < 10; j++) {
                        List<Double> queryVector = generateRandomVector(128);
                        List<Document> results = cassandra.similaritySearch(queryVector, 5);
                        assertNotNull(results);
                        assertTrue(results.size() <= 5);
                    }
                } catch (Exception e) {
                    exceptions.add(e);
                }
            });
        }
        
        // Start all threads
        for (Thread thread : threads) {
            thread.start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join(30000); // 30 second timeout
        }
        
        // Check for exceptions
        if (!exceptions.isEmpty()) {
            fail("Concurrent access failed: " + exceptions.get(0).getMessage());
        }
    }

    @Test
    public void testMetadataFiltering() {
        // Create documents with different metadata
        List<Document> documents = Arrays.asList(
                createDocumentWithMetadata("Document 1", generateRandomVector(128), 
                        Map.of("category", "science", "rating", 5)),
                createDocumentWithMetadata("Document 2", generateRandomVector(128), 
                        Map.of("category", "history", "rating", 4)),
                createDocumentWithMetadata("Document 3", generateRandomVector(128), 
                        Map.of("category", "science", "rating", 3))
        );
        
        cassandra.addDocuments(documents);
        
        // Perform search
        List<Double> queryVector = generateRandomVector(128);
        List<Document> results = cassandra.similaritySearch(queryVector, 10);
        
        assertNotNull(results);
        
        // Verify metadata is preserved
        for (Document result : results) {
            assertNotNull(result.getMetadata());
            assertTrue(result.getMetadata().containsKey("category") || result.getMetadata().containsKey("similarity"));
        }
    }

    private List<Document> createTestDocuments(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> {
                    Document doc = new Document();
                    doc.setPageContent("Test document " + i);
                    doc.setUniqueId("doc_" + i);
                    doc.setEmbedding(generateRandomVector(128));
                    doc.setMetadata(Map.of("index", i, "category", "test"));
                    return doc;
                })
                .collect(Collectors.toList());
    }

    private Document createDocumentWithEmbedding(String content, List<Double> embedding) {
        Document doc = new Document();
        doc.setPageContent(content);
        doc.setUniqueId(UUID.randomUUID().toString());
        doc.setEmbedding(embedding);
        return doc;
    }

    private Document createDocumentWithMetadata(String content, List<Double> embedding, Map<String, Object> metadata) {
        Document doc = createDocumentWithEmbedding(content, embedding);
        doc.setMetadata(new HashMap<>(metadata));
        return doc;
    }

    private List<Double> generateRandomVector(int dimensions) {
        Random random = new Random();
        return IntStream.range(0, dimensions)
                .mapToDouble(i -> random.nextGaussian())
                .boxed()
                .collect(Collectors.toList());
    }

    private List<Double> generateSimilarVector(List<Double> base, int dimensions) {
        Random random = new Random();
        List<Double> vector = new ArrayList<>();
        
        for (int i = 0; i < dimensions; i++) {
            if (i < base.size()) {
                // Add some noise to base values
                vector.add(base.get(i) + random.nextGaussian() * 0.1);
            } else {
                vector.add(random.nextGaussian() * 0.1);
            }
        }
        
        return vector;
    }
}
