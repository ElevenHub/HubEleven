package com.hubEleven.notification.slack.application.command;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateSlackMessageCommand(
        UUID orderId,
        String recipientId,
        String channel,

        String customerName,
        String customerEmail,
        LocalDateTime orderDateTime,
        LocalDateTime requestedArrivalDateTime,

        String sourceHub,
        List<String> viaHubs,

        String destinationHub,
        String destinationAddress,
        String requestNote,

        String deliveryManagerName,
        String deliverManagerEmail,

        List<CreateSlackMessageItemCommand> items
) {
}
