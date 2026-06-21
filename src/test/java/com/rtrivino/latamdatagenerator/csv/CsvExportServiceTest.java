package com.rtrivino.latamdatagenerator.csv;

import com.rtrivino.latamdatagenerator.entity.GeneratedRecordEntity;
import com.rtrivino.latamdatagenerator.enums.RecordType;
import com.rtrivino.latamdatagenerator.service.CsvExportService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvExportServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldCreateCsvFile() throws Exception {
        CsvExportService csvExportService = new CsvExportService(tempDir.toString());

        GeneratedRecordEntity record = new GeneratedRecordEntity(
                "Raul",
                "Trivino",
                35,
                "123456789",
                "Bogota",
                "Colombia",
                "Spanish",
                RecordType.NATURAL_PERSON
        );

        Path filePath = csvExportService.export("test-file.csv", List.of(record));

        assertTrue(Files.exists(filePath));
        assertTrue(Files.size(filePath) > 0);

        String content = Files.readString(filePath);

        assertTrue(content.contains("Raul"));
        assertTrue(content.contains("Trivino"));
        assertTrue(content.contains("123456789"));
        assertTrue(content.contains("NATURAL_PERSON"));
    }
}