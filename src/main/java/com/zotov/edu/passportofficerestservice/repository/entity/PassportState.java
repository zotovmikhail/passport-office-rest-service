package com.zotov.edu.passportofficerestservice.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum PassportState {
    ACTIVE("ACTIVE"),
    LOST("LOST");

    private final String databaseName;

    public static PassportState fromDatabaseName(String databaseName) {
        return Arrays
                .stream(PassportState.values())
                .filter(value -> value.getDatabaseName().equals(databaseName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Passport State with the database name '%s' is not defined", databaseName)));
    }
}
