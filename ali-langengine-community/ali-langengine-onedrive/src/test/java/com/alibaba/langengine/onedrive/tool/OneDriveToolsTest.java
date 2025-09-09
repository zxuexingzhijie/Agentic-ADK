package com.alibaba.langengine.onedrive.tool;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OneDriveToolsTest {

    @Test
    void testListFilesToolMetadata() {
        OneDriveListFilesTool tool = new OneDriveListFilesTool();
        assertEquals("OneDrive.listFiles", tool.getName());
        assertNotNull(tool.getDescription());
        assertNotNull(tool.getParameters());
        assertFalse(tool.getParameters().isEmpty());
    }

    @Test
    void testDownloadFileToolMetadata() {
        OneDriveDownloadFileTool tool = new OneDriveDownloadFileTool();
        assertEquals("OneDrive.downloadFile", tool.getName());
        assertNotNull(tool.getDescription());
        assertNotNull(tool.getParameters());
        assertTrue(tool.getParameters().contains("itemId"));
    }

    @Test
    void testUploadFileToolMetadata() {
        OneDriveUploadFileTool tool = new OneDriveUploadFileTool();
        assertEquals("OneDrive.uploadFile", tool.getName());
        assertNotNull(tool.getDescription());
        assertNotNull(tool.getParameters());
        assertTrue(tool.getParameters().contains("contentBase64"));
    }
}

