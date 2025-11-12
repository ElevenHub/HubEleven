package com.hubEleven.deliveryManager.infrastructure.dto;

import java.util.UUID;

public record HubResponseDto(
		UUID hubId,
		String name,
		String address,
		Double latitude,
		Double longitude,
		String regionCode) {}
