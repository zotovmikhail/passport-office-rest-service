package com.zotov.edu.passportofficerestservice.util;

import com.zotov.edu.passportofficerestservice.model.*;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;

import java.time.LocalDate;

import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generateRandomDate;
import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.generateRandomDepartmentCode;

public class DataConverter {

    public static PersonResponse convertToPersonResponse(Person person) {
        return PersonResponse.builder()
                .id(person.getId())
                .name(person.getName())
                .birthday(person.getBirthday().toString())
                .country(person.getCountry())
                .build();
    }

    public static PersonPutRequest convertToPersonPutRequest(Person person) {
        return PersonPutRequest.builder()
                .id(person.getId())
                .name(person.getName())
                .birthday(person.getBirthday().toString())
                .country(person.getCountry())
                .build();
    }

    public static Passport convertToPassportEntity(PassportResponse passportResponse, PassportState passportState, String ownerId) {
        return new Passport(passportResponse.getNumber(),
                LocalDate.parse(passportResponse.getGivenDate()),
                passportResponse.getDepartmentCode(),
                passportState,
                ownerId);
    }


    public static PassportRequest convertToPassportRequest(Passport passport) {
        return PassportRequest.builder()
                .number(passport.getNumber())
                .givenDate(passport.getGivenDate().toString())
                .departmentCode(passport.getDepartmentCode())
                .build();
    }

    public static PassportPutRequest generatePassportPutRequest() {
        return PassportPutRequest.builder()
                .givenDate(generateRandomDate())
                .departmentCode(generateRandomDepartmentCode())
                .build();
    }

    public static PassportResponse convertToPassportResponse(Passport passport) {
        return PassportResponse.builder()
                .number(passport.getNumber())
                .givenDate(passport.getGivenDate().toString())
                .departmentCode(passport.getDepartmentCode())
                .build();
    }

}
