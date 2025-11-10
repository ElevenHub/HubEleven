package com.hubEleven.user.domain.exception;

import com.commonLib.common.code.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode implements StatusCode {
	INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 8~15자이며, 대소문자/숫자/특수문자를 포함해야 합니다."),
	INVALID_USERNAME(HttpStatus.BAD_REQUEST, "아이디는 4~10자이며, 소문자/숫자로 구성되어야 합니다."),
	DUPLICATED_USERNAME(HttpStatus.BAD_REQUEST, "이미 사용하고 있는 아이디입니다."),
	FORBIDDEN_USER(HttpStatus.BAD_REQUEST, "해당 유저를 찾을 수 없습니다."),
	FAILED_LOGIN(HttpStatus.BAD_REQUEST, "로그인에 실패했습니다."),
	NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다.");

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

	@Override
	public String getName() {
		return this.name();
	}
}
