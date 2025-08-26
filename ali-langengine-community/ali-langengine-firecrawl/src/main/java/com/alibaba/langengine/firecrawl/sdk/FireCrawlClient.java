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

package com.alibaba.langengine.firecrawl.sdk;

import com.alibaba.langengine.firecrawl.sdk.request.ScrapeRequest;
import com.alibaba.langengine.firecrawl.sdk.response.ErrorResponse;
import com.alibaba.langengine.firecrawl.sdk.response.ScrapeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

import static com.alibaba.langengine.firecrawl.sdk.FireCrawlConstant.FIRE_CRAWL_BASE_URL;

public class FireCrawlClient {

    private final OkHttpClient client;

    private final ObjectMapper objectMapper;

    private final String apiKey;

    public FireCrawlClient(String apiKey) {
        this.client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
        this.apiKey = apiKey;
    }

    /**
     * Scrape a URL using FireCrawl service
     *
     * @param request The scrape request parameters
     * @return The scrape response
     * @throws FireCrawlException if an error occurs during the request
     */
    public ScrapeResponse scrape(ScrapeRequest request) throws FireCrawlException {
        try {
            String json = objectMapper.writeValueAsString(request);
            RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

            Request httpRequest = new Request.Builder()
                    .url(FIRE_CRAWL_BASE_URL + "/scrape")
                    .post(body)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .build();

            try (Response response = client.newCall(httpRequest).execute()) {
                if (!response.isSuccessful()) {
                    handleErrorResponse(response);
                }

                ResponseBody responseBody = response.body();
                if (responseBody == null) {
                    throw new FireCrawlException("Empty response body");
                }

                return objectMapper.readValue(responseBody.string(), ScrapeResponse.class);
            }
        } catch (IOException e) {
            throw new FireCrawlException("Error occurred while scraping", e);
        }
    }

    private void handleErrorResponse(Response response) throws IOException, FireCrawlException {
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            String errorBody = responseBody.string();
            try {
                ErrorResponse errorResponse = objectMapper.readValue(errorBody, ErrorResponse.class);
                throw new FireCrawlException("API Error: " + errorResponse.getError() + " (Code: " + response.code() + ")");
            } catch (Exception e) {
                throw new FireCrawlException("HTTP Error " + response.code() + ": " + errorBody);
            }
        } else {
            throw new FireCrawlException("HTTP Error " + response.code());
        }
    }
}
