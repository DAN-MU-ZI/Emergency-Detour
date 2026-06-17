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
public final class ExternalApiUtils {

    @Value("${its-api-key}")
    private String API_KEY;
    // 재난상황정보 조회 API 문서: https://www.its.go.kr/opendata/opendataList?service=disaster
    private static final String DISASTER_INFO_URL = "https://openapi.its.go.kr:9443/disasterInfo";
    // 돌발상황정보 조회 API 문서: https://www.its.go.kr/opendata/opendataList?service=event
    private static final String EVENT_INFO_URL = "https://openapi.its.go.kr:9443/eventInfo";

    public String getDisasterInfo() throws IOException {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("apiKey", API_KEY);
        params.put("category", "D");
        params.put("eventType", "D03,D04");
        params.put("startDate", "20251201");
        params.put("endDate", "20251205");
        params.put("minX", "126.800000");
        params.put("maxX", "127.890000");
        params.put("minY", "34.900000");
        params.put("maxY", "35.100000");
        params.put("getType", "json");

        return get(DISASTER_INFO_URL, params, "application/json;charset=UTF-8");
    }

    public String getEventInfo() throws IOException {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("apiKey", API_KEY);
        params.put("type", "all");
        params.put("eventType", "all");
        params.put("minX", "126.800000");
        params.put("maxX", "127.890000");
        params.put("minY", "34.900000");
        params.put("maxY", "35.100000");
        params.put("getType", "json");

        return get(EVENT_INFO_URL, params, "application/json;charset=UTF-8");
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

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
