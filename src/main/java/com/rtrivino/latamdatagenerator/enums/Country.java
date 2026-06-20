package com.rtrivino.latamdatagenerator.enums;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public enum Country {

    COLOMBIA("Colombia", "Spanish", List.of("Bogota", "Medellin", "Cali", "Barranquilla", "Cartagena")),
    UNITED_STATES("United States", "English", List.of("Miami", "New York", "Los Angeles", "Houston", "Chicago")),
    BRAZIL("Brazil", "Portuguese", List.of("Sao Paulo", "Rio de Janeiro", "Brasilia", "Salvador", "Fortaleza")),
    FRANCE("France", "French", List.of("Paris", "Lyon", "Marseille", "Toulouse", "Nice")),
    GERMANY("Germany", "German", List.of("Berlin", "Munich", "Hamburg", "Frankfurt", "Cologne")),
    ITALY("Italy", "Italian", List.of("Rome", "Milan", "Naples", "Turin", "Florence"));

    private final String displayName;
    private final String language;
    private final List<String> cities;

    Country(String displayName, String language, List<String> cities) {
        this.displayName = displayName;
        this.language = language;
        this.cities = cities;
    }

    public static Country random() {
        List<Country> countries = Arrays.asList(values());
        int index = ThreadLocalRandom.current().nextInt(countries.size());

        return countries.get(index);
    }

    public String randomCity() {
        int index = ThreadLocalRandom.current().nextInt(cities.size());

        return cities.get(index);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLanguage() {
        return language;
    }
}