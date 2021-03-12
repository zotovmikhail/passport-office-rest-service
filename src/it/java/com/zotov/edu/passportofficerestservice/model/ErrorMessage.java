package com.zotov.edu.passportofficerestservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties
public class ErrorMessage {
    String timestamp;

    List<String> messages;
}
