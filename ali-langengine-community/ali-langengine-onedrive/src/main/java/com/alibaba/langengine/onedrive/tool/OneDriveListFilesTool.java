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

public class OneDriveListFilesTool extends BaseTool {

    public OneDriveListFilesTool() {
        setName("OneDrive.listFiles");
        setHumanName("OneDrive 文件列表");
        setDescription("列出根目录或指定路径下的文件，支持分页。");
        setParameters("""
        {
          "type":"object",
          "properties":{
            "path":{"type":"string","description":"路径，默认 root"},
            "top":{"type":"integer","minimum":1,"maximum":200,"default":50},
            "skiptoken":{"type":"string","description":"分页token"}
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
            String token = System.getenv("ONEDRIVE_TOKEN");
            if (token == null || token.isEmpty()) {
                return wrapError("missing_token", "缺少环境变量 ONEDRIVE_TOKEN");
            }
            String path = args.getString("path");
            if (path == null) path = "";
            String base = "https://graph.microsoft.com/v1.0/me/drive/root";
            String listUrl = path.isEmpty() ? base + "/children" : base + ":/" + encode(path) + ":/children";

            StringBuilder url = new StringBuilder(listUrl);
            String sep = "?";
            Integer top = args.getInteger("top");
            if (top != null) { url.append(sep).append("$top=").append(top); sep = "&"; }
            String skiptoken = args.getString("skiptoken");
            if (skiptoken != null) { url.append(sep).append("$skiptoken=").append(encode(skiptoken)); }

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

