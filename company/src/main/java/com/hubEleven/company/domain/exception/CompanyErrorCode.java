package com.hubEleven.company.domain.exception;

import com.hubEleven.common.code.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CompanyErrorCode implements StatusCode {
	COMPANY_DUPLICATED(HttpStatus.CONFLICT, "같은 허브에 동일한 업체명이 이미 존재합니다."),
	COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "업체를 찾을 수 없습니다."),
	HUB_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 허브입니다.");

	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public String getName() {
		return name();
	}
}
