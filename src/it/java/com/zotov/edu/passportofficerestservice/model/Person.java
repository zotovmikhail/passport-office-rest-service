package com.zotov.edu.passportofficerestservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {
    private String id;

    @With
    private String name;

    @With
    private String birthday;

    @With
    private String country;
}
