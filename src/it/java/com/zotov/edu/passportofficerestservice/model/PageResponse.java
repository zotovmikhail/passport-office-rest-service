package com.zotov.edu.passportofficerestservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties
public class PageResponse<T> {
	private int number;

	private int size;

	private int totalPages;

	private List<T> content;

	private int totalElements;
}