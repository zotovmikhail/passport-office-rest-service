package com.zotov.edu.passportofficerestservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Passport {
    @With
    private String number;

    @With
    private String givenDate;

    @With
    private String departmentCode;
}
