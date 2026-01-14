package com.hubEleven.notification.slack.domain.vo;

public record SlackMessageItem(
        String name,
        int quantity,
        String note
) {
}
