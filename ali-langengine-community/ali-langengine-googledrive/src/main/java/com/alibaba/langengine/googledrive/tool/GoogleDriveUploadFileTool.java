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

public class GoogleDriveUploadFileTool extends BaseTool {

    public GoogleDriveUploadFileTool() {
        setName("GoogleDrive.uploadFile");
        setHumanName("GoogleDrive 上传文件");
        setDescription("上传文件到Google Drive（multipart/related, base64内容）");
        setParameters("""
        {
          "type":"object",
          "properties":{
            "name":{"type":"string","description":"文件名"},
            "mimeType":{"type":"string","description":"MIME类型"},
            "parentId":{"type":"string","description":"父目录ID，可选"},
            "contentBase64":{"type":"string","description":"文件内容base64"}
          },
          "required":["name","mimeType","contentBase64"],
          "additionalProperties":false
        }
        """);
    }

    @Override
    public ToolExecuteResult run(String toolInput, ExecutionContext executionContext) {
        try {
            JSONObject args = JSON.parseObject(toolInput);
            String token = System.getenv("GOOGLE_DRIVE_TOKEN");
            if (token == null || token.isEmpty()) {
                return wrapError("missing_token", "缺少环境变量 GOOGLE_DRIVE_TOKEN");
            }
            String name = args.getString("name");
            String mimeType = args.getString("mimeType");
            String parentId = args.getString("parentId");
            byte[] content = java.util.Base64.getDecoder().decode(args.getString("contentBase64"));

            String boundary = "batch_" + System.currentTimeMillis();
            byte[] metadata = (parentId == null
                ? ("{\"name\":\"" + name + "\",\"mimeType\":\"" + mimeType + "\"}")
                : ("{\"name\":\"" + name + "\",\"mimeType\":\"" + mimeType + "\",\"parents\":[\"" + parentId + "\"]}"))
                .getBytes(java.nio.charset.StandardCharsets.UTF_8);

            String part1Header = "--" + boundary + "\r\nContent-Type: application/json; charset=UTF-8\r\n\r\n";
            String part2Header = "\r\n--" + boundary + "\r\nContent-Type: " + mimeType + "\r\n\r\n";
            String end = "\r\n--" + boundary + "--";
            byte[] p1 = part1Header.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            byte[] p2 = part2Header.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            byte[] e = end.getBytes(java.nio.charset.StandardCharsets.UTF_8);

            byte[] body = new byte[p1.length + metadata.length + p2.length + content.length + e.length];
            System.arraycopy(p1, 0, body, 0, p1.length);
            System.arraycopy(metadata, 0, body, p1.length, metadata.length);
            System.arraycopy(p2, 0, body, p1.length + metadata.length, p2.length);
            System.arraycopy(content, 0, body, p1.length + metadata.length + p2.length, content.length);
            System.arraycopy(e, 0, body, p1.length + metadata.length + p2.length + content.length, e.length);

            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart"))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "multipart/related; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(body))
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

