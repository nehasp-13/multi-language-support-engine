package com.internship.tool.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AiServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public String callDescribe(String text) {

        String url = "http://localhost:5000/describe";

        Map<String, String> request = Map.of("text", text);

        Map response = restTemplate.postForObject(url, request, Map.class);

        return response.get("result").toString();
    }
}