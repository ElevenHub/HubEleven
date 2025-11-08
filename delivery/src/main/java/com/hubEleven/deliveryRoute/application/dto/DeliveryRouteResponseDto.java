package com.hubEleven.deliveryRoute.application.dto;

import com.hubEleven.delivery.domain.DeliveryStatus;
import com.hubEleven.deliveryRoute.domain.DeliveryRoute;
import java.util.UUID;

public record DeliveryRouteResponseDto(
		UUID deliveryRouteId,
		UUID deliveryId,
		Integer seq,
		UUID fromHubId,
		UUID toHubId,
		Long deliveryManagerId,
		DeliveryStatus status,
		Double expectedDistance,
		Long expectedDuration,
		Double actualDistance,
		Long actualDuration) {
	public static DeliveryRouteResponseDto from(DeliveryRoute deliveryRoute) {
		return new DeliveryRouteResponseDto(
				deliveryRoute.getId(),
				deliveryRoute.getDelivery().getId(),
				deliveryRoute.getSeq(),
				deliveryRoute.getFromHubId(),
				deliveryRoute.getToHubId(),
				deliveryRoute.getDeliveryManagerId(),
				deliveryRoute.getStatus(),
				deliveryRoute.getExpectedDistance(),
				deliveryRoute.getExpectedDuration(),
				deliveryRoute.getActualDistance(),
				deliveryRoute.getActualDuration());
	}
}
