package com.rtrivino.latamdatagenerator.controller;

import com.rtrivino.latamdatagenerator.dto.GenerationExecutionResponseDto;
import com.rtrivino.latamdatagenerator.service.GeneratedDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GeneratedDataController.class)
class GeneratedDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GeneratedDataService generatedDataService;

    @Test
    void shouldGenerateDataSuccessfully() throws Exception {
        GenerationExecutionResponseDto response = new GenerationExecutionResponseDto(
                "execution-id",
                10,
                "generated-data-execution-id.csv",
                "/api/generated-data/executions/execution-id/file",
                LocalDateTime.now(),
                List.of()
        );

        when(generatedDataService.generate(10)).thenReturn(response);

        mockMvc.perform(post("/api/generated-data")
                        .param("quantity", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.executionId").value("execution-id"))
                .andExpect(jsonPath("$.recordsGenerated").value(10))
                .andExpect(jsonPath("$.fileName").value("generated-data-execution-id.csv"));
    }

    @Test
    void shouldFindAllExecutions() throws Exception {
        GenerationExecutionResponseDto response = new GenerationExecutionResponseDto(
                "execution-id",
                5,
                "generated-data-execution-id.csv",
                "/api/generated-data/executions/execution-id/file",
                LocalDateTime.now(),
                List.of()
        );

        when(generatedDataService.findAllExecutions()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/generated-data/executions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].executionId").value("execution-id"))
                .andExpect(jsonPath("$[0].recordsGenerated").value(5));
    }

    @Test
    void shouldFindExecutionById() throws Exception {
        GenerationExecutionResponseDto response = new GenerationExecutionResponseDto(
                "execution-id",
                3,
                "generated-data-execution-id.csv",
                "/api/generated-data/executions/execution-id/file",
                LocalDateTime.now(),
                List.of()
        );

        when(generatedDataService.findExecutionById("execution-id")).thenReturn(response);

        mockMvc.perform(get("/api/generated-data/executions/execution-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.executionId").value("execution-id"))
                .andExpect(jsonPath("$.recordsGenerated").value(3));
    }

    @Test
    void shouldDownloadGeneratedFile() throws Exception {
        Path tempFile = Files.createTempFile("generated-data-test", ".csv");
        Files.writeString(tempFile, "id,name\n1,Raul");

        when(generatedDataService.getGeneratedFile("execution-id")).thenReturn(tempFile);

        mockMvc.perform(get("/api/generated-data/executions/execution-id/file"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/csv"))
                .andExpect(header().string("Content-Disposition",
                        "attachment; filename=\"" + tempFile.getFileName() + "\""));

        Files.deleteIfExists(tempFile);
    }
}