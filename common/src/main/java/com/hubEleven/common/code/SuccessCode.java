package com.hubEleven.common.code;

import static org.springframework.http.HttpStatus.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
	SUCCESS(HttpStatus.OK, "성공했습니다."),
	CREATED(HttpStatus.CREATED, "생성되었습니다."),
	UPDATED(HttpStatus.OK, "수정되었습니다."),
	DELETED(HttpStatus.OK, "삭제되었습니다.");

	private final HttpStatus status;
	private final String message;
}
