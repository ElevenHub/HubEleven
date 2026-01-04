package com.hubEleven.notification.ai.application.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record GenerateMessageResponse(
		Boolean success, String message, Data data, String errorCode) {
	public record Data(String finalDispatchDeadline, String messageBody) {}

	public static GenerateMessageResponse success(String finalDispatchDeadline, String messageBody) {
		return new GenerateMessageResponse(
				true, "최종 발송 시한 생성이 성공했습니다.", new Data(finalDispatchDeadline, messageBody), null);
	}

	public static GenerateMessageResponse failure(String errorCode, String message) {
		return GenerateMessageResponse.builder()
				.success(false)
				.message(message)
				.data(null)
				.errorCode(errorCode)
				.build();
	}
}
