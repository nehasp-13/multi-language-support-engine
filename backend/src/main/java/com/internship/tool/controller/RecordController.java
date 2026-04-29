package com.internship.tool.controller;

import com.internship.tool.entity.RecordData;
import com.internship.tool.service.RecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RecordController {

    private final RecordService service;

    public RecordController(RecordService service) {
        this.service = service;
    }

    // 🔹 DAY 5
    @PostMapping("/create")
    public RecordData create(@RequestBody RecordData record) {
        return service.create(record);
    }

    @GetMapping("/all")
    public List<RecordData> getAll() {
        return service.getAll();
    }

    // 🔹 DAY 6
    @PostMapping("/generate-report")
    public Map<String, Object> generateReport(@RequestBody RecordData record) {
        return service.generateReport(record.getInputText());
    }
}