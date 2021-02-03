package com.zotov.edu.passportofficerestservice.repository.converter;

import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PassportEntityConverter {

    public Passport makePassportLost(Passport passport) {
        return new Passport(
                passport.getNumber(),
                passport.getGivenDate(),
                passport.getDepartmentCode(),
                PassportState.LOST,
                passport.getOwnerId());
    }

    public Passport updateEntityFromDto(LocalDate givenDate, String departmentCode, Passport passport) {
        return new Passport(
                passport.getNumber(),
                givenDate,
                departmentCode,
                passport.getState(),
                passport.getOwnerId()
        );
    }

    public Passport convertToEntity(String id, String number, LocalDate givenDate, String departmentCode) {
        return new Passport(
                number,
                givenDate,
                departmentCode,
                PassportState.ACTIVE,
                id);
    }
}
