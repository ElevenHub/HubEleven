package com.hubEleven.common.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record CommonPageResponse<T> (
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public static <T> CommonPageResponse<T> of(Page<T> page) {
        return new CommonPageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}
