package com.alibaba.langengine.googledrive.tool;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.langengine.core.callback.ExecutionContext;
import com.alibaba.langengine.core.tool.BaseTool;
import com.alibaba.langengine.core.tool.ToolExecuteResult;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GoogleDriveDownloadFileTool extends BaseTool {

    public GoogleDriveDownloadFileTool() {
        setName("GoogleDrive.downloadFile");
        setHumanName("GoogleDrive 下载文件");
        setDescription("根据fileId下载文件，返回base64内容。");
        setParameters("""
        {
          "type":"object",
          "properties":{
            "fileId":{"type":"string","description":"文件ID"}
          },
          "required":["fileId"],
          "additionalProperties":false
        }
        """);
    }

    @Override
    public ToolExecuteResult run(String toolInput, ExecutionContext executionContext) {
        try {
            JSONObject args = JSON.parseObject(toolInput);
            String fileId = args.getString("fileId");
            String token = System.getenv("GOOGLE_DRIVE_TOKEN");
            if (token == null || token.isEmpty()) {
                return wrapError("missing_token", "缺少环境变量 GOOGLE_DRIVE_TOKEN");
            }
            String url = "https://www.googleapis.com/drive/v3/files/" + fileId + "?alt=media";
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .GET().build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<byte[]> resp = client.send(req, HttpResponse.BodyHandlers.ofByteArray());
            if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                String b64 = java.util.Base64.getEncoder().encodeToString(resp.body());
                JSONObject ok = new JSONObject();
                ok.put("ok", true);
                ok.put("data", new JSONObject() {{ put("fileId", fileId); put("contentBase64", b64); }});
                return new ToolExecuteResult(ok.toJSONString());
            }
            return wrapHttpError(resp.statusCode(), new String(resp.body()));
        } catch (Exception e) {
            return wrapError("exception", e.getMessage());
        }
    }

    private ToolExecuteResult wrapError(String code, String message) {
        JSONObject err = new JSONObject();
        err.put("ok", false);
        err.put("error", new JSONObject() {{ put("code", code); put("message", message); }});
        return new ToolExecuteResult(err.toJSONString());
    }

    private ToolExecuteResult wrapHttpError(int status, String body) {
        JSONObject err = new JSONObject();
        err.put("ok", false);
        JSONObject e = new JSONObject();
        e.put("code", "http_" + status);
        e.put("message", body);
        err.put("error", e);
        return new ToolExecuteResult(err.toJSONString());
    }
}

