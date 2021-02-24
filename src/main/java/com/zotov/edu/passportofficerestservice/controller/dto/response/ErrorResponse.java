package com.zotov.edu.passportofficerestservice.controller.dto.response;

import lombok.Getter;
import lombok.NonNull;

import java.time.Instant;
import java.util.List;

@Getter
public final class ErrorResponse {
    @NonNull
    private final Instant timestamp;

    @NonNull
    private final List<String> messages;

    public ErrorResponse(List<String> messages) {
        this.timestamp = Instant.now();
        this.messages = messages;
    }

}
