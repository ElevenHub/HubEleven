package com.hubEleven.notification.ai.application.service;

import com.commonLib.common.exception.GlobalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubEleven.notification.ai.application.dto.MessageGenerationRequest;
import com.hubEleven.notification.ai.application.dto.MessageGenerationResponse;
import com.hubEleven.notification.ai.domain.exception.NotificationErrorCode;
import com.hubEleven.notification.ai.domain.model.AiRequestLog;
import com.hubEleven.notification.ai.domain.repository.AiRequestLogRepository;
import com.hubEleven.notification.ai.domain.service.PromptDomainService;
import com.hubEleven.notification.ai.infrastructure.client.GeminiClient;
import com.hubEleven.notification.ai.infrastructure.client.dto.GeminiResponse;
import com.hubEleven.notification.ai.infrastructure.configuration.AiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiAppService {

	private final AiRequestLogRepository aiRequestLogRepository;
	private final PromptDomainService promptDomainService;
	private final GeminiClient geminiClient;
	private final AiProperties aiProperties;
	private final ObjectMapper objectMapper;

	@Transactional
	public MessageGenerationResponse generateDispatchGuidance(MessageGenerationRequest request) {
		if (aiRequestLogRepository.existsByOrderId(request.orderId())) {
			throw new GlobalException(NotificationErrorCode.AI_REQUEST_DUPLICATED);
		}

		String prompt = promptDomainService.buildDispatchGuidancePrompt(request);
		AiRequestLog requestLog =
				aiRequestLogRepository.save(AiRequestLog.requested(request.orderId(), prompt));
		String metadata = serialize(request);

		try {
			GeminiResponse response =
					geminiClient.generate(aiProperties.model(), aiProperties.api().key(), prompt);

			if (response == null) {
				log.error("GeminiClient returned null response for orderId: {}", request.orderId());
				requestLog.fail("Gemini returned null response", metadata);
				throw new GlobalException(NotificationErrorCode.AI_UPSTREAM_UNAVAILABLE);
			}
			String raw = response.primaryText();
			if (raw == null || raw.isBlank()) {
				log.error("GeminiClient returned empty primaryText for orderId: {}", request.orderId());
				requestLog.fail("Gemini returned empty body", metadata);
				throw new GlobalException(NotificationErrorCode.AI_UPSTREAM_UNAVAILABLE);
			}

			log.info("Gemini raw response for orderId: {} - {}", request.orderId(), raw);

			MessageGenerationResponse result = parseResponse(raw);
			requestLog.success(raw, metadata);
			return result;
		} catch (GlobalException ex) {
			log.error(
					"GlobalException occurred for orderId: {} - {}", request.orderId(), ex.getMessage(), ex);
			requestLog.fail(ex.getMessage(), metadata);
			throw ex;
		} catch (Exception ex) {
			log.error(
					"Unexpected exception occurred for orderId: {} - {}",
					request.orderId(),
					ex.getMessage(),
					ex);
			requestLog.fail(ex.getMessage(), metadata);
			throw new GlobalException(NotificationErrorCode.AI_GENERATION_FAIL);
		}
	}

	private MessageGenerationResponse parseResponse(String rawJson) {
		try {
			// 코드 블록(```json, ```) 제거
			String cleanedJson = cleanJsonResponse(rawJson);
			log.debug("Cleaned JSON response: {}", cleanedJson);

			ResponsePayload payload = objectMapper.readValue(cleanedJson, ResponsePayload.class);

			// 필수 필드 검증
			if (payload.finalDispatchDeadline() == null || payload.finalDispatchDeadline().isBlank()) {
				log.error("finalDispatchDeadline is null or blank in response: {}", cleanedJson);
				throw new GlobalException(NotificationErrorCode.AI_RESPONSE_PARSE_FAIL);
			}
			if (payload.messageBody() == null || payload.messageBody().isBlank()) {
				log.error("messageBody is null or blank in response: {}", cleanedJson);
				throw new GlobalException(NotificationErrorCode.AI_RESPONSE_PARSE_FAIL);
			}

			return MessageGenerationResponse.success(
					payload.finalDispatchDeadline(), payload.messageBody());
		} catch (JsonProcessingException e) {
			log.error("Failed to parse Gemini response as JSON. Raw response: {}", rawJson, e);
			throw new GlobalException(NotificationErrorCode.AI_RESPONSE_PARSE_FAIL);
		} catch (GlobalException e) {
			throw e;
		} catch (Exception e) {
			log.error("Unexpected error while parsing response: {}", rawJson, e);
			throw new GlobalException(NotificationErrorCode.AI_RESPONSE_PARSE_FAIL);
		}
	}

	private String cleanJsonResponse(String rawJson) {
		if (rawJson == null || rawJson.isBlank()) {
			return rawJson;
		}

		String cleaned = rawJson.trim();

		// ```json ... ``` 형태의 코드 블록 제거
		if (cleaned.startsWith("```json")) {
			cleaned = cleaned.substring(7);
		} else if (cleaned.startsWith("```")) {
			cleaned = cleaned.substring(3);
		}

		if (cleaned.endsWith("```")) {
			cleaned = cleaned.substring(0, cleaned.length() - 3);
		}

		return cleaned.trim();
	}

	private String serialize(MessageGenerationRequest request) {
		try {
			return objectMapper.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			return null;
		}
	}

	private record ResponsePayload(String finalDispatchDeadline, String messageBody) {}
}
