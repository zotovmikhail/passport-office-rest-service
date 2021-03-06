package com.zotov.edu.passportofficerestservice.controller.converter;

import com.zotov.edu.passportofficerestservice.controller.dto.response.PassportResponse;
import com.zotov.edu.passportofficerestservice.controller.dto.response.PersonResponse;
import com.zotov.edu.passportofficerestservice.repository.entity.Passport;
import com.zotov.edu.passportofficerestservice.repository.entity.Person;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PassportControllerDtoConverter {
    public PassportResponse convertPassportToDto(Passport passport) {
        return new PassportResponse(
                passport.getNumber(),
                passport.getGivenDate(),
                passport.getDepartmentCode());
    }

    public List<PassportResponse> convertPassportListToDto(List<Passport> passport) {
        return passport.stream().map(this::convertPassportToDto).collect(Collectors.toList());
    }

    public PersonResponse convertPersonToDto(Person person) {
        return new PersonResponse(
                person.getId(),
                person.getName(),
                person.getBirthday(),
                person.getCountry());
    }
}
