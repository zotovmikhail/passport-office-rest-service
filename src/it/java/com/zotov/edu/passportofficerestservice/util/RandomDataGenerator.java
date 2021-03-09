package com.zotov.edu.passportofficerestservice.util;

import com.github.javafaker.Faker;
import com.zotov.edu.passportofficerestservice.model.PageResponse;
import com.zotov.edu.passportofficerestservice.model.PassportSpecification;
import com.zotov.edu.passportofficerestservice.model.PersonSpecification;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.zotov.edu.passportofficerestservice.util.DataConverter.PASSPORT_SERVICE_SPECIFICATION_DATE_TIME_FORMATTER;

public class RandomDataGenerator {

    public static PersonSpecification generatePerson() {
        return PersonSpecification.builder()
                .name(generateRandomName())
                .birthday(generateRandomDate())
                .country(generateRandomCountry())
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
            generatedPersonSpecifications.add(generatePerson().withId(generateRandomPersonId()));
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
                .number(generateRandomPassportNumber())
                .givenDate(generateRandomDate())
                .departmentCode(generateRandomDepartmentCode())
                .state(PassportState.ACTIVE)
                .build();
    }

    public static String generateRandomDate() {
        Instant randomDate = new Faker().date().past(30000, TimeUnit.DAYS).toInstant();
        return PASSPORT_SERVICE_SPECIFICATION_DATE_TIME_FORMATTER.format(randomDate);
    }

    public static String generateRandomString() {
        return new Faker().idNumber().valid();
    }

    public static String generateRandomDepartmentCode() {
        return new Faker().code().isbnRegistrant();
    }

    public static String generateRandomPassportNumber() {
        return new Faker().idNumber().valid();
    }

    public static String generateRandomName() {
        return new Faker().name().fullName();
    }

    public static String generateRandomCountry() {
        return new Faker().country().countryCode2();
    }

    public static String generateRandomPersonId() {
        return UUID.randomUUID().toString();
    }
}
