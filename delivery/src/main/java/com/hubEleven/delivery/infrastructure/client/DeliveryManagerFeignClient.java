package com.hubEleven.delivery.infrastructure.client;

import com.hubEleven.delivery.infrastructure.dto.DeliveryManagerFeignResponseDto;
import com.hubEleven.deliveryManager.domain.DeliveryType;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "delivery-service")
public interface DeliveryManagerFeignClient {
	@GetMapping("/v1/deliveryManager")
	DeliveryManagerFeignResponseDto getDeliveryManager(
			@RequestParam UUID orderId,
			@RequestParam UUID toHubId,
			@RequestParam DeliveryType deliveryType);
}
