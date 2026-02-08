package com.hubEleven.notification.slack.infrastructure.config;

import com.slack.api.Slack;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SlackSdkConfig.class)
@ConfigurationProperties(prefix = "slack.sdk")
public class SlackSdkConfig {

	private String botToken;

	public String getBotToken() {
		return botToken;
	}

	public void setBotToken(String botToken) {
		this.botToken = botToken;
	}

	@Bean
	public Slack slack() {
		return Slack.getInstance();
	}
}
