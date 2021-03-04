package com.zotov.edu.passportofficerestservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zotov.edu.passportofficerestservice.repository.entity.PassportState;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PassportSpecification {
    @With
    private String number;

    @With
    private String givenDate;

    @With
    private String departmentCode;

    @With
    @EqualsAndHashCode.Exclude
    private String ownerId;

    @With
    @EqualsAndHashCode.Exclude
    private PassportState state;
}
