package com.internship.tool.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Component
public class AiServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();

    // 🔹 GET DESCRIPTION
    @Async
    public CompletableFuture<String> getDescription(String text) {
        String url = "http://127.0.0.1:5000/ai/describe";

        Map<String, String> request = new HashMap<>();
        request.put("text", text);

        try {
            Map response = restTemplate.postForObject(url, request, Map.class);
            return CompletableFuture.completedFuture((String) response.get("result"));
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture("AI Service Not Available");
        }
    }

    // 🔹 GET RECOMMENDATIONS
    @Async
    public CompletableFuture<List<Map<String, Object>>> getRecommendations(String text) {
        String url = "http://127.0.0.1:5000/ai/recommend";

        Map<String, String> request = new HashMap<>();
        request.put("text", text);

        try {
            List response = restTemplate.postForObject(url, request, List.class);
            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
    }
}