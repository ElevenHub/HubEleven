package com.hubEleven.notification.slack.application.dto;

import com.hubEleven.notification.slack.domain.model.SlackMessage;
import com.hubEleven.notification.slack.domain.model.SlackMessageStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record SlackMessageResponse(
		UUID messageId,
		UUID orderId,
		String recipientId,
		String channel,
		String message,
		SlackMessageStatus status,
		LocalDateTime sentAt,
		LocalDateTime updatedAt,
		LocalDateTime createdAt) {
	public static SlackMessageResponse from(SlackMessage slackMessage) {
		return new SlackMessageResponse(
				slackMessage.getId(),
				slackMessage.getOrderId(),
				slackMessage.getRecipientId(),
				slackMessage.getChannel(),
				slackMessage.getMessage(),
				slackMessage.getStatus(),
				slackMessage.getSentAt(),
				slackMessage.getUpdatedAt(),
				slackMessage.getCreatedAt());
	}
}
