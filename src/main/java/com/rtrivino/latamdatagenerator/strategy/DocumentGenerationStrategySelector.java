package com.rtrivino.latamdatagenerator.strategy;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DocumentGenerationStrategySelector {
    private static final int ADULT_AGE = 18;

    private final  CompanyDocumentGenerationStrategy companyDocumentGenerationStrategy;
    private final MinorDocumentGenerationStrategy minorDocumentGenerationStrategy;
    private final AdultDocumentGenerationStrategy adultDocumentGenerationStrategy;
    
    public DocumentGenerationStrategy select (boolean isCompany, Integer age){
        if (isCompany){
            return companyDocumentGenerationStrategy;
        }

        if (age < ADULT_AGE){
            return minorDocumentGenerationStrategy;
        }else{
            return adultDocumentGenerationStrategy;
        }
    }

    
}
