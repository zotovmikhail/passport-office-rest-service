package com.zotov.edu.passportofficerestservice.repository.entity;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public final class Passport {
    @NonNull
    private final String number;

    @NonNull
    private final LocalDate givenDate;

    @NonNull
    private final String departmentCode;

    @NonNull
    @With
    private final PassportState state;

    @NonNull
    private final String ownerId;

    public boolean isActive() {
        return this.state == PassportState.ACTIVE;
    }
}
