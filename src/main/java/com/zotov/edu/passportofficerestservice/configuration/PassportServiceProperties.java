package com.zotov.edu.passportofficerestservice.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "application")
public class PassportServiceProperties {

    /**
     * Type of the repository implementation
     */
    private RepositoryType repository;

    public enum RepositoryType {
        MEMORY,
        JDBC
    }
}
