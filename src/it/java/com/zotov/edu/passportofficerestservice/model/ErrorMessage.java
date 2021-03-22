package com.zotov.edu.passportofficerestservice.model;

import lombok.Data;

import java.util.List;

@Data
public class ErrorMessage {
    String timestamp;

    List<String> messages;
}
