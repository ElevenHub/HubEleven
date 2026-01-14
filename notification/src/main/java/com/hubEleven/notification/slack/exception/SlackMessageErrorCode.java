package com.hubEleven.notification.slack.exception;

import com.commonLib.common.code.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SlackMessageErrorCode implements StatusCode {
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증 정보가 필요합니다."),
	FORBIDDEN(HttpStatus.FORBIDDEN, "해당 작업을 수행할 권한이 없습니다."),
	SLACK_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "Slack 메시지를 찾을 수 없습니다."),
	SLACK_MESSAGE_ALREADY_SENT(HttpStatus.CONFLICT, "해당 주문은 이미 Slack으로 발송되었습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public String getName() {
		return name();
	}
}
