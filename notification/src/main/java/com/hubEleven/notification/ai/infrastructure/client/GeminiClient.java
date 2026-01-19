package com.hubEleven.notification.ai.infrastructure.client;

import com.commonLib.common.exception.GlobalException;
import com.hubEleven.notification.ai.exception.AiErrorCode;
import com.hubEleven.notification.ai.infrastructure.client.dto.request.GeminiRequest;
import com.hubEleven.notification.ai.infrastructure.client.dto.response.GeminiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiClient {

	@Qualifier("geminiWebClient")
	private final WebClient geminiWebClient;

	public GeminiResponse generate(String model, String apiKey, String prompt) {
		GeminiRequest body = GeminiRequest.fromPrompt(prompt);

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
						s -> s.value() == 400,
						resp ->
								resp.bodyToMono(String.class)
										.flatMap(msg -> Mono.error(new GlobalException(AiErrorCode.AI_BAD_REQUEST))))
				.onStatus(
						s -> s.value() == 429,
						resp ->
								resp.bodyToMono(String.class)
										.flatMap(msg -> Mono.error(new GlobalException(AiErrorCode.AI_RATE_LIMITED))))
				.onStatus(
						HttpStatusCode::is5xxServerError,
						resp ->
								resp.bodyToMono(String.class)
										.flatMap(
												msg ->
														Mono.error(new GlobalException(AiErrorCode.AI_UPSTREAM_UNAVAILABLE))))
				.bodyToMono(GeminiResponse.class)
				.onErrorMap(
						ex ->
								(ex instanceof GlobalException)
										? ex
										: new GlobalException(AiErrorCode.AI_UPSTREAM_UNAVAILABLE))
				.blockOptional()
				.orElseThrow(() -> new GlobalException(AiErrorCode.AI_UPSTREAM_UNAVAILABLE));
	}
}
