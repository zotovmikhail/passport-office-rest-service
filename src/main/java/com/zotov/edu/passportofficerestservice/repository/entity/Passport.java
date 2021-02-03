package com.zotov.edu.passportofficerestservice.repository.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@NonNull
@EqualsAndHashCode
public final class Passport {
    private final String number;

    private final LocalDate givenDate;

    private final String departmentCode;

    private final PassportState state;

    private final String ownerId;

    public boolean isActive() {
        return this.state == PassportState.ACTIVE;
    }
}
