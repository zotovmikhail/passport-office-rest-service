package com.zotov.edu.passportofficerestservice.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@NonNull
@Getter
public final class PassportRequest {

    @NotNull
    private final String number;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate givenDate;

    @NotNull
    private final String departmentCode;
}
