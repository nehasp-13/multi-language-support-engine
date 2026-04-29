package com.internship.tool.controller;

import com.internship.tool.entity.RecordData;
import com.internship.tool.service.RecordService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class RecordController {

    private final RecordService service;

    public RecordController(RecordService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public RecordData create(@RequestBody RecordData r) {
        return service.create(r);
    }

    @GetMapping("/all")
    public List<RecordData> all() {
        return service.getAll();
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return service.health();
    }

    @PostMapping("/generate-report")
    public Map<String, Object> report(@RequestBody RecordData r) {
        return service.generateReport(r.getInputText());
    }
}