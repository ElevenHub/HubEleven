package com.hubEleven.delivery.infrastructure.service;

import com.hubEleven.delivery.infrastructure.client.HubRouteFeignClient;
import com.hubEleven.delivery.infrastructure.dto.HubRouteFeignResponseDto;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HubFeignService {
	private HubRouteFeignClient hubRouteFeignClient;

	public List<HubRouteFeignResponseDto> getRoute(UUID fromHubId, UUID toHubId) {
		return hubRouteFeignClient.getRoute(fromHubId, toHubId);
	}
}
