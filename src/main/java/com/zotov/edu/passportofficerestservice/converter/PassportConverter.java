package com.zotov.edu.passportofficerestservice.converter;

import com.zotov.edu.passportofficerestservice.model.entity.Passport;
import com.zotov.edu.passportofficerestservice.model.request.PassportPutRequest;
import com.zotov.edu.passportofficerestservice.model.request.PassportRequest;
import com.zotov.edu.passportofficerestservice.model.response.PassportResponse;

import java.util.List;

public interface PassportConverter {
    PassportResponse convertToDto(Passport passport);

    Passport covertToEntity(String id, PassportRequest passportRequest);

    List<PassportResponse> convertToDto(List<Passport> passport);

    Passport makePassportLost(Passport passport);

    Passport updateEntityFromDto(PassportPutRequest passportRequest, Passport passport);
}
