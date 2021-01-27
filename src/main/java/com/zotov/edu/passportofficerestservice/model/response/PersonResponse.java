package com.zotov.edu.passportofficerestservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@NotNull
public final class PersonResponse {

    private final String id;

    private final String name;

    private final LocalDate birthday;

    private final String country;
}
