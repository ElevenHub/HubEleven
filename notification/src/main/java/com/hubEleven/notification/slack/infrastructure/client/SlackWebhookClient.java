package com.hubEleven.notification.slack.infrastructure.client;

import com.hubEleven.notification.slack.application.dto.SlackWebhookRequest;
import com.hubEleven.notification.slack.infrastructure.config.SlackProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackWebhookClient {

	private final WebClient slackWebClient;
	private final SlackProperties slackProperties;

	public Mono<Boolean> sendMessage(String title, String messageText) {
		SlackWebhookRequest request = SlackWebhookRequest.create(title, messageText);

		return slackWebClient
				.post()
				.uri(slackProperties.webhookUrl())
				.bodyValue(request)
				.retrieve()
				.bodyToMono(String.class)
				.map("ok"::equalsIgnoreCase)
				.doOnSuccess(
						ok -> {
							if (Boolean.TRUE.equals(ok)) log.info("Slack webhook sent OK");
							else log.warn("Slack webhook responded non-ok");
						})
				.doOnError(err -> log.error("Slack webhook send failed", err));
	}
}
