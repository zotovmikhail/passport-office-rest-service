package com.zotov.edu.passportofficerestservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@AllArgsConstructor
@NonNull
@Getter
@EqualsAndHashCode
public final class Person {

    private final String id;

    private final String name;

    private final LocalDate birthday;

    private final String country;

}
