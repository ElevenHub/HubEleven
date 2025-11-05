package com.hubEleven.deliveryManager.presentation.dto.response;

import com.hubEleven.deliveryManager.domain.DeliveryManager;
import com.hubEleven.deliveryManager.domain.DeliveryType;
import java.util.UUID;

public record DeliveryManagerResponseDto(
		Long deliveryManagerId,
		UUID hubId,
		String slackId,
		DeliveryType deliveryType,
		int deliveryOrder) {
	public static DeliveryManagerResponseDto from(DeliveryManager deliveryManager) {
		return new DeliveryManagerResponseDto(
				deliveryManager.getDeliveryManagerId(),
				deliveryManager.getHubId(),
				deliveryManager.getSlackId(),
				deliveryManager.getDeliveryType(),
				deliveryManager.getDeliveryOrder());
	}
}
