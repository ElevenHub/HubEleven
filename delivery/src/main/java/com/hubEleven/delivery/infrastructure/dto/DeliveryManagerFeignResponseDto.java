package com.hubEleven.delivery.infrastructure.dto;

import java.util.UUID;

public record DeliveryManagerFeignResponseDto(Long deliveryManagerId, UUID deliveryOrder) {}
