package com.rtrivino.latamdatagenerator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rtrivino.latamdatagenerator.entity.GeneratedRecordEntity;

public interface GeneratedRecordRepository extends JpaRepository<GeneratedRecordEntity, Long> {

    boolean existsByIdentificationNumber(String identificationNumber);

    List<GeneratedRecordEntity> findByExecutionId(String executionId);
}
