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

package com.alibaba.langengine.baidumap.sdk;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.alibaba.langengine.baidumap.BaiduMapConfiguration.BAIDU_MAP_API_KEY;
import static com.alibaba.langengine.baidumap.sdk.BaiduMapConstant.BAIDU_MAP_BASE_URL;
import static com.alibaba.langengine.baidumap.sdk.BaiduMapConstant.DEFAULT_TIMEOUT;
import static com.alibaba.langengine.baidumap.sdk.BaiduMapConstant.PLACE_SEARCH_API_ENDPOINT;

public class BaiduMapClient {

    private final String apiKey;

    private final OkHttpClient httpClient;

    private final ObjectMapper objectMapper;

    private static final Map<String, Method> PLACE_SEARCH_REQUEST_GETTERS;

    static {
        // Use the reflection API to map the getter methods of the Request fields to their corresponding JsonProperty names.
        PLACE_SEARCH_REQUEST_GETTERS = Arrays.stream(PlaceSearchRequest.class.getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .collect(Collectors.toMap(f -> {
                        if (f.getAnnotation(JsonProperty.class) == null) {
                            return f.getName();
                        }
                        return f.getAnnotation(JsonProperty.class).value();
                    }, f -> {
                        String name = f.getName();
                        String getterName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                        try {
                            return PlaceSearchRequest.class.getMethod(getterName);
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException(e);
                        }
                    }));
    }

    public BaiduMapClient(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public BaiduMapClient() {
        this(BAIDU_MAP_API_KEY);
    }

    /**
     * Use Baidu Map API to Location Search. (Full Request)
     * Doc: <a href="https://lbs.baidu.com/faq/api?title=webapi/guide/webservice-placeapiV3/interfaceDocumentV3">...</a>
     * @param placeSearchRequest request
     * @return response
     * @throws BaiduMapException exception
     */
    public PlaceSearchResponse placeSearch(PlaceSearchRequest placeSearchRequest) throws BaiduMapException {
        try {
            // Build the HTTP URL with query parameters
            HttpUrl.Builder urlBuilder = HttpUrl.parse(BAIDU_MAP_BASE_URL + PLACE_SEARCH_API_ENDPOINT).newBuilder();

            // obtain field properties and apply them to set request headers.
            PLACE_SEARCH_REQUEST_GETTERS.forEach((key, value) -> {
                try {
                    Object obj = value.invoke(placeSearchRequest);
                    if (obj != null) {
                        urlBuilder.addQueryParameter(key, obj.toString());
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            urlBuilder.addQueryParameter("ak", apiKey);

            // Create the HTTP request
            Request httpRequest = new Request.Builder()
                    .url(urlBuilder.build())
                    .addHeader("Accept", "application/json")
                    .get()
                    .build();

            // Execute the request
            try (Response response = httpClient.newCall(httpRequest).execute()) {
                if (!response.isSuccessful()) {
                    throw new BaiduMapException("API request failed: " + response.code() + " " + response.message());
                }
                ResponseBody body = response.body();
                if (body == null) {
                    throw new BaiduMapException("API Returns Empty Body");
                }
                return objectMapper.readValue(body.string(), new TypeReference<PlaceSearchResponse>() {});
            }
        } catch (BaiduMapException e) {
            throw e;
        } catch (Exception e) {
            throw new BaiduMapException(e.getMessage(), e);
        }
    }

    /**
     * Use Baidu Map API to Location Search. (Simple Request)
     * Doc: <a href="https://lbs.baidu.com/faq/api?title=webapi/guide/webservice-placeapiV3/interfaceDocumentV3">...</a>
     * @param query keywords
     * @param region Retrieve administrative divisions and regions
     * @return response
     * @throws BaiduMapException exception
     */
    public PlaceSearchResponse placeSearch(String query, String region) throws BaiduMapException {
        PlaceSearchRequest placeSearchRequest = new PlaceSearchRequest();
        placeSearchRequest.setQuery(query);
        placeSearchRequest.setRegion(region);
        return this.placeSearch(placeSearchRequest);
    }

}
