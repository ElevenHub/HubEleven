package com.hubEleven.user.domain.exception;

import com.commonLib.common.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
	INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 8~15자이며, 대소문자/숫자/특수문자를 포함해야 합니다."),
	INVALID_USERNAME(HttpStatus.BAD_REQUEST, "아이디는 4~10자이며, 소문자/숫자로 구성되어야 합니다."),
	DUPLICATED_USERNAME(HttpStatus.BAD_REQUEST, "이미 사용하고 있는 아이디입니다."),
	FAILED_LOGIN(HttpStatus.BAD_REQUEST, "로그인에 실패했습니다."),
	NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),
	NOT_APPROVED_USER(HttpStatus.FORBIDDEN, "승인되지 않은 사용자입니다."),
	UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "본인의 정보만 조회할 수 있습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
