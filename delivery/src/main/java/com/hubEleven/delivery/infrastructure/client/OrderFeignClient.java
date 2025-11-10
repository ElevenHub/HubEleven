package com.hubEleven.delivery.infrastructure.client;

import com.hubEleven.delivery.infrastructure.dto.OrderFeignResponseDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service", contextId = "delivery-order-client")
public interface OrderFeignClient {
	@GetMapping("/v1/order")
	OrderFeignResponseDto getOrder(@RequestParam UUID orderId);
}
