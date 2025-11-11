package com.hubEleven.delivery.infrastructure.client;

import com.commonLib.common.response.ApiResponse;
import com.hubEleven.delivery.infrastructure.config.FeignClientConfig;
import com.hubEleven.delivery.infrastructure.dto.DeliveryManagerFeignResponseDto;
import com.hubEleven.deliveryManager.domain.DeliveryType;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "delivery-service", contextId = "delivery-client" , configuration = FeignClientConfig.class)
public interface DeliveryManagerFeignClient {
	@PatchMapping("/v1/delivery-managers/assign")
    ApiResponse<DeliveryManagerFeignResponseDto> getDeliveryManager(
			@RequestParam UUID orderId,
			@RequestParam UUID toHubId,
			@RequestParam DeliveryType deliveryType);
}
