package com.internship.tool.service;

import com.internship.tool.entity.Translation;
import com.internship.tool.exception.DuplicateResourceException;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.exception.ValidationException;
import com.internship.tool.repository.TranslationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslationServiceImpl implements TranslationService {

    private final TranslationRepository translationRepository;

    @Override
    @CacheEvict(value = "translations", allEntries = true)
    public Translation create(Translation translation) {
        validateTranslation(translation);
        log.info("Creating translation from {} to {}",
            translation.getSourceLanguage(), translation.getTargetLanguage());
        return translationRepository.save(translation);
    }

    @Override
    @Cacheable(value = "translations", key = "#id")
    public Translation getById(Long id) {
        return translationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Translation not found with id: " + id));
    }

    @Override
    @Cacheable(value = "translations", key = "'all_' + #pageable.pageNumber")
    public Page<Translation> getAll(Pageable pageable) {
        return translationRepository.findAll(pageable);
    }

    @Override
    @CacheEvict(value = "translations", allEntries = true)
    public Translation update(Long id, Translation translation) {
        Translation existing = getById(id);
        validateTranslation(translation);
        existing.setSourceText(translation.getSourceText());
        existing.setTranslatedText(translation.getTranslatedText());
        existing.setSourceLanguage(translation.getSourceLanguage());
        existing.setTargetLanguage(translation.getTargetLanguage());
        existing.setStatus(translation.getStatus());
        return translationRepository.save(existing);
    }

    @Override
    @CacheEvict(value = "translations", allEntries = true)
    public void delete(Long id) {
        Translation existing = getById(id);
        translationRepository.delete(existing);
    }

    @Override
    @Cacheable(value = "translations", key = "'search_' + #keyword + #pageable.pageNumber")
    public Page<Translation> search(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new ValidationException("Search keyword cannot be empty");
        }
        return translationRepository.searchByKeyword(keyword, pageable);
    }

    @Override
    @Cacheable(value = "translations", key = "'src_' + #sourceLanguage")
    public List<Translation> getBySourceLanguage(String sourceLanguage) {
        if (sourceLanguage == null || sourceLanguage.trim().isEmpty()) {
            throw new ValidationException("Source language cannot be empty");
        }
        return translationRepository.findBySourceLanguage(sourceLanguage);
    }

    @Override
    @Cacheable(value = "translations", key = "'tgt_' + #targetLanguage")
    public List<Translation> getByTargetLanguage(String targetLanguage) {
        if (targetLanguage == null || targetLanguage.trim().isEmpty()) {
            throw new ValidationException("Target language cannot be empty");
        }
        return translationRepository.findByTargetLanguage(targetLanguage);
    }

    @Override
    @Cacheable(value = "translations", key = "'status_' + #status + #pageable.pageNumber")
    public Page<Translation> getByStatus(String status, Pageable pageable) {
        if (status == null || status.trim().isEmpty()) {
            throw new ValidationException("Status cannot be empty");
        }
        return translationRepository.findByStatus(status, pageable);
    }

    private void validateTranslation(Translation translation) {
        if (translation.getSourceText() == null ||
            translation.getSourceText().trim().isEmpty()) {
            throw new ValidationException("Source text cannot be empty");
        }
        if (translation.getTranslatedText() == null ||
            translation.getTranslatedText().trim().isEmpty()) {
            throw new ValidationException("Translated text cannot be empty");
        }
        if (translation.getSourceLanguage() == null ||
            translation.getSourceLanguage().trim().isEmpty()) {
            throw new ValidationException("Source language cannot be empty");
        }
        if (translation.getTargetLanguage() == null ||
            translation.getTargetLanguage().trim().isEmpty()) {
            throw new ValidationException("Target language cannot be empty");
        }
        if (translation.getSourceLanguage().equals(translation.getTargetLanguage())) {
            throw new DuplicateResourceException(
                "Source and target language cannot be the same");
        }
    }
}