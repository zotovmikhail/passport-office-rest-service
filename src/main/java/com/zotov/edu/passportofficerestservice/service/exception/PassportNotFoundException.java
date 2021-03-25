package com.zotov.edu.passportofficerestservice.service.exception;

public class PassportNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = -7990417611502420722L;

    public PassportNotFoundException(String passportId) {
        super(String.format("Passport with id '%s' is not found", passportId));
    }
}
