package com.rtrivino.latamdatagenerator.controller;

import com.rtrivino.latamdatagenerator.service.GeneratedDataService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rtrivino.latamdatagenerator.dto.GenerationExecutionResponseDto;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/generated-data")
@RequiredArgsConstructor
public class GeneratedDataController {

    private final GeneratedDataService generatedDataService;

    @PostMapping
    public GenerationExecutionResponseDto generate(@RequestParam Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity debe ser mayor a cero");
        }

        return generatedDataService.generate(quantity);
    }

    @GetMapping("/executions/{executionId}/file")
    public ResponseEntity<Resource> downloadFile(@PathVariable String executionId) {
        Path filePath = generatedDataService.getGeneratedFile(executionId);
        Resource resource = new FileSystemResource(filePath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName() + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

    @GetMapping("/executions")
    public List<GenerationExecutionResponseDto> findAllExecutions() {
        return generatedDataService.findAllExecutions();
    }

    @GetMapping("/executions/{executionId}")
    public GenerationExecutionResponseDto getMethodName(@PathVariable String executionId) {
        return generatedDataService.findExecutionById(executionId.trim());
    }

}
