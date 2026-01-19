package com.hubEleven.deliveryManager.domain.exception;

import com.commonLib.common.code.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum DeliveryManagerErrorCode implements ErrorCode {
	DELIVERY_MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "배송 담당자를 찾을 수 없습니다."),
	HUB_DELIVERY_MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "허브 배송 담당자가 존재하지 않습니다."),
	COMPANY_DELIVERY_MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "업체 배송 담당자가 존재하지 않습니다."),
	DUPLICATE_DELIVERY_MANAGER(HttpStatus.CONFLICT, "이미 존재하는 배송 담당자입니다."),
	INVALID_DELIVERY_TYPE(HttpStatus.BAD_REQUEST, "잘못된 담당 유형입니다."),
	ASSIGNMENT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "배송담당자 배정에 실패하였습니다."),
	NO_AVAILABLE_MANAGER(HttpStatus.NOT_FOUND, "배정 가능한 배송 담당자가 존재하지 않습니다."),
	INVALID_ASSIGNMENT_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청형식입니다."),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다."),

	HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "허브가 존재하지 않습니다."),

	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문이 존재하지 않습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	DeliveryManagerErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
