package com.zotov.edu.passportofficerestservice.controller.exception;

import com.zotov.edu.passportofficerestservice.controller.dto.response.ErrorResponse;
import com.zotov.edu.passportofficerestservice.repository.exception.PassportAlreadyExistsException;
import com.zotov.edu.passportofficerestservice.service.exception.PassportIsAlreadyLostException;
import com.zotov.edu.passportofficerestservice.service.exception.PassportNotFoundException;
import com.zotov.edu.passportofficerestservice.service.exception.PersonNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class PersonExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<String> messages = exception.getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        return new ErrorResponse(messages);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return new ErrorResponse(Collections.singletonList(exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PersonNotFoundException.class)
    public ErrorResponse handlePersonNotFoundException(PersonNotFoundException exception) {
        return new ErrorResponse(Collections.singletonList(exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PassportNotFoundException.class)
    public ErrorResponse handlePassportNotFoundException(PassportNotFoundException exception) {
        return new ErrorResponse(Collections.singletonList(exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(PassportIsAlreadyLostException.class)
    public ErrorResponse handlePassportIsAlreadyLostException(PassportIsAlreadyLostException exception) {
        return new ErrorResponse(Collections.singletonList(exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(PassportAlreadyExistsException.class)
    public ErrorResponse handlePassportAlreadyExistsException(PassportAlreadyExistsException exception) {
        return new ErrorResponse(Collections.singletonList(exception.getMessage()));
    }
}
