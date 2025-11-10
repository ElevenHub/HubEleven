package com.hubEleven.notification.ai.infrastructure.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gemini")
public record AiProperties(Api api, String model) {
	public record Api(String key) {}
}
