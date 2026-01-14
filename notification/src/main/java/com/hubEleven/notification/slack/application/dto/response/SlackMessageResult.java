package com.hubEleven.notification.slack.application.dto.response;

import com.hubEleven.notification.slack.domain.model.SlackMessage;
import com.hubEleven.notification.slack.domain.vo.SlackMessageStatus;
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
        LocalDateTime updatedAt,
        LocalDateTime createdAt
) {
    public static SlackMessageResult from(SlackMessage m) {
        return new SlackMessageResult(
                m.getId(),
                m.getOrderId(),
                m.getRecipientId(),
                m.getChannel(),
                m.getMessage(),
                m.getStatus(),
                m.getSentAt(),
                m.getUpdatedAt(),
                m.getCreatedAt()
        );
    }
}

