package com.internship.tool.repository;

import com.internship.tool.entity.RecordData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<RecordData, Long> {
}