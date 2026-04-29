package com.internship.tool.service;

import com.internship.tool.entity.RecordData;
import com.internship.tool.repository.RecordRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class RecordService {

    private final RecordRepository repo;
    private final AiServiceClient aiServiceClient;

    public RecordService(RecordRepository repo, AiServiceClient aiServiceClient) {
        this.repo = repo;
        this.aiServiceClient = aiServiceClient;
    }

    // 🔹 DAY 5 CREATE (ASYNC)
    public RecordData create(RecordData record) {
        record.setResult("Processing...");
        RecordData saved = repo.save(record);

        aiServiceClient.getDescription(record.getInputText())
                .thenAccept(result -> {
                    saved.setResult(result);
                    repo.save(saved);
                });

        return saved;
    }

    public List<RecordData> getAll() {
        return repo.findAll();
    }

    // 🔹 DAY 6 GENERATE REPORT
    public Map<String, Object> generateReport(String inputText) {

        Map<String, Object> report = new HashMap<>();

        try {
            String description = aiServiceClient.getDescription(inputText).get();
            List<Map<String, Object>> recommendations =
                    aiServiceClient.getRecommendations(inputText).get();

            report.put("title", "AI Generated Report");
            report.put("summary", "Summary for: " + inputText);
            report.put("overview", description);

            report.put("key_items", List.of(
                    "Input analyzed",
                    "AI description generated",
                    "Recommendations created"
            ));

            report.put("recommendations", recommendations);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            report.put("error", "Failed to generate report");
        }

        return report;
    }
}