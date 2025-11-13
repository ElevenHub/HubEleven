package com.hubEleven.notification.slack.infrastructure.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "slack")
public record SlackProperties(String webhookUrl) {}
