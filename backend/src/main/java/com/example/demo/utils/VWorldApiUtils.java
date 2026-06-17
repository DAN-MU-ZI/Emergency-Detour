package com.example.demo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class VWorldApiUtils {

    @Value("${vworld-api-key}")
    private String apiKey;

    private static final String SEARCH_URL = "https://api.vworld.kr/req/search";

    public String search(String query, String type, String category) throws IOException {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("service", "search");
        params.put("request", "search");
        params.put("version", "2.0");
        params.put("crs", "EPSG:4326");
        params.put("size", "10");
        params.put("page", "1");
        params.put("query", query);
        params.put("type", type);
        putIfNotBlank(params, "category", category);
        params.put("format", "json");
        params.put("errorformat", "json");
        params.put("key", apiKey);

        return get(SEARCH_URL, params, "application/json;charset=UTF-8");
    }

    private static String get(String baseUrl, Map<String, String> params, String contentType) throws IOException {
        URL url = new URL(baseUrl + "?" + toQueryString(params));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", contentType);

            int responseCode = conn.getResponseCode();
            InputStream responseStream = responseCode >= 200 && responseCode <= 300
                    ? conn.getInputStream()
                    : conn.getErrorStream();

            if (responseStream == null) {
                return "";
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(responseStream, StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                return response.toString();
            }
        } finally {
            conn.disconnect();
        }
    }

    private static String toQueryString(Map<String, String> params) {
        return params.entrySet()
                .stream()
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    private static void putIfNotBlank(Map<String, String> params, String key, String value) {
        if (value != null && !value.isBlank()) {
            params.put(key, value);
        }
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
