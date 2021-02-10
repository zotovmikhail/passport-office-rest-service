package com.zotov.edu.passportofficerestservice.controller.dto.request;

import com.zotov.edu.passportofficerestservice.controller.validator.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@NonNull
@Getter
public final class PersonRequest {

    @NotNull
    @NotEmpty
    private final String name;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate birthday;

    @NotNull
    @Country
    private final String country;
}
