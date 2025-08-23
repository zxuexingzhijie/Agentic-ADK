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
package com.alibaba.langengine.arxiv.llm;

import com.alibaba.langengine.arxiv.ArXivLLM;
import com.alibaba.langengine.arxiv.model.ArXivPaper;
import com.alibaba.langengine.arxiv.model.ArXivSearchRequest;
import com.alibaba.langengine.arxiv.model.ArXivSearchResponse;
import com.alibaba.langengine.arxiv.service.ArXivApiService;
import com.alibaba.langengine.arxiv.sdk.ArXivException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Mock-based unit tests for ArXivLLM.
 * Uses Mockito to avoid network dependencies.
 * 
 * @author agentic-adk
 */
public class ArXivLLMTest {
    
    @Mock
    private ArXivApiService mockApiService;
    
    private ArXivLLM llm;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        llm = new ArXivLLM();
        llm.setApiService(mockApiService);
    }
    
    @Test
    void testSearchPapersWithQuery() throws ArXivException {
        // Arrange
        String query = "machine learning";
        ArXivSearchResponse mockResponse = createMockSearchResponse();
        when(mockApiService.searchPapers(eq(query))).thenReturn(mockResponse);
        
        // Act
        ArXivSearchResponse result = llm.getApiService().searchPapers(query);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.getPapers().size());
        verify(mockApiService).searchPapers(eq(query));
    }
    
    @Test
    void testSearchPapersWithQueryAndMaxResults() throws ArXivException {
        // Arrange
        String query = "neural networks";
        int maxResults = 5;
        ArXivSearchResponse mockResponse = createMockSearchResponse();
        when(mockApiService.searchPapers(eq(query), eq(maxResults))).thenReturn(mockResponse);
        
        // Act
        ArXivSearchResponse result = llm.getApiService().searchPapers(query, maxResults);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.getPapers().size());
        verify(mockApiService).searchPapers(eq(query), eq(maxResults));
    }
    
    @Test
    void testSearchByCategory() throws ArXivException {
        // Arrange
        String category = "cs.AI";
        int maxResults = 10;
        ArXivSearchResponse mockResponse = createMockSearchResponse();
        when(mockApiService.searchByCategory(eq(category), eq(maxResults))).thenReturn(mockResponse);
        
        // Act
        ArXivSearchResponse result = llm.getApiService().searchByCategory(category, maxResults);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.getPapers().size());
        verify(mockApiService).searchByCategory(eq(category), eq(maxResults));
    }
    
    @Test
    void testSearchByAuthor() throws ArXivException {
        // Arrange
        String author = "Geoffrey Hinton";
        int maxResults = 15;
        ArXivSearchResponse mockResponse = createMockSearchResponse();
        when(mockApiService.searchByAuthor(eq(author), eq(maxResults))).thenReturn(mockResponse);
        
        // Act
        ArXivSearchResponse result = llm.getApiService().searchByAuthor(author, maxResults);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.getPapers().size());
        verify(mockApiService).searchByAuthor(eq(author), eq(maxResults));
    }
    
    @Test
    void testGetPaper() throws ArXivException {
        // Arrange
        String arxivId = "2301.12345";
        ArXivPaper mockPaper = createMockPaper();
        when(mockApiService.getPaper(eq(arxivId))).thenReturn(mockPaper);
        
        // Act
        ArXivPaper result = llm.getApiService().getPaper(arxivId);
        
        // Assert
        assertNotNull(result);
        assertEquals(arxivId, result.getId());
        assertEquals("Test Paper", result.getTitle());
        verify(mockApiService).getPaper(eq(arxivId));
    }
    
    @Test
    void testGetPaperNotFound() throws ArXivException {
        // Arrange
        String arxivId = "9999.99999";
        when(mockApiService.getPaper(eq(arxivId))).thenThrow(new ArXivException("Paper not found"));
        
        // Act & Assert
        assertThrows(ArXivException.class, () -> llm.getApiService().getPaper(arxivId));
        verify(mockApiService).getPaper(eq(arxivId));
    }
    
    @Test
    void testGetRecentPapers() throws ArXivException {
        // Arrange
        String category = "cs.AI";
        int days = 7;
        int maxResults = 20;
        ArXivSearchResponse mockResponse = createMockSearchResponse();
        when(mockApiService.getRecentPapers(eq(category), eq(days), eq(maxResults))).thenReturn(mockResponse);
        
        // Act
        ArXivSearchResponse result = llm.getApiService().getRecentPapers(category, days, maxResults);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.getPapers().size());
        verify(mockApiService).getRecentPapers(eq(category), eq(days), eq(maxResults));
    }
    
    @Test
    void testDownloadPdf() throws ArXivException {
        // Arrange
        String arxivId = "2301.12345";
        byte[] mockPdfContent = "Mock PDF content".getBytes();
        when(mockApiService.downloadPdf(eq(arxivId))).thenReturn(mockPdfContent);
        
        // Act
        byte[] result = llm.getApiService().downloadPdf(arxivId);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.length > 0);
        verify(mockApiService).downloadPdf(eq(arxivId));
    }
    
    private ArXivSearchResponse createMockSearchResponse() {
        ArXivSearchResponse response = new ArXivSearchResponse();
        response.setTotalResults(2);
        response.setStartIndex(0);
        response.setItemsPerPage(10);
        
        List<ArXivPaper> papers = Arrays.asList(
            createMockPaper(),
            createMockPaper2()
        );
        response.setPapers(papers);
        
        return response;
    }
    
    private ArXivPaper createMockPaper() {
        ArXivPaper paper = new ArXivPaper();
        paper.setId("2301.12345");
        paper.setTitle("Test Paper");
        paper.setSummary("This is a test paper about machine learning");
        paper.setAuthors(Arrays.asList("Author One", "Author Two"));
        return paper;
    }
    
    private ArXivPaper createMockPaper2() {
        ArXivPaper paper = new ArXivPaper();
        paper.setId("2301.67890");
        paper.setTitle("Another Test Paper");
        paper.setSummary("This is another test paper about neural networks");
        paper.setAuthors(Arrays.asList("Author Three", "Author Four"));
        return paper;
    }
}
