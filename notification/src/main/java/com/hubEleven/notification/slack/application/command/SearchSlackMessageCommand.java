package com.hubEleven.notification.slack.application.command;

import com.hubEleven.notification.slack.domain.model.SlackMessageStatus;

import java.time.LocalDateTime;

public record SearchSlackMessageCommand(
        SlackMessageStatus status,
        String channel,
        LocalDateTime dateFrom,
        LocalDateTime dateTo
) {
}
