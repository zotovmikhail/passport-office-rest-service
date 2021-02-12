package com.zotov.edu.passportofficerestservice.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public final class PassportPutRequest {

    @NotNull
    @NonNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate givenDate;

    @NotNull
    @NonNull
    private final String departmentCode;
}
