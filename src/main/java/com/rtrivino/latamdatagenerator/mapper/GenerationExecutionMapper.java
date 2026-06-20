package com.rtrivino.latamdatagenerator.mapper;

import org.springframework.stereotype.Component;

import com.rtrivino.latamdatagenerator.dto.GenerationExecutionResponseDto;
import com.rtrivino.latamdatagenerator.entity.GenerationExecutionEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GenerationExecutionMapper {
    public static final String DOWNLOAD_URL_TEMPLATE = "/api/generated-data/executions/%s/file";

    private final GeneratedRecordMapper generatedRecordMapper;

    public GenerationExecutionResponseDto toResponseDto(GenerationExecutionEntity entity){
        return new GenerationExecutionResponseDto(
            entity.getId(), 
            entity.getRecordsGenerated(), 
            entity.getFileName(), 
            String.format(DOWNLOAD_URL_TEMPLATE, entity.getId()), 
            entity.getCreateAt(), 
            entity.getRecords().stream().map(generatedRecordMapper::toResponseDto).toList()
        );
    }
}
