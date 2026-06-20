package com.rtrivino.latamdatagenerator.service;

import com.opencsv.CSVWriter;
import com.rtrivino.latamdatagenerator.entity.GeneratedRecordEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class CsvExportService {

    private static final String[] CSV_HEADER = {
            "id",
            "name",
            "lastName",
            "age",
            "identificationNumber",
            "city",
            "country",
            "language",
            "recordType"
    };

    private final String outputDirectory;

    public CsvExportService(@Value("${app.files.output-directory}") String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public Path export(String fileName, List<GeneratedRecordEntity> records) {
        try {
            Path outputPath = Path.of(outputDirectory);
            Files.createDirectories(outputPath);

            Path filePath = outputPath.resolve(fileName);

            try (CSVWriter writer = new CSVWriter(Files.newBufferedWriter(filePath, StandardCharsets.UTF_8))) {
                writer.writeNext(CSV_HEADER);

                for (GeneratedRecordEntity record : records) {
                    writer.writeNext(toCsvRow(record));
                }
            }

            return filePath;
        } catch (IOException exception) {
            throw new IllegalStateException("Error generating CSV file", exception);
        }
    }

    private String[] toCsvRow(GeneratedRecordEntity record) {
        return new String[] {
                String.valueOf(record.getId()),
                record.getName(),
                record.getLastName(),
                String.valueOf(record.getAge()),
                record.getIdentificationNumber(),
                record.getCity(),
                record.getCountry(),
                record.getLanguage(),
                record.getRecordType().name()
        };
    }
}