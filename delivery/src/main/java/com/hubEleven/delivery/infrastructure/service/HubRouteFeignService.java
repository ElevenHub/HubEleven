package com.hubEleven.delivery.infrastructure.service;

import com.commonLib.common.exception.GlobalException;
import com.hubEleven.delivery.domain.DeliveryErrorCode;
import com.hubEleven.delivery.infrastructure.client.HubRouteFeignClient;
import com.hubEleven.delivery.infrastructure.dto.HubRouteFeignResponseDto;
import feign.FeignException;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HubRouteFeignService {
	private HubRouteFeignClient hubRouteFeignClient;

	public List<HubRouteFeignResponseDto> getRoute(UUID fromHubId, UUID toHubId) {
		try {
			return hubRouteFeignClient.getRoute(fromHubId, toHubId);
		} catch (FeignException.NotFound e) {
			// 허브 정보 없음
			throw new GlobalException(DeliveryErrorCode.HUB_NOT_FOUND);
		} catch (FeignException e) {
			// 기타 Feign 문제
			throw new GlobalException(DeliveryErrorCode.FEIGN_ERROR);
		}
	}
}
