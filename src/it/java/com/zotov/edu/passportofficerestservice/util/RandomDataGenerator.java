package com.zotov.edu.passportofficerestservice.util;

import com.neovisionaries.i18n.CountryCode;
import com.zotov.edu.passportofficerestservice.model.PageResponse;
import com.zotov.edu.passportofficerestservice.model.PassportSpecification;
import com.zotov.edu.passportofficerestservice.model.PersonSpecification;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import net.bytebuddy.utility.RandomString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDataGenerator {

    public static PersonSpecification generatePerson() {
        return PersonSpecification.builder()
                .name(generateRandomString())
                .birthday(generateRandomDate())
                .country(generateCountry())
                .build();
    }

    public static List<PersonSpecification> generatePersons(int numberOfPersons) {
        List<PersonSpecification> generatedPersonSpecifications = new ArrayList<>();
        for (int i = 0; i < numberOfPersons; i++) {
            generatedPersonSpecifications.add(generatePerson());
        }
        return generatedPersonSpecifications;
    }

    public static List<PersonSpecification> generatePersonsWithIds(int numberOfPersons) {
        List<PersonSpecification> generatedPersonSpecifications = new ArrayList<>();
        for (int i = 0; i < numberOfPersons; i++) {
            generatedPersonSpecifications.add(generatePerson().withId(UUID.randomUUID().toString()));
        }
        return generatedPersonSpecifications;
    }

    public static PageResponse<PersonSpecification> generatePersonsPage(List<PersonSpecification> personSpecifications, int size, int number) {
        return PageResponse.<PersonSpecification>builder()
                .content(personSpecifications)
                .size(size)
                .number(number)
                .build();
    }

    public static PassportSpecification generatePassport() {
        return PassportSpecification.builder()
                .number(generateRandomString())
                .givenDate(generateRandomDate())
                .departmentCode(generateRandomString())
                .state(PassportState.ACTIVE)
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
