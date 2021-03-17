package com.zotov.edu.passportofficerestservice.util;

import com.zotov.edu.passportofficerestservice.model.PassportPutRequest;
import com.zotov.edu.passportofficerestservice.model.PassportRequest;
import com.zotov.edu.passportofficerestservice.model.PassportResponse;
import com.zotov.edu.passportofficerestservice.model.PersonResponse;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

import static com.zotov.edu.passportofficerestservice.util.RandomDataGenerator.*;

@UtilityClass
public class DataConverter {

    public PersonResponse convertToPersonResponse(Person person) {
        return PersonResponse.builder()
                .id(person.getId())
                .name(person.getName())
                .birthday(person.getBirthday().toString())
                .country(person.getCountry())
                .build();
    }

    public Passport convertToPassportEntity(PassportResponse passportResponse, PassportState passportState, String ownerId) {
        return new Passport(passportResponse.getNumber(),
                LocalDate.parse(passportResponse.getGivenDate()),
                passportResponse.getDepartmentCode(),
                passportState,
                ownerId);
    }


    public PassportRequest convertToPassportRequest(Passport passport) {
        return PassportRequest.builder()
                .number(passport.getNumber())
                .givenDate(passport.getGivenDate().toString())
                .departmentCode(passport.getDepartmentCode())
                .build();
    }

    public PassportPutRequest generatePassportPutRequest() {
        return PassportPutRequest.builder()
                .givenDate(generateRandomDate())
                .departmentCode(generateRandomDepartmentCode())
                .build();
    }

    public PassportResponse convertToPassportResponse(Passport passport) {
        return PassportResponse.builder()
                .number(passport.getNumber())
                .givenDate(passport.getGivenDate().toString())
                .departmentCode(passport.getDepartmentCode())
                .build();
    }

}
