package com.hubEleven.notification.ai.domain.exception;

import com.commonLib.common.code.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotificationErrorCode implements StatusCode {
	PROMPT_BUILD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "프롬프트 생성에 실패했습니다."),
	AI_UPSTREAM_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "AI 제공자와 통신할 수 없습니다."),
	AI_BAD_REQUEST(HttpStatus.BAD_REQUEST, "AI 제공자에 잘못된 요청이 전달되었습니다."),
	AI_RATE_LIMITED(HttpStatus.TOO_MANY_REQUESTS, "AI 제공자의 호출 한도를 초과했습니다."),
	AI_RESPONSE_PARSE_FAIL(HttpStatus.BAD_GATEWAY, "AI 응답을 해석하는 데 실패했습니다."),
	AI_GENERATION_FAIL(HttpStatus.BAD_GATEWAY, "AI 결과 생성에 실패했습니다."),
	AI_REQUEST_DUPLICATED(HttpStatus.CONFLICT, "해당 주문의 AI 요청이 이미 존재합니다.");

	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public String getName() {
		return name();
	}
}
