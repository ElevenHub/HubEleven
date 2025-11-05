package com.hubEleven.deliveryManager.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record DeliveryManagerCreateRequestDto(
		// 배송타입이 허브일때는 null, 업체일때는 id필수여야함
		@NotNull(message = "ID는 필수입니다.") Long deliveryManagerId) {}
