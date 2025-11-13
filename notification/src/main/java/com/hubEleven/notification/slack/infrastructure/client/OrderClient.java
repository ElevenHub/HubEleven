package com.hubEleven.notification.slack.infrastructure.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service")
public interface OrderClient {
	@GetMapping("/v1/orders/{orderId}")
	OrderDTO getOrder(@PathVariable UUID orderId);

	record OrderDTO(UUID orderId) {}
}
