package com.internship.tool.service;

import com.internship.tool.entity.RecordData;
import com.internship.tool.repository.RecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Service
public class RecordService {

    private final RecordRepository repo;

    public RecordService(RecordRepository repo) {
        this.repo = repo;
    }

    public RecordData save(RecordData record) {

        // 🔥 Call AI Service
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://127.0.0.1:5000/describe";

        Map<String, String> request = new HashMap<>();
        request.put("text", record.getInputText());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, entity, Map.class);

        String aiResult = (String) response.getBody().get("result");

        // set AI result
        record.setResult(aiResult);

        return repo.save(record);
    }

    public List<RecordData> getAll() {
        return repo.findAll();
    }
}