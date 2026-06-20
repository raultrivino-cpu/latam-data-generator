package com.rtrivino.latamdatagenerator.strategy;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class MinorDocumentGenerationStrategy implements DocumentGenerationStrategy{
    public static final int MIN_VALUE = 10_000_000;
    public static final int MAX_VALUE = 99_999_999;

    @Override
    public String generate() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(MIN_VALUE, MAX_VALUE));
    }
}
