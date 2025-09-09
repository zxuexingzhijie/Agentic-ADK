package com.alibaba.langengine.googledrive.tool;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GoogleDriveToolsTest {

    @Test
    void testListFilesToolMetadata() {
        GoogleDriveListFilesTool tool = new GoogleDriveListFilesTool();
        assertEquals("GoogleDrive.listFiles", tool.getName());
        assertNotNull(tool.getDescription());
        assertNotNull(tool.getParameters());
        assertFalse(tool.getParameters().isEmpty());
    }

    @Test
    void testDownloadFileToolMetadata() {
        GoogleDriveDownloadFileTool tool = new GoogleDriveDownloadFileTool();
        assertEquals("GoogleDrive.downloadFile", tool.getName());
        assertNotNull(tool.getDescription());
        assertNotNull(tool.getParameters());
        assertTrue(tool.getParameters().contains("fileId"));
    }

    @Test
    void testUploadFileToolMetadata() {
        GoogleDriveUploadFileTool tool = new GoogleDriveUploadFileTool();
        assertEquals("GoogleDrive.uploadFile", tool.getName());
        assertNotNull(tool.getDescription());
        assertNotNull(tool.getParameters());
        assertTrue(tool.getParameters().contains("contentBase64"));
        assertTrue(tool.getParameters().contains("name"));
        assertTrue(tool.getParameters().contains("mimeType"));
    }
}

