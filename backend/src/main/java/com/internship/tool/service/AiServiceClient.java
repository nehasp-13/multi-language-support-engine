package com.internship.tool.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Component
public class AiServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Async
    public CompletableFuture<String> getDescription(String text) {
        String url = "http://127.0.0.1:5000/ai/describe";

        Map<String, String> req = new HashMap<>();
        req.put("text", text);

        try {
            Map res = restTemplate.postForObject(url, req, Map.class);
            return CompletableFuture.completedFuture((String) res.get("result"));
        } catch (Exception e) {
            return CompletableFuture.completedFuture("AI Service Not Available");
        }
    }

    @Async
    public CompletableFuture<List<Map<String, Object>>> getRecommendations(String text) {
        String url = "http://127.0.0.1:5000/ai/recommend";

        Map<String, String> req = new HashMap<>();
        req.put("text", text);

        try {
            Map res = restTemplate.postForObject(url, req, Map.class);
            return CompletableFuture.completedFuture((List<Map<String, Object>>) res.get("recommendations"));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
    }
}