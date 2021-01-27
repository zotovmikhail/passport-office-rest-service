package com.zotov.edu.passportofficerestservice.converter;

import com.zotov.edu.passportofficerestservice.model.entity.Passport;
import com.zotov.edu.passportofficerestservice.model.entity.PassportState;
import com.zotov.edu.passportofficerestservice.model.request.PassportPutRequest;
import com.zotov.edu.passportofficerestservice.model.request.PassportRequest;
import com.zotov.edu.passportofficerestservice.model.response.PassportResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PassportDtoConverter implements PassportConverter {

    @Override
    public PassportResponse convertToDto(Passport passport) {
        return new PassportResponse(
                passport.getNumber(),
                passport.getGivenDate(),
                passport.getDepartmentCode());
    }

    @Override
    public List<PassportResponse> convertToDto(List<Passport> passport) {
        return passport.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Passport makePassportLost(Passport passport) {
        return new Passport(
                passport.getNumber(),
                passport.getGivenDate(),
                passport.getDepartmentCode(),
                PassportState.LOST,
                passport.getOwnerId());
    }

    @Override
    public Passport updateEntityFromDto(PassportPutRequest passportRequest, Passport passport) {
        return new Passport(
                passport.getNumber(),
                passportRequest.getGivenDate(),
                passportRequest.getDepartmentCode(),
                passport.getState(),
                passport.getOwnerId()
        );
    }

    @Override
    public Passport covertToEntity(String id, PassportRequest passportRequest) {
        return new Passport(
                passportRequest.getNumber(),
                passportRequest.getGivenDate(),
                passportRequest.getDepartmentCode(),
                PassportState.ACTIVE,
                id);
    }
}
