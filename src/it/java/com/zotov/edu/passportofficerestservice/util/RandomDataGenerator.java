package com.zotov.edu.passportofficerestservice.util;

import com.neovisionaries.i18n.CountryCode;
import com.zotov.edu.passportofficerestservice.model.Passport;
import com.zotov.edu.passportofficerestservice.model.Person;
import net.bytebuddy.utility.RandomString;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDataGenerator {

    public static Person generatePerson() {
        return Person.builder()
                .name(generateRandomString())
                .birthday(generateRandomDate())
                .country(generateCountry())
                .build();
    }

    public static List<Person> generatePersons(int numberOfPersons) {
        List<Person> generatedPersons = new ArrayList<>();
        for (int i = 0; i < numberOfPersons; i++) {
            generatedPersons.add(generatePerson());
        }
        return generatedPersons;
    }

    public static Set<Passport> generatePassports(int numberOfPassports) {
        Set<Passport> generatedPassports = new HashSet<>();
        for (int i = 0; i < numberOfPassports; i++) {
            generatedPassports.add(generatePassport());
        }
        return generatedPassports;
    }

    public static Passport generatePassport() {
        return Passport.builder()
                .number(generateRandomString())
                .givenDate(generateRandomDate())
                .departmentCode(generateRandomString())
                .build();
    }

    public static String generateRandomDate() {
        long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
        long maxDay = LocalDate.now().toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        return randomDate.toString();
    }

    public static String generateRandomString() {
        return new RandomString().nextString();
    }

    public static String generateCountry() {
        int randomCountryIndex = new Random().nextInt(CountryCode.values().length);
        return CountryCode.values()[randomCountryIndex].toString();
    }
}
