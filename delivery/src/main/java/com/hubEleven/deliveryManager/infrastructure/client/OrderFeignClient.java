package com.hubEleven.deliveryManager.infrastructure.client;

import com.commonLib.common.response.ApiResponse;
import com.hubEleven.deliveryManager.infrastructure.dto.OrderResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", contextId = "deliveryManager-order-client")
public interface OrderFeignClient {

	@GetMapping("/v1/orders/{orderId}")
	ResponseEntity<ApiResponse<OrderResponse>> getOrderDetail(@PathVariable UUID orderId);
}
