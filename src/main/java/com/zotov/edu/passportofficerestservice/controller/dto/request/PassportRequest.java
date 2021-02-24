package com.zotov.edu.passportofficerestservice.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public final class PassportRequest {

    @NotEmpty
    private final String number;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate givenDate;

    @NotEmpty
    private final String departmentCode;
}
