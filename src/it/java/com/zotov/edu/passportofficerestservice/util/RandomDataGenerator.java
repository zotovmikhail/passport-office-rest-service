package com.zotov.edu.passportofficerestservice.util;

import com.github.javafaker.Faker;
import com.zotov.edu.passportofficerestservice.model.PassportRequest;
import com.zotov.edu.passportofficerestservice.model.PassportResponse;
import com.zotov.edu.passportofficerestservice.model.PersonPutRequest;
import com.zotov.edu.passportofficerestservice.model.PersonRequest;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class RandomDataGenerator {

    public PersonRequest generatePersonRequest() {
        return PersonRequest.builder()
                .name(generateRandomName())
                .birthday(generateRandomDate())
                .country(generateRandomCountry())
                .build();
    }

    public PersonPutRequest generatePersonPutRequest() {
        return PersonPutRequest.builder()
                .name(generateRandomName())
                .birthday(generateRandomDate())
                .country(generateRandomCountry())
                .build();
    }

    public Person generatePerson() {
        return new Person(
                generateRandomPersonId(),
                generateRandomName(),
                LocalDate.parse(generateRandomDate(), DateTimeFormatter.ISO_LOCAL_DATE),
                generateRandomCountry()
        );
    }

    public List<Person> generatePersons(int numberOfPersons) {
        List<Person> generatedPersonSpecifications = new ArrayList<>();
        for (int i = 0; i < numberOfPersons; i++) {
            generatedPersonSpecifications.add(generatePerson());
        }
        return generatedPersonSpecifications;
    }

    public PassportRequest generatePassportRequest() {
        return PassportRequest.builder()
                .number(generateRandomPassportNumber())
                .givenDate(generateRandomDate())
                .departmentCode(generateRandomDepartmentCode())
                .build();
    }

    public PassportResponse generatePassportResponse() {
        return PassportResponse.builder()
                .number(generateRandomPassportNumber())
                .givenDate(generateRandomDate())
                .departmentCode(generateRandomDepartmentCode())
                .build();
    }

    public Passport generatePassport(String ownerId) {
        return new Passport(generateRandomPassportNumber(),
                LocalDate.parse(generateRandomDate(), DateTimeFormatter.ISO_LOCAL_DATE),
                generateRandomDepartmentCode(),
                PassportState.ACTIVE,
                ownerId);
    }

    public String generateRandomDate() {
        Instant randomDate = Faker.instance().date().past(30000, TimeUnit.DAYS).toInstant();
        return LocalDateTime.ofInstant(randomDate, ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public String generateRandomString() {
        return Faker.instance().idNumber().valid();
    }

    public String generateRandomDepartmentCode() {
        return Faker.instance().code().isbnRegistrant();
    }

    public String generateRandomPassportNumber() {
        return Faker.instance().idNumber().valid();
    }

    public String generateRandomName() {
        return Faker.instance().name().fullName();
    }

    public String generateRandomCountry() {
        return Faker.instance().country().countryCode2();
    }

    public String generateRandomPersonId() {
        return UUID.randomUUID().toString();
    }
}
