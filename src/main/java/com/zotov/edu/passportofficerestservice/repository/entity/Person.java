package com.zotov.edu.passportofficerestservice.repository.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public final class Person {

    @NonNull
    private final String id;

    @NonNull
    private final String name;

    @NonNull
    private final LocalDate birthday;

    @NonNull
    private final String country;

}
