package com.zotov.edu.passportofficerestservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.List;

@Data
@Builder
@With
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageResponse<T> {
	private int number;

	private int size;

	private int totalPages;

	private List<T> content;

	private int totalElements;
}