package com.hubEleven.notification.slack.application.command;

import java.util.UUID;

public record UpdateSlackMessageCommand(UUID messageId, String message) {}
