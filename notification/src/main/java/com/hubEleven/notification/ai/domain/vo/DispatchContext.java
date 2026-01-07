package com.hubEleven.notification.ai.domain.vo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DispatchContext(
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
	public record Item(String name, int quantity, String note) {}
}
