package com.zotov.edu.passportofficerestservice.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
public class PersonResponse {
    @With
    private String id;

    @With
    private String name;

    @With
    private String birthday;

    @With
    private String country;
}
