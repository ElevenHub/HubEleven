package com.hubEleven.notification.ai.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// 최종 발송 시한 산출을 위해 AI에게 전달할 주문/배송 컨텍스트
public record MessageGenerationRequest(
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
	public static MessageGenerationRequest of(
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
		return new MessageGenerationRequest(
				orderId,
				customerName,
				customerEmail,
				orderDateTime,
				requestedArrivalDateTime,
				sourceHub,
				viaHubs,
				destinationHub,
				destinationAddress,
				requestNote,
				deliveryManagerName,
				deliveryManagerEmail,
				items);
	}

	public record Item(String name, int quantity, String note) {
		public static Item of(String name, int quantity, String note) {
			return new Item(name, quantity, note);
		}
	}
}
