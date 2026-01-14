package com.hubEleven.notification.slack.application.dto.request;

import com.hubEleven.notification.slack.domain.model.SlackMessageStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record SlackMessageResult(
        UUID messageId,
        UUID orderId,
        String recipientId,
        String channel,
        String message,
        SlackMessageStatus status,
        LocalDateTime sentAt,
        LocalDateTime updateAt,
        LocalDateTime createAt
) {
}
