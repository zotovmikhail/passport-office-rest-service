package com.zotov.edu.passportofficerestservice.model;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
	private int number;

	private int size;

	private int totalPages;

	private List<T> content;

	private int totalElements;
}