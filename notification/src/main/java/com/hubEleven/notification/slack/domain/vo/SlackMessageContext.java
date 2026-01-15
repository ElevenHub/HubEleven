package com.hubEleven.notification.slack.domain.vo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SlackMessageContext(
		UUID orderId,
		String customerName,
		String customerEmail,
		LocalDateTime orderDateTime,
		String sourceHub,
		List<String> viaHubs,
		String destinationHub,
		String destinationAddress,
		String requestNote,
		String deliveryManagerName,
		String deliveryManagerEmail,
		List<SlackMessageItem> items) {}
