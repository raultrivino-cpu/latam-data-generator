package com.rtrivino.latamdatagenerator.strategy;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class AdultDocumentGenerationStrategy implements DocumentGenerationStrategy {
 public static final long MIN_VALUE = 100_000_000L;
    public static final long MAX_VALUE = 99_999_999_999L;

    @Override
    public String generate() {
        return String.valueOf(ThreadLocalRandom.current().nextLong(MIN_VALUE, MAX_VALUE));
    }
}
