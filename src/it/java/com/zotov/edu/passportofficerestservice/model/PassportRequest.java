package com.zotov.edu.passportofficerestservice.model;

import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
public class PassportRequest {
    @With
    private String number;

    @With
    private String givenDate;

    @With
    private String departmentCode;
}
