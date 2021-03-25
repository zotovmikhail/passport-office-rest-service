package com.zotov.edu.passportofficerestservice.repository.exception;

public class PassportAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = -3822780705640084003L;

    public PassportAlreadyExistsException(String passportId) {
        super(String.format("Passport with id '%s' already exists in the data", passportId));
    }
}
