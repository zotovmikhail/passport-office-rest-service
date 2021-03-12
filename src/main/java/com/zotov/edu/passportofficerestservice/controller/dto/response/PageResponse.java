package com.zotov.edu.passportofficerestservice.controller.dto.response;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> {

    @NonNull
    private final int number;

    @NonNull
    private final int size;

    @NonNull
    private final int totalPages;

    @NonNull
    private final List<T> content;

    @NonNull
    private final long totalElements;

    public PageResponse(Page<T> page) {
        this.number = page.getNumber();
        this.size = page.getSize();
        this.totalPages = page.getTotalPages();
        this.content = List.copyOf(page.getContent());
        this.totalElements = page.getTotalElements();
    }

}
