package com.hubEleven.deliveryManager.presentation.dto.request;

import com.hubEleven.deliveryManager.domain.DeliveryType;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record DeliveryManagerAssignRequestDto(
		@NotNull UUID hubId, // 도착허브
		@NotNull UUID orderId, // 주문ID
		@NotNull DeliveryType deliveryType // 업체배송 vs 허브배송
		) {}
