package com.hubEleven.delivery.domain;

import com.hubEleven.common.code.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DeliveryErrorCode implements StatusCode {
    DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "유효하지 않은 배달 ID입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return name();
    }






}
