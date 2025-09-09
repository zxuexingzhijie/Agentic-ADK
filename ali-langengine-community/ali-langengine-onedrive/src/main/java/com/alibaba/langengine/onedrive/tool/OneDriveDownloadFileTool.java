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

public class OneDriveDownloadFileTool extends BaseTool {

    public OneDriveDownloadFileTool() {
        setName("OneDrive.downloadFile");
        setHumanName("OneDrive 下载文件");
        setDescription("根据itemId下载文件，返回base64内容。");
        setParameters("""
        {
          "type":"object",
          "properties":{
            "itemId":{"type":"string","description":"文件itemId"}
          },
          "required":["itemId"],
          "additionalProperties":false
        }
        """);
    }

    @Override
    public ToolExecuteResult run(String toolInput, ExecutionContext executionContext) {
        try {
            JSONObject args = JSON.parseObject(toolInput);
            String itemId = args.getString("itemId");
            String token = System.getenv("ONEDRIVE_TOKEN");
            if (token == null || token.isEmpty()) {
                return wrapError("missing_token", "缺少环境变量 ONEDRIVE_TOKEN");
            }
            String url = "https://graph.microsoft.com/v1.0/me/drive/items/" + itemId + "/content";
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
                ok.put("data", new JSONObject() {{ put("itemId", itemId); put("contentBase64", b64); }});
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

