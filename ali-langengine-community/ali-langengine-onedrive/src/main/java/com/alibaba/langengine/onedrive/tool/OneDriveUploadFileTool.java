package com.alibaba.langengine.onedrive.tool;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.langengine.core.callback.ExecutionContext;
import com.alibaba.langengine.core.tool.BaseTool;
import com.alibaba.langengine.core.tool.ToolExecuteResult;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OneDriveUploadFileTool extends BaseTool {

    public OneDriveUploadFileTool() {
        setName("OneDrive.uploadFile");
        setHumanName("OneDrive 上传文件");
        setDescription("上传文件到OneDrive根目录或指定路径（base64内容）");
        setParameters("""
        {
          "type":"object",
          "properties":{
            "path":{"type":"string","description":"目标路径，如 folder/sub/file.txt"},
            "contentBase64":{"type":"string","description":"文件内容base64"},
            "conflictBehavior":{"type":"string","enum":["fail","replace","rename"],"default":"rename"}
          },
          "required":["contentBase64"],
          "additionalProperties":false
        }
        """);
    }

    @Override
    public ToolExecuteResult run(String toolInput, ExecutionContext executionContext) {
        try {
            JSONObject args = JSON.parseObject(toolInput);
            String token = System.getenv("ONEDRIVE_TOKEN");
            if (token == null || token.isEmpty()) {
                return wrapError("missing_token", "缺少环境变量 ONEDRIVE_TOKEN");
            }
            String path = args.getString("path");
            if (path == null || path.isEmpty()) path = "uploaded_" + System.currentTimeMillis();
            byte[] content = java.util.Base64.getDecoder().decode(args.getString("contentBase64"));
            String behavior = args.getString("conflictBehavior");
            if (behavior == null) behavior = "rename";

            String url = "https://graph.microsoft.com/v1.0/me/drive/root:/" + encode(path) + ":/content?@microsoft.graph.conflictBehavior=" + behavior;
            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/octet-stream")
                .PUT(HttpRequest.BodyPublishers.ofByteArray(content))
                .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                JSONObject ok = new JSONObject();
                ok.put("ok", true);
                ok.put("data", JSON.parse(resp.body()));
                return new ToolExecuteResult(ok.toJSONString());
            }
            return wrapHttpError(resp.statusCode(), resp.body());
        } catch (Exception e) {
            return wrapError("exception", e.getMessage());
        }
    }

    private String encode(String s) {
        return java.net.URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8);
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

