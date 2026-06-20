package com.rtrivino.latamdatagenerator.service;

import com.rtrivino.latamdatagenerator.mapper.GeneratedRecordMapper;
import com.rtrivino.latamdatagenerator.mapper.GenerationExecutionMapper;
import com.rtrivino.latamdatagenerator.repository.GenerationExecutionRepository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rtrivino.latamdatagenerator.domain.GeneratedDataRecord;
import com.rtrivino.latamdatagenerator.dto.GenerationExecutionResponseDto;
import com.rtrivino.latamdatagenerator.entity.GeneratedRecordEntity;
import com.rtrivino.latamdatagenerator.entity.GenerationExecutionEntity;
import com.rtrivino.latamdatagenerator.factory.GeneratedDataRecordFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeneratedDataService {

    private final GenerationExecutionMapper generationExecutionMapper;

    private final GeneratedRecordMapper generatedRecordMapper;

    private static final String FILE_NAME_TEMPLATE = "generated-data-%s.csv";

    private final GeneratedDataRecordFactory generatedDataRecordFactory;
    private final GenerationExecutionRepository generationExecutionRepository;
    private final CsvExportService csvExportService;

    private List<GeneratedRecordEntity> generateUniqueRecords(Integer quantity) {
        Set<String> fullNames = new HashSet<>();
        Set<String> documents = new HashSet<>();
        List<GeneratedRecordEntity> records = new ArrayList<>();

        while (records.size() < quantity) {
            GeneratedDataRecord generatedRecord = generatedDataRecordFactory.createRandom();

            String fullNameKey = generatedRecord.getFullName().toLowerCase();
            String documentKey = generatedRecord.getIdentificationNumber();

            if (fullNames.contains(fullNameKey) || documents.contains(documentKey)) {
                continue;
            }

            fullNames.add(fullNameKey);
            documents.add(documentKey);
            records.add(generatedRecordMapper.toEntity(generatedRecord));
        }

        return records;
    }

    @Transactional
    public GenerationExecutionResponseDto generate(Integer quantity) {
        String executionId = UUID.randomUUID().toString();
        String fileName = String.format(FILE_NAME_TEMPLATE, executionId);

        List<GeneratedRecordEntity> records = generateUniqueRecords(quantity);

        GenerationExecutionEntity execution = new GenerationExecutionEntity(
                executionId, records.size(), fileName, null);

        records.forEach(execution::addRecord);

        GenerationExecutionEntity savedExecution = generationExecutionRepository.save(execution);
        Path csvPath = csvExportService.export(fileName, savedExecution.getRecords());
        savedExecution.setFilePath(csvPath.toString());
        GenerationExecutionEntity updateExecution = generationExecutionRepository.save(savedExecution);

        return generationExecutionMapper.toResponseDto(updateExecution);
    }

    @Transactional
    public Path getGeneratedFile(String executionId) {
        GenerationExecutionEntity execution = generationExecutionRepository.findById(executionId)
                .orElseThrow(() -> new IllegalArgumentException("Código no encontrado:" + executionId));

        if (execution.getFilePath() == null || execution.getFilePath().isBlank()) {
            throw new IllegalStateException("La ruta del archivo no está disponible para el id:" + executionId);
        }

        return Path.of(execution.getFilePath());
    }

    @Transactional(readOnly = true)
    public List<GenerationExecutionResponseDto> findAllExecutions() {
        return generationExecutionRepository.findAll().stream().map(generationExecutionMapper::toResponseDto).toList();
    }

    @Transactional(readOnly = true)
    public GenerationExecutionResponseDto findExecutionById(String executionId){
        GenerationExecutionEntity execution = generationExecutionRepository.findById(executionId.trim()).orElseThrow(() -> new IllegalArgumentException("Id no encontrado: "+executionId));

        return generationExecutionMapper.toResponseDto(execution);
    }
}
