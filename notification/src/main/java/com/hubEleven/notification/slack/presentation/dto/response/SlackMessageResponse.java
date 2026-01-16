package com.hubEleven.notification.slack.presentation.dto.response;

import com.hubEleven.notification.slack.application.dto.response.SlackMessageResult;
import com.hubEleven.notification.slack.domain.vo.SlackMessageStatus;
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
        LocalDateTime createdAt
) {
    public static SlackMessageResponse from(SlackMessageResult r) {
        return new SlackMessageResponse(
                r.messageId(),
                r.orderId(),
                r.recipientId(),
                r.channel(),
                r.message(),
                r.status(),
                r.sentAt(),
                r.updatedAt(),
                r.createdAt()
        );
    }
}
