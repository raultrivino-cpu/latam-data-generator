package com.rtrivino.latamdatagenerator.strategy;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class CompanyDocumentGenerationStrategy implements DocumentGenerationStrategy{

    public static final int MIN_VALUE = 10_000_000;
    public static final int MAX_VALUE = 99_999_999;

    @Override
    public String generate() {
        int randomNumber = ThreadLocalRandom.current().nextInt(MIN_VALUE, MAX_VALUE);

        return "9" + randomNumber;
    }
}
