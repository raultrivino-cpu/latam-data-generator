package com.rtrivino.latamdatagenerator.mapper;

import org.springframework.stereotype.Component;

import com.rtrivino.latamdatagenerator.domain.GeneratedDataRecord;
import com.rtrivino.latamdatagenerator.dto.GeneratedRecordResponseDto;
import com.rtrivino.latamdatagenerator.entity.GeneratedRecordEntity;

@Component
public class GeneratedRecordMapper {

    public GeneratedRecordEntity toEntity(GeneratedDataRecord record){
        return new GeneratedRecordEntity(
            record.getName(), 
            record.getLastName(),
            record.getAge(), 
            record.getIdentificationNumber(), 
            record.getCity(), 
            record.getCountry(), 
            record.getLanguage(),
            record.getRecordType()
        );
    } 

    public GeneratedRecordResponseDto toResponseDto(GeneratedRecordEntity entity){
        return new GeneratedRecordResponseDto(
            entity.getId(), 
            entity.getName(),
            entity.getLastName(), 
            entity.getAge(), 
            entity.getIdentificationNumber(), 
            entity.getCity(), 
            entity.getCountry(),
            entity.getLanguage(), 
            entity.getRecordType().name()
        );
    }
}
