package com.rtrivino.latamdatagenerator.entity;

import com.rtrivino.latamdatagenerator.enums.RecordType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "generated_records")
@Getter
@Setter
@NoArgsConstructor
public class GeneratedRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastName;
    private Integer age;
    private String identificationNumber;
    private String city;
    private String country;
    private String language;
    
    @Enumerated(EnumType.STRING)
    private RecordType recordType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="execution_id")
    private GenerationExecutionEntity execution;

    public GeneratedRecordEntity(String name, String lastName, Integer age, String identificationNumber,
            String city, String country, String language, RecordType recordType) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.identificationNumber = identificationNumber;
        this.city = city;
        this.country = country;
        this.language = language;
        this.recordType = recordType;
    }
    
}
