package com.zotov.edu.passportofficerestservice.util;

import com.zotov.edu.passportofficerestservice.model.PassportSpecification;
import com.zotov.edu.passportofficerestservice.model.PersonSpecification;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DataConverter {

    final static DateTimeFormatter PASSPORT_SERVICE_SPECIFICATION_DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd")
            .withZone(ZoneId.systemDefault());

    public static List<Person> convertToPersonEntities(List<PersonSpecification> personSpecifications) {
        return personSpecifications.stream().map(DataConverter::convertToPersonEntity).collect(Collectors.toList());
    }

    public static Person convertToPersonEntity(PersonSpecification personSpecification) {
        return new Person(personSpecification.getId(),
                personSpecification.getName(),
                LocalDate.parse(personSpecification.getBirthday(), PASSPORT_SERVICE_SPECIFICATION_DATE_TIME_FORMATTER),
                personSpecification.getCountry());
    }

    public static List<Passport> convertToPassportEntities(Set<PassportSpecification> passportSpecifications) {
        return passportSpecifications.stream().map(DataConverter::convertToPassportEntity).collect(Collectors.toList());
    }

    public static Passport convertToPassportEntity(PassportSpecification passportSpecification) {
        return new Passport(passportSpecification.getNumber(),
                LocalDate.parse(passportSpecification.getGivenDate()),
                passportSpecification.getDepartmentCode(),
                passportSpecification.getState(),
                passportSpecification.getOwnerId());
    }

    public static PassportSpecification convertToPassportSpec(Passport passport) {
        return PassportSpecification.builder()
                .number(passport.getNumber())
                .givenDate(passport.getGivenDate().toString())
                .departmentCode(passport.getDepartmentCode())
                .state(passport.getState())
                .ownerId(passport.getOwnerId())
                .build();
    }

    public static PersonSpecification convertToPersonSpec(Person person) {
        return PersonSpecification.builder()
                .id(person.getId())
                .name(person.getName())
                .birthday(person.getBirthday().toString())
                .country(person.getCountry())
                .build();
    }
}
