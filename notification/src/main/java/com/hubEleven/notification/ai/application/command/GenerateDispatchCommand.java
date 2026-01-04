package com.hubEleven.notification.ai.application.command;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record GenerateDispatchCommand(
		UUID orderId,
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
		String deliveryManagerEmail,
		List<Item> items) {
	@Builder
	public record Item(String name, int quantity, String note) {}
}
