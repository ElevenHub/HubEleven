package com.hubEleven.product.domain.exception;

import com.commonLib.common.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode implements ErrorCode {
	PRODUCT_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 상품입니다."),
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
	COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "업체를 찾을 수 없습니다."),
	HUB_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 허브입니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
