package com.zotov.edu.passportofficerestservice.service.exception;

public class PassportIsAlreadyLostException extends RuntimeException {
    private static final long serialVersionUID = 1552392264549984559L;

    public PassportIsAlreadyLostException(String message) {
        super(String.format("Passport with number '%s' is already lost.", message));
    }
}
