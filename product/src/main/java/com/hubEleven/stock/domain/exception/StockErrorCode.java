package com.hubEleven.stock.domain.exception;

import com.hubEleven.common.code.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StockErrorCode implements StatusCode {
	STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "재고를 찾을 수 없습니다."),
	INVALID_STOCK_QUANTITY(HttpStatus.BAD_REQUEST, "재고 수량이 유효하지 않습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public String getName() {
		return name();
	}
}
