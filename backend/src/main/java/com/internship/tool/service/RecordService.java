package com.internship.tool.service;

import com.internship.tool.entity.RecordData;
import com.internship.tool.repository.RecordRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecordService {

    private final RecordRepository repo;
    private final AiServiceClient ai;
    private final CacheService cache;

    private long startTime = System.currentTimeMillis();
    private long totalTime = 0;
    private int count = 0;

    public RecordService(RecordRepository repo, AiServiceClient ai, CacheService cache) {
        this.repo = repo;
        this.ai = ai;
        this.cache = cache;
    }

    // ✅ DAY 5 - CREATE (ASYNC)
    public RecordData create(RecordData r) {
        r.setResult("Processing...");
        RecordData saved = repo.save(r);

        ai.getDescription(r.getInputText())
                .thenAccept(res -> {
                    saved.setResult(res);
                    repo.save(saved);
                });

        return saved;
    }

    // ✅ GET ALL
    public List<RecordData> getAll() {
        return repo.findAll();
    }

    // ✅ DAY 7 - HEALTH API
    public Map<String, Object> health() {
        Map<String, Object> m = new HashMap<>();

        long uptime = System.currentTimeMillis() - startTime;
        long avg = count == 0 ? 0 : totalTime / count;

        m.put("model", "Groq / LLaMA3");
        m.put("uptime_ms", uptime);
        m.put("avg_response_time_ms", avg);

        return m;
    }

    // ✅ DAY 6 + DAY 7 - GENERATE REPORT + CACHE + PERFORMANCE
    public Map<String, Object> generateReport(String input) {

        Map<String, Object> res = new HashMap<>();

        // 🔥 START TIME (IMPORTANT)
        long start = System.currentTimeMillis();

        String key = cache.key(input);

        // 🔹 CACHE CHECK
        String cached = cache.get(key);
        if (cached != null) {
            res.put("cached", true);
            res.put("data", cached);

            // 🔥 UPDATE TIME FOR CACHE ALSO
            long end = System.currentTimeMillis();
            totalTime += (end - start);
            count++;

            return res;
        }

        try {
            String desc = ai.getDescription(input).get();
            List<Map<String, Object>> rec = ai.getRecommendations(input).get();

            res.put("title", "AI Report");
            res.put("summary", "Summary for: " + input);
            res.put("overview", desc);
            res.put("key_items", List.of("Analyzed", "Generated", "Recommended"));
            res.put("recommendations", rec);

            // 🔥 STORE IN CACHE
            cache.set(key, res.toString());

        } catch (Exception e) {
            res.put("error", "Failed");
        }

        // 🔥 END TIME
        long end = System.currentTimeMillis();
        totalTime += (end - start);
        count++;

        return res;
    }
}