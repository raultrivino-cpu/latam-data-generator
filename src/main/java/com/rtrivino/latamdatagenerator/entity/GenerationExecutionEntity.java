package com.rtrivino.latamdatagenerator.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "generation_executions")
@Getter
@Setter
@NoArgsConstructor
public class GenerationExecutionEntity {

    @Id
    private String id;
    private Integer recordsGenerated;
    private String fileName;
    private String filePath;
    private LocalDateTime createAt;

    @OneToMany(mappedBy =  "execution", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GeneratedRecordEntity> records  = new ArrayList<>();

    public GenerationExecutionEntity(String id, Integer recordsGenerated, String fileName, String filePath) {
        this.id = id;
        this.recordsGenerated = recordsGenerated;
        this.fileName = fileName;
        this.filePath = filePath;
        this.createAt = LocalDateTime.now();
    }

    public void addRecord(GeneratedRecordEntity record){
        records.add(record);
        record.setExecution(this);
    }
    
}
