package com.rtrivino.latamdatagenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rtrivino.latamdatagenerator.entity.GenerationExecutionEntity;

public interface GenerationExecutionRepository extends JpaRepository<GenerationExecutionEntity, String> {

}
