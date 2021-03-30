package com.zotov.edu.passportofficerestservice.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PassportState {
    ACTIVE("ACTIVE"),
    LOST("LOST");

    private final String name;
}
