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
@Getter
public final class PersonRequest {

    @NotEmpty
    @NonNull
    private final String name;

    @NotNull
    @NonNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate birthday;

    @Country
    @NonNull
    private final String country;
}
