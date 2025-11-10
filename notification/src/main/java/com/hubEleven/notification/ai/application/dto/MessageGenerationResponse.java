package com.hubEleven.notification.ai.application.dto;

// Ai가 산출한 최종 발송 시한 결과
public record MessageGenerationResponse(
		Boolean success, String message, Data data, String errorCode) {
	public record Data(String finalDispatchDeadline, String messageBody) {}

	public static MessageGenerationResponse success(
			String finalDispatchDeadline, String messageBody) {
		return new MessageGenerationResponse(
				true, "최종 발송 시한 생성이 성공했습니다.", new Data(finalDispatchDeadline, messageBody), null);
	}

	public static MessageGenerationResponse failure(String errorCode, String message) {
		return new MessageGenerationResponse(false, message, null, errorCode);
	}
}
