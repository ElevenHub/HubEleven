package com.hubEleven.order.domain.exception;

import com.hubEleven.common.code.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode implements StatusCode {
	// Company
	REQUESTOR_COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "업체를 찾을 수 없습니다."),
	RECIPIENT_COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "업체를 찾을 수 없습니다."),

	// Product
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),

	// Stock
	STOCK_RESTORE_FAILED(HttpStatus.BAD_REQUEST, "재고 복구에 실패했습니다."),
	STOCK_INSUFFICIENT(HttpStatus.BAD_REQUEST, "재고가 부족합니다."),

	// Order
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public String getName() {
		return name();
	}
}
