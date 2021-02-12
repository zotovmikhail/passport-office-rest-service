package com.zotov.edu.passportofficerestservice.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public final class PersonResponse {

    @NotNull
    private final String id;

    @NotNull
    private final String name;

    @NotNull
    private final LocalDate birthday;

    @NotNull
    private final String country;
}
