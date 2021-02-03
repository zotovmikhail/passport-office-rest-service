package com.zotov.edu.passportofficerestservice.service.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(String.format("Entity with id '%s' is not found", message));
    }
}
