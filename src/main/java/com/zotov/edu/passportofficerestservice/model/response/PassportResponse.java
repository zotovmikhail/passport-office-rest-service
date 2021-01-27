package com.zotov.edu.passportofficerestservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@AllArgsConstructor
@NonNull
@Getter
public class PassportResponse {

    private final String number;

    private final LocalDate givenDate;

    private final String departmentCode;
}
