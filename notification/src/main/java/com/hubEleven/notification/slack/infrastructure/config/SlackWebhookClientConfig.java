package com.hubEleven.notification.slack.infrastructure.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@EnableConfigurationProperties(SlackProperties.class)
public class SlackWebhookClientConfig {

	@Bean
	public WebClient slackWebClient() {
		int timeoutMs = 30000;

		HttpClient httpClient =
				HttpClient.create()
						.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutMs)
						.responseTimeout(Duration.ofMillis(timeoutMs))
						.doOnConnected(
								conn ->
										conn.addHandlerLast(new ReadTimeoutHandler(timeoutMs / 1000))
												.addHandlerLast(new WriteTimeoutHandler(timeoutMs / 1000)));

		return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
	}
}
