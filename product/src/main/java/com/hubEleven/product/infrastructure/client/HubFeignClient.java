package com.hubEleven.product.infrastructure.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-service")
public interface HubFeignClient {

	@GetMapping("/v1/hubs/{hubId}")
	HubResponse getHub(@PathVariable UUID hubId);

	record HubResponse(UUID hubId, String name, String address) {}
}
