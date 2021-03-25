package com.zotov.edu.passportofficerestservice.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public final class PersonResponse {

    @NonNull
    private final String id;

    @NonNull
    private final String name;

    @NonNull
    private final LocalDate birthday;

    @NonNull
    private final String country;
}
