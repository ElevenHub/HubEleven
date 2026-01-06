package com.hubEleven.company.infrastructure.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-service")
public interface HubClient {
	@GetMapping("/v1/hubs/{hubId}")
	HubDTO getHub(@PathVariable UUID hubId);

	record HubDTO(UUID hubId, String name, String address) {}
}
