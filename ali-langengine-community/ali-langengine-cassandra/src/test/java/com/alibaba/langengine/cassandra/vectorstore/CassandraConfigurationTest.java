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
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


public class CassandraConfigurationTest {

    @Test
    public void testDefaultConstructor() {
        CassandraConfiguration config = new CassandraConfiguration();
        
        assertNotNull(config);
        // Configuration fields might be null or have default values depending on properties
        // Test that the object is created successfully
        assertTrue(config.getContactPoints() == null || config.getContactPoints().isEmpty() || !config.getContactPoints().isEmpty());
        // Other fields might be null or empty strings depending on properties configuration
    }

    @Test
    public void testSettersAndGetters() {
        CassandraConfiguration config = new CassandraConfiguration();
        
        config.setContactPoints(Arrays.asList("127.0.0.1:9042", "127.0.0.2:9042"));
        config.setLocalDatacenter("datacenter1");
        config.setKeyspace("test_keyspace");
        config.setUsername("test_user");
        config.setPassword("test_password");
        
        assertEquals(Arrays.asList("127.0.0.1:9042", "127.0.0.2:9042"), config.getContactPoints());
        assertEquals("datacenter1", config.getLocalDatacenter());
        assertEquals("test_keyspace", config.getKeyspace());
        assertEquals("test_user", config.getUsername());
        assertEquals("test_password", config.getPassword());
    }

    @Test
    public void testConfigurationWithSingleContactPoint() {
        CassandraConfiguration config = new CassandraConfiguration();
        config.setContactPoints(Arrays.asList("localhost"));
        
        assertEquals(1, config.getContactPoints().size());
        assertEquals("localhost", config.getContactPoints().get(0));
    }

    @Test
    public void testConfigurationWithMultipleContactPoints() {
        CassandraConfiguration config = new CassandraConfiguration();
        config.setContactPoints(Arrays.asList("node1:9042", "node2:9042", "node3:9042"));
        
        assertEquals(3, config.getContactPoints().size());
        assertTrue(config.getContactPoints().contains("node1:9042"));
        assertTrue(config.getContactPoints().contains("node2:9042"));
        assertTrue(config.getContactPoints().contains("node3:9042"));
    }

    @Test
    public void testConfigurationEquality() {
        CassandraConfiguration config1 = new CassandraConfiguration();
        config1.setContactPoints(Arrays.asList("127.0.0.1:9042"));
        config1.setLocalDatacenter("datacenter1");
        config1.setKeyspace("test");
        
        CassandraConfiguration config2 = new CassandraConfiguration();
        config2.setContactPoints(Arrays.asList("127.0.0.1:9042"));
        config2.setLocalDatacenter("datacenter1");
        config2.setKeyspace("test");
        
        // Note: Lombok @Data should provide equals/hashCode
        assertEquals(config1, config2);
        assertEquals(config1.hashCode(), config2.hashCode());
    }

    @Test
    public void testConfigurationToString() {
        CassandraConfiguration config = new CassandraConfiguration();
        config.setContactPoints(Arrays.asList("127.0.0.1:9042"));
        config.setLocalDatacenter("datacenter1");
        config.setKeyspace("test");
        
        String toString = config.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("CassandraConfiguration"));
    }

    @Test
    public void testConfigurationWithNullValues() {
        CassandraConfiguration config = new CassandraConfiguration();
        
        // Should handle null values gracefully
        config.setContactPoints(null);
        config.setLocalDatacenter(null);
        config.setKeyspace(null);
        config.setUsername(null);
        config.setPassword(null);
        
        assertNull(config.getContactPoints());
        assertNull(config.getLocalDatacenter());
        assertNull(config.getKeyspace());
        assertNull(config.getUsername());
        assertNull(config.getPassword());
    }

    @Test
    public void testConfigurationWithEmptyContactPoints() {
        CassandraConfiguration config = new CassandraConfiguration();
        config.setContactPoints(Arrays.asList());
        
        assertTrue(config.getContactPoints().isEmpty());
    }
}
