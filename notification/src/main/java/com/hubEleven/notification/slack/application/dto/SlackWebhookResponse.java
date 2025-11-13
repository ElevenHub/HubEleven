package com.hubEleven.notification.slack.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SlackWebhookResponse(
		@JsonProperty("ok") Boolean ok, @JsonProperty("error") String error) {}
