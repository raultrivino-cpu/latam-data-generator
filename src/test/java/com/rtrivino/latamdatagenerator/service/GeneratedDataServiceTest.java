package com.rtrivino.latamdatagenerator.service;

import com.rtrivino.latamdatagenerator.domain.NaturalPerson;
import com.rtrivino.latamdatagenerator.dto.GenerationExecutionResponseDto;
import com.rtrivino.latamdatagenerator.entity.GeneratedRecordEntity;
import com.rtrivino.latamdatagenerator.entity.GenerationExecutionEntity;
import com.rtrivino.latamdatagenerator.enums.RecordType;
import com.rtrivino.latamdatagenerator.factory.GeneratedDataRecordFactory;
import com.rtrivino.latamdatagenerator.mapper.GeneratedRecordMapper;
import com.rtrivino.latamdatagenerator.mapper.GenerationExecutionMapper;
import com.rtrivino.latamdatagenerator.repository.GenerationExecutionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeneratedDataServiceTest {

    @Mock
    private GenerationExecutionMapper generationExecutionMapper;

    @Mock
    private GeneratedRecordMapper generatedRecordMapper;

    @Mock
    private GeneratedDataRecordFactory generatedDataRecordFactory;

    @Mock
    private GenerationExecutionRepository generationExecutionRepository;

    @Mock
    private CsvExportService csvExportService;

    @InjectMocks
    private GeneratedDataService generatedDataService;

    @Test
    void shouldGenerateRequestedAmountOfRecords() {
        NaturalPerson generatedRecord = new NaturalPerson(
                "Raul",
                "Trivino",
                35,
                "123456789",
                "Bogota",
                "Colombia",
                "Spanish"
        );

        GeneratedRecordEntity entity = new GeneratedRecordEntity(
                "Raul",
                "Trivino",
                35,
                "123456789",
                "Bogota",
                "Colombia",
                "Spanish",
                RecordType.NATURAL_PERSON
        );

        GenerationExecutionResponseDto responseDto = new GenerationExecutionResponseDto(
                "execution-id",
                1,
                "generated-data-execution-id.csv",
                "/api/generated-data/executions/execution-id/file",
                LocalDateTime.now(),
                List.of()
        );

        when(generatedDataRecordFactory.createRandom()).thenReturn(generatedRecord);
        when(generatedRecordMapper.toEntity(generatedRecord)).thenReturn(entity);
        when(generationExecutionRepository.save(any(GenerationExecutionEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(csvExportService.export(anyString(), anyList())).thenReturn(Path.of("output/test.csv"));
        when(generationExecutionMapper.toResponseDto(any(GenerationExecutionEntity.class))).thenReturn(responseDto);

        GenerationExecutionResponseDto result = generatedDataService.generate(1);

        assertNotNull(result);
        assertEquals(1, result.recordsGenerated());

        verify(generatedDataRecordFactory, times(1)).createRandom();
        verify(csvExportService, times(1)).export(anyString(), anyList());
        verify(generationExecutionRepository, times(2)).save(any(GenerationExecutionEntity.class));
    }

    @Test
    void shouldFindExecutionById() {
        GenerationExecutionEntity execution = new GenerationExecutionEntity(
                "execution-id",
                1,
                "generated-data-execution-id.csv",
                "output/generated-data-execution-id.csv"
        );

        GenerationExecutionResponseDto responseDto = new GenerationExecutionResponseDto(
                "execution-id",
                1,
                "generated-data-execution-id.csv",
                "/api/generated-data/executions/execution-id/file",
                LocalDateTime.now(),
                List.of()
        );

        when(generationExecutionRepository.findById("execution-id")).thenReturn(Optional.of(execution));
        when(generationExecutionMapper.toResponseDto(execution)).thenReturn(responseDto);

        GenerationExecutionResponseDto result = generatedDataService.findExecutionById("execution-id");

        assertEquals("execution-id", result.executionId());
        assertEquals(1, result.recordsGenerated());
    }

    @Test
    void shouldThrowExceptionWhenExecutionDoesNotExist() {
        when(generationExecutionRepository.findById("wrong-id")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> generatedDataService.findExecutionById("wrong-id")
        );

        assertTrue(exception.getMessage().contains("wrong-id"));
    }

    @Test
    void shouldReturnGeneratedFilePath() {
        GenerationExecutionEntity execution = new GenerationExecutionEntity(
                "execution-id",
                1,
                "generated-data-execution-id.csv",
                "output/generated-data-execution-id.csv"
        );

        when(generationExecutionRepository.findById("execution-id")).thenReturn(Optional.of(execution));

        Path result = generatedDataService.getGeneratedFile("execution-id");

        assertEquals(Path.of("output/generated-data-execution-id.csv"), result);
    }
}