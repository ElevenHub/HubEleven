package com.hubEleven.notification.slack.application.command;

public record CreateSlackMessageItemCommand(String name, int quantity, String note) {}
