package com.zotov.edu.passportofficerestservice.service.exception;

public class PersonNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -6251884725067856181L;

    public PersonNotFoundException(String personId) {
        super(String.format("Person with id '%s' is not found", personId));
    }
}
