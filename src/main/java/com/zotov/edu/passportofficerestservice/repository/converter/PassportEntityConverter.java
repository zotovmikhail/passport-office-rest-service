package com.zotov.edu.passportofficerestservice.repository.converter;

import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PassportEntityConverter {

    public Passport updateEntityFromDto(LocalDate givenDate, String departmentCode, Passport passport) {
        return new Passport(
                passport.getNumber(),
                givenDate,
                departmentCode,
                passport.getState(),
                passport.getOwnerId()
        );
    }

    public Passport convertToEntity(String passportNumber, LocalDate givenDate, String departmentCode, String ownerId) {
        return new Passport(
                passportNumber,
                givenDate,
                departmentCode,
                PassportState.ACTIVE,
                ownerId);
    }
}
