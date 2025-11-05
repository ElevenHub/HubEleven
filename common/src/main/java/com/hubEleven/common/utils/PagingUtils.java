package com.hubEleven.common.utils;

import com.hubEleven.common.response.CommonPageResponse;

import org.springframework.data.domain.Page;

import java.util.function.Function;

public class PagingUtils {

    public static <E, D> CommonPageResponse<D> convert(Page<E> page, Function<E, D> mapper) {
        return CommonPageResponse.of(page.map(mapper));
    }
}
