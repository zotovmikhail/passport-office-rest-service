package com.zotov.edu.passportofficerestservice.model.request;

import com.zotov.edu.passportofficerestservice.validator.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@AllArgsConstructor
@NonNull
@Getter
public final class PersonRequest {

    @NotNull
    private final String name;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate birthday;

    @NotNull
    @Pattern(regexp = "[a-zA-Z]{2}")
    @Country
    private final String country;
}
