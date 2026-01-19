package com.hubEleven.delivery.domain;

import com.commonLib.common.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DeliveryErrorCode implements ErrorCode {
	DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "유효하지 않은 배달 ID입니다."),
	DELIVERY_ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "유효하지 않은 배달 경로입니다."),
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다."),
	FEIGN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Feign error 입니다."),
	DELIVERY_MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "배달 매니저 정보를 찾을 수 없습니다."),
	COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "업체 정보를 찾을 수 없습니다."),
	HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "업체 정보를 찾을 수 없습니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 정보를 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
