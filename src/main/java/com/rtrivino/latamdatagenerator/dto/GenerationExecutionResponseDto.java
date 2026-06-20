package com.rtrivino.latamdatagenerator.dto;

import java.time.LocalDateTime;
import java.util.List;

public record GenerationExecutionResponseDto(
        String executionId,
        Integer recordsGenerated,
        String fileName,
        String downloadUrl,
        LocalDateTime createdAt,
        List<GeneratedRecordResponseDto> records) {

}
