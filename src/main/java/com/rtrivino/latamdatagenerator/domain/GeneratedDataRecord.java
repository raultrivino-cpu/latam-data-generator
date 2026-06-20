package com.rtrivino.latamdatagenerator.domain;

import com.rtrivino.latamdatagenerator.enums.RecordType;

import lombok.Getter;

@Getter
public abstract class GeneratedDataRecord {
    private final String name;
    private final String lastName;
    private final Integer age;
    private final String identificationNumber;
    private final String city;
    private final String country;
    private final String language;

    protected GeneratedDataRecord(String name, String lastName, Integer age, String identificationNumber, String city,
            String country, String language) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.identificationNumber = identificationNumber;
        this.city = city;
        this.country = country;
        this.language = language;
    }

    public abstract RecordType getRecordType();
    
    public String getFullName(){
        if (lastName == null || lastName.isBlank()){
            return name;
        }

        return name + " " + lastName;
    }
}
