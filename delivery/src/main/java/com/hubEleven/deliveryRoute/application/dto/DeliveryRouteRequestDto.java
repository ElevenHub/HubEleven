package com.hubEleven.deliveryRoute.application.dto;

import com.hubEleven.delivery.domain.DeliveryStatus;
import java.util.UUID;

public record DeliveryRouteRequestDto(
		Integer seq,
		UUID toHubId,
		Long deliveryManagerId,
		DeliveryStatus status,
		Double expectedDistance,
		Long expectedDuration,
		Double actualDistance,
		Long actualDuration) {}
