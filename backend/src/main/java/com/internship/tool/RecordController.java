package com.internship.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/records")
@CrossOrigin
public class RecordController {

    private List<Map<String, String>> list = new ArrayList<>();

    // GET
    @GetMapping
    public List<Map<String, String>> getRecords() {
        if (list.isEmpty()) {
            Map<String, String> data = new HashMap<>();
            data.put("title", "Hello World");
            data.put("language", "English");
            list.add(data);
        }
        return list;
    }

    // POST
    @PostMapping
    public Map<String, String> addRecord(@RequestBody Map<String, String> newData) {
        list.add(newData);
        return newData;
    }

    // DELETE
    @DeleteMapping("/{index}")
    public String deleteRecord(@PathVariable int index) {
        if (index >= 0 && index < list.size()) {
            list.remove(index);
            return "Deleted";
        }
        return "Invalid index";
    }

    // UPDATE
    @PutMapping("/{index}")
    public Map<String, String> updateRecord(
            @PathVariable int index,
            @RequestBody Map<String, String> updatedData) {

        if (index >= 0 && index < list.size()) {
            list.set(index, updatedData);
            return updatedData;
        }
        return null;
    }
    @GetMapping("/stats")
public Map<String, Integer> getStats() {

    int total = list.size();
    int english = 0;

    for (Map<String, String> item : list) {
        if ("english".equalsIgnoreCase(item.get("language"))) {
            english++;
        }
    }

    Map<String, Integer> stats = new HashMap<>();
    stats.put("total", total);
    stats.put("english", english);

    return stats;
}
}