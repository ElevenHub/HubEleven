package com.hubEleven.gateWay.exception;

import com.commonLib.common.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GateWayException implements ErrorCode {
	INVALID_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "인가되지 않은 헤더입니다."),
	INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "사용할 수 없는 JWT 입니다."),
	JWT_PROCESSING_ERROR(HttpStatus.UNAUTHORIZED, "JWT 처리중 에러가 발생했습니다.");

	private final HttpStatus code;
	private final String description;

	@Override
	public HttpStatus getHttpStatus() {
		return code;
	}

	@Override
	public String getMessage() {
		return description;
	}
}
