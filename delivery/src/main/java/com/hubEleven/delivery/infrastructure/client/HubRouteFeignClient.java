package com.hubEleven.delivery.infrastructure.client;

import com.hubEleven.delivery.infrastructure.config.FeignClientConfig;
import com.hubEleven.delivery.infrastructure.dto.HubRouteFeignResponseDto;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hub-service", contextId = "delivery-hub-client" , configuration = FeignClientConfig.class)
public interface HubRouteFeignClient {
	@GetMapping("/v1/hubRoute")
	List<HubRouteFeignResponseDto> getRoute(@RequestParam UUID fromHubId, @RequestParam UUID toHubId);
}
