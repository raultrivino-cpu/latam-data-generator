package com.rtrivino.latamdatagenerator.domain;

import com.rtrivino.latamdatagenerator.enums.RecordType;

public class NaturalPerson extends GeneratedDataRecord {

    private static final int ADULT_AGE = 18;

    public NaturalPerson(String name, String lastName, Integer age, String identificationNumber, String city,
            String country, String language) {
        super(name, lastName, age, identificationNumber, city, country, language);
    }

    @Override
    public RecordType getRecordType() {
        return RecordType.NATURAL_PERSON;
    }

    public boolean isMinor() {
        return getAge() < ADULT_AGE;
    }

}
