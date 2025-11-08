package com.hubEleven.common.code;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode implements StatusCode {
	SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버 에러가 발생하였습니다."),
	VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "요청 값이 유효하지 않습니다."),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public String getName() {
		return this.name();
	}
}
