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

public class GoogleDriveListFilesTool extends BaseTool {

    public GoogleDriveListFilesTool() {
        setName("GoogleDrive.listFiles");
        setHumanName("GoogleDrive 文件列表");
        setDescription("列出Google Drive文件，支持q查询与分页。");
        setParameters("""
        {
          "type":"object",
          "properties":{
            "q":{"type":"string","description":"Drive查询语法，如 name contains 'report'"},
            "pageSize":{"type":"integer","minimum":1,"maximum":200,"default":50},
            "pageToken":{"type":"string","description":"上一页的nextPageToken"}
          },
          "required":[],
          "additionalProperties":false
        }
        """);
    }

    @Override
    public ToolExecuteResult run(String toolInput, ExecutionContext executionContext) {
        try {
            JSONObject args = (toolInput == null || toolInput.isEmpty()) ? new JSONObject() : JSON.parseObject(toolInput);
            String token = System.getenv("GOOGLE_DRIVE_TOKEN");
            if (token == null || token.isEmpty()) {
                return wrapError("missing_token", "缺少环境变量 GOOGLE_DRIVE_TOKEN");
            }

            StringBuilder url = new StringBuilder("https://www.googleapis.com/drive/v3/files?fields=files(id,name,mimeType,modifiedTime,size),nextPageToken");
            Integer pageSize = args.getInteger("pageSize");
            if (pageSize != null) url.append("&pageSize=").append(pageSize);
            String pageToken = args.getString("pageToken");
            if (pageToken != null) url.append("&pageToken=").append(pageToken);
            String q = args.getString("q");
            if (q != null) url.append("&q=").append(java.net.URLEncoder.encode(q, java.nio.charset.StandardCharsets.UTF_8));

            HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url.toString()))
                .header("Authorization", "Bearer " + token)
                .GET().build();
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

