package com.rtrivino.latamdatagenerator.domain;

import com.rtrivino.latamdatagenerator.enums.RecordType;

public class Company extends GeneratedDataRecord{

    public static final String EMPTY_LAST_NAME = "";

    public Company(String name, Integer age, String identificationNumber, String city,
            String country, String language) {
        super(name, EMPTY_LAST_NAME , age, identificationNumber, city, country, language);
    }

    @Override
    public RecordType getRecordType() {
        return RecordType.COMPANY;
    }

}
