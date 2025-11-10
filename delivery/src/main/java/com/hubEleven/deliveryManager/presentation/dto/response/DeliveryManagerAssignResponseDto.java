package com.hubEleven.deliveryManager.presentation.dto.response;

import com.hubEleven.deliveryManager.domain.DeliveryManager;
import java.util.UUID;

public record DeliveryManagerAssignResponseDto(
		UUID orderId, // 주문번호
		Long deliveryManagerId // 배송담당자 ID
		) {

	public static DeliveryManagerAssignResponseDto of(UUID orderId, DeliveryManager deliveryManager) {
		return new DeliveryManagerAssignResponseDto(orderId, deliveryManager.getDeliveryManagerId());
	}
}
