package com.zotov.edu.passportofficerestservice.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class PassportResponse {

    @NonNull
    private final String number;

    @NonNull
    private final LocalDate givenDate;

    @NonNull
    private final String departmentCode;
}
