package com.internship.tool.controller;

import com.internship.tool.entity.Translation;
import com.internship.tool.service.TranslationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/translations")
@RequiredArgsConstructor
public class TranslationController {

    private final TranslationService translationService;

    // GET /all — paginated, any authenticated user
    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<Translation>> getAll(Pageable pageable) {
        log.info("GET /all called");
        return ResponseEntity.ok(translationService.getAll(pageable));
    }

    // GET /{id} — any authenticated user
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Translation> getById(@PathVariable Long id) {
        log.info("GET /{} called", id);
        return ResponseEntity.ok(translationService.getById(id));
    }

    // POST /create — only ADMIN
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Translation> create(
            @Valid @RequestBody Translation translation) {
        log.info("POST /create called");
        Translation created = translationService.create(translation);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /{id} — only ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Translation> update(
            @PathVariable Long id,
            @Valid @RequestBody Translation translation) {
        log.info("PUT /{} called", id);
        return ResponseEntity.ok(translationService.update(id, translation));
    }

    // DELETE /{id} — only ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /{} called", id);
        translationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET /search — any authenticated user
    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<Translation>> search(
            @RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(translationService.search(keyword, pageable));
    }

    // GET /status — any authenticated user
    @GetMapping("/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<Translation>> getByStatus(
            @RequestParam String status, Pageable pageable) {
        return ResponseEntity.ok(translationService.getByStatus(status, pageable));
    }

    // GET /language/source — any authenticated user
    @GetMapping("/language/source")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Translation>> getBySourceLanguage(
            @RequestParam String sourceLanguage) {
        return ResponseEntity.ok(
            translationService.getBySourceLanguage(sourceLanguage));
    }

    // GET /language/target — any authenticated user
    @GetMapping("/language/target")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Translation>> getByTargetLanguage(
            @RequestParam String targetLanguage) {
        return ResponseEntity.ok(
            translationService.getByTargetLanguage(targetLanguage));
    }
}