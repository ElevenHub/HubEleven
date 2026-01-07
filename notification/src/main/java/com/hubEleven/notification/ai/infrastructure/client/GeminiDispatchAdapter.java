package com.hubEleven.notification.ai.infrastructure.client;

import com.commonLib.common.exception.GlobalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubEleven.notification.ai.application.port.DispatchAiPort;
import com.hubEleven.notification.ai.domain.vo.DispatchResult;
import com.hubEleven.notification.ai.exception.NotificationErrorCode;
import com.hubEleven.notification.ai.infrastructure.client.dto.response.GeminiResponse;
import com.hubEleven.notification.ai.infrastructure.config.AiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeminiDispatchAdapter implements DispatchAiPort {

	private final GeminiClient geminiClient;
	private final AiProperties aiProperties;
	private final ObjectMapper objectMapper;

	@Override
	public DispatchResult generate(String prompt) {
		GeminiResponse response =
				geminiClient.generate(aiProperties.model(), aiProperties.api().key(), prompt);
		if (response == null) throw new GlobalException(NotificationErrorCode.AI_UPSTREAM_UNAVAILABLE);

		String raw = response.primaryText();
		if (raw == null || raw.isBlank())
			throw new GlobalException(NotificationErrorCode.AI_UPSTREAM_UNAVAILABLE);

		String cleaned = cleanJsonResponse(raw);
		String json = extractFirstJsonObject(cleaned);

		try {
			Payload payload = objectMapper.readValue(json, Payload.class);

			if (payload.finalDispatchDeadline() == null || payload.finalDispatchDeadline().isBlank()) {
				throw new GlobalException(NotificationErrorCode.AI_RESPONSE_PARSE_FAIL);
			}
			if (payload.messageBody() == null || payload.messageBody().isBlank()) {
				throw new GlobalException(NotificationErrorCode.AI_RESPONSE_PARSE_FAIL);
			}

			return new DispatchResult(payload.finalDispatchDeadline(), payload.messageBody(), raw);
		} catch (Exception e) {
			throw new GlobalException(NotificationErrorCode.AI_RESPONSE_PARSE_FAIL);
		}
	}

	private String cleanJsonResponse(String rawJson) {
		if (rawJson == null || rawJson.isBlank()) return rawJson;
		String cleaned = rawJson.trim();

		if (cleaned.startsWith("```json")) cleaned = cleaned.substring(7);
		else if (cleaned.startsWith("```")) cleaned = cleaned.substring(3);

		if (cleaned.endsWith("```")) cleaned = cleaned.substring(0, cleaned.length() - 3);
		return cleaned.trim();
	}

	private String extractFirstJsonObject(String s) {
		if (s == null) return null;
		int start = s.indexOf('{');
		int end = s.lastIndexOf('}');
		if (start < 0 || end < 0 || end <= start) return s;
		return s.substring(start, end + 1);
	}

	private record Payload(String finalDispatchDeadline, String messageBody) {}
}
