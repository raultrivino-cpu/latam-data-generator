package com.rtrivino.latamdatagenerator.factory;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.rtrivino.latamdatagenerator.domain.Company;
import com.rtrivino.latamdatagenerator.domain.GeneratedDataRecord;
import com.rtrivino.latamdatagenerator.domain.NaturalPerson;
import com.rtrivino.latamdatagenerator.enums.Country;
import com.rtrivino.latamdatagenerator.strategy.DocumentGenerationStrategy;
import com.rtrivino.latamdatagenerator.strategy.DocumentGenerationStrategySelector;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;

@Component
@RequiredArgsConstructor
public class GeneratedDataRecordFactory {
    private static final int MIN_AGE = 11;
    private static final int MAX_AGE = 79;
    private static final int COMPANY_MIN_AGE = 1;
    private static final int COMPANY_MAX_AGE = 50;

    private final DocumentGenerationStrategySelector documentGenerationStrategySelector;

    private final Faker faker = new Faker(new Locale("es"));

    private int radomAge() {
        return ThreadLocalRandom.current().nextInt(MIN_AGE, MAX_AGE);
    }

    private int radomCompanyAge() {
        return ThreadLocalRandom.current().nextInt(COMPANY_MIN_AGE, COMPANY_MAX_AGE);
    }

    private NaturalPerson createNaturalPerson() {
        int age = radomAge();
        Country country = Country.random();
        DocumentGenerationStrategy strategy = documentGenerationStrategySelector.select(false, age);
        String document = strategy.generate();

        return new NaturalPerson(faker.name().firstName(), faker.name().lastName(), age, document, country.randomCity(),
                country.getDisplayName(),
                country.getLanguage());
    }

    private Company createCompany() {
        int age = radomCompanyAge();
        Country country = Country.random();
        DocumentGenerationStrategy strategy = documentGenerationStrategySelector.select(true, age);
        String document = strategy.generate();

        return new Company(
                faker.company().name(),
                age,
                document,
                country.randomCity(),
                country.getDisplayName(),
                country.getLanguage());
    }

    public GeneratedDataRecord createRandom() {
        boolean isCompany = ThreadLocalRandom.current().nextBoolean();

        if (isCompany) {
            return createCompany();
        }

        return createNaturalPerson();
    }
}
