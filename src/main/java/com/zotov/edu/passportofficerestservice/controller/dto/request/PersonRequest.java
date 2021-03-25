package com.zotov.edu.passportofficerestservice.controller.dto.request;

import com.zotov.edu.passportofficerestservice.controller.validator.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public final class PersonRequest {

    @NotEmpty
    private final String name;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate birthday;

    @Country
    @NotNull
    private final String country;
}
