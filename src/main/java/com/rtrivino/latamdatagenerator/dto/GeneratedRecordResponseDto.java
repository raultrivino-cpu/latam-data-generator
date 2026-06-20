package com.rtrivino.latamdatagenerator.dto;

public record GeneratedRecordResponseDto(
        Long id,
        String name,
        String lastName,
        Integer age,
        String identificationNumber,
        String city,
        String country,
        String language,
        String recordType
) {
}