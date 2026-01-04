package com.hubEleven.notification.ai.infrastructure.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@EnableConfigurationProperties(AiProperties.class)
@RequiredArgsConstructor
public class WebClientConfig {

	private static final String GEMINI_BASE_URL = "https://generativelanguage.googleapis.com";

	@Bean
	public WebClient geminiWebClient() {
		int timeoutMs = 30000;

		HttpClient httpClient =
				HttpClient.create()
						.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutMs)
						.responseTimeout(Duration.ofMillis(timeoutMs))
						.doOnConnected(
								conn ->
										conn.addHandlerLast(new ReadTimeoutHandler(timeoutMs / 1000))
												.addHandlerLast(new WriteTimeoutHandler(timeoutMs / 1000)));

		return WebClient.builder()
				.baseUrl(GEMINI_BASE_URL)
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();
	}
}
