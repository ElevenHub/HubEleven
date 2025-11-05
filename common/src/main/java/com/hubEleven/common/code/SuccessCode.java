package com.hubEleven.common.code;

import static org.springframework.http.HttpStatus.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
	SUCCESS(OK, "성공했습니다.");

	private final HttpStatus status;
	private final String message;
}
