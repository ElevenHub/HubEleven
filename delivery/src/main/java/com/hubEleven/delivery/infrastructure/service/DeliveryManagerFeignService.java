package com.hubEleven.delivery.infrastructure.service;

import com.hubEleven.delivery.infrastructure.client.DeliveryManagerFeignClient;
import com.hubEleven.delivery.infrastructure.dto.DeliveryManagerFeignResponseDto;
import com.hubEleven.deliveryManager.domain.DeliveryType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeliveryManagerFeignService {
	private final DeliveryManagerFeignClient deliveryManagerFeignClient;

	public DeliveryManagerFeignResponseDto getDeliveryManagerInfo(
			UUID orderId, UUID toHubId, DeliveryType deliveryType) {
		return deliveryManagerFeignClient.getDeliveryManager(orderId, toHubId, deliveryType);
	}
}
