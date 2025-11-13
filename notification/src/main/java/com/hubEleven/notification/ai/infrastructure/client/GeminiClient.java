package com.hubEleven.notification.ai.infrastructure.client;

import com.commonLib.common.exception.GlobalException;
import com.hubEleven.notification.ai.domain.exception.NotificationErrorCode;
import com.hubEleven.notification.ai.infrastructure.client.dto.GeminiRequest;
import com.hubEleven.notification.ai.infrastructure.client.dto.GeminiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiClient {

	@Qualifier("geminiWebClient")
	private final WebClient geminiWebClient;

	public GeminiResponse generate(String model, String apiKey, String prompt) {
		GeminiRequest body = GeminiRequest.fromPrompt(prompt);

		// block()이 null을 반환할 가능성(또는 예외 발생)을 명확히 처리
		return geminiWebClient
				.post()
				.uri(
						uriBuilder ->
								uriBuilder
										.path("/v1beta/models/{model}:generateContent")
										.queryParam("key", apiKey)
										.build(model))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(body)
				.retrieve()
				.onStatus(
						status -> status.value() == 400,
						resp ->
								resp.bodyToMono(String.class)
										.map(msg -> new GlobalException(NotificationErrorCode.AI_BAD_REQUEST)))
				.onStatus(
						status -> status.value() == 429,
						resp ->
								resp.bodyToMono(String.class)
										.map(msg -> new GlobalException(NotificationErrorCode.AI_RATE_LIMITED)))
				.onStatus(
						HttpStatusCode::is5xxServerError,
						resp ->
								resp.bodyToMono(String.class)
										.map(msg -> new GlobalException(NotificationErrorCode.AI_UPSTREAM_UNAVAILABLE)))
				.bodyToMono(GeminiResponse.class)
				.onErrorMap(
						ex -> {
							log.error("Error calling Gemini API", ex);
							return new GlobalException(NotificationErrorCode.AI_UPSTREAM_UNAVAILABLE);
						})
				.blockOptional()
				.orElseThrow(
						() -> {
							log.error(
									"GeminiClient: response body is empty for prompt (truncated): {}",
									prompt == null
											? ""
											: (prompt.length() > 200 ? prompt.substring(0, 200) : prompt));
							return new GlobalException(NotificationErrorCode.AI_UPSTREAM_UNAVAILABLE);
						});
	}
}
