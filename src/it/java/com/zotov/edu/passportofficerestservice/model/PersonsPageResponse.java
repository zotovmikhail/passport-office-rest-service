package com.zotov.edu.passportofficerestservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonsPageResponse {
	private int number;

	private int size;

	private int totalPages;

	private List<Person> content;

	private int totalElements;
}