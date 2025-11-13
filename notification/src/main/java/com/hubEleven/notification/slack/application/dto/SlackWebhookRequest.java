package com.hubEleven.notification.slack.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record SlackWebhookRequest(
		@JsonProperty("text") String text, @JsonProperty("attachments") List<Attachment> attachments) {
	public static SlackWebhookRequest create(String title, String messageText) {
		Attachment attachment = new Attachment(messageText);
		return new SlackWebhookRequest(title, List.of(attachment));
	}

	public record Attachment(@JsonProperty("text") String text) {}
}
