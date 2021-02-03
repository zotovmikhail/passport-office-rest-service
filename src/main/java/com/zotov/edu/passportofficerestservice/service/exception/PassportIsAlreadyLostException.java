package com.zotov.edu.passportofficerestservice.service.exception;

public class PassportIsAlreadyLostException extends RuntimeException {
    public PassportIsAlreadyLostException(String message) {
        super(String.format("Passport with number '%s' is already lost.", message));
    }
}
