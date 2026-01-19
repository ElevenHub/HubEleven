package com.hubEleven.deliveryManager.infrastructure.client;

import com.hubEleven.deliveryManager.infrastructure.dto.HubResponseDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub-service", contextId = "deliveryManager-hub-client")
public interface HubFeignClient {

	@GetMapping("/v1/hubs/{hubId}")
	ResponseEntity<HubResponseDto> getHub(@PathVariable("hubId") UUID hubId);
}
