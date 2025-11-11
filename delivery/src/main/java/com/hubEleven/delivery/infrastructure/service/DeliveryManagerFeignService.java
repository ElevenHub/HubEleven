package com.hubEleven.delivery.infrastructure.service;

import com.commonLib.common.exception.GlobalException;
import com.commonLib.common.response.ApiResponse;
import com.hubEleven.delivery.domain.DeliveryErrorCode;
import com.hubEleven.delivery.infrastructure.client.DeliveryManagerFeignClient;
import com.hubEleven.delivery.infrastructure.dto.DeliveryManagerFeignResponseDto;
import com.hubEleven.deliveryManager.domain.DeliveryType;
import feign.FeignException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeliveryManagerFeignService {
	private final DeliveryManagerFeignClient deliveryManagerFeignClient;

	public ApiResponse<DeliveryManagerFeignResponseDto> getDeliveryManagerInfo(
			UUID orderId, UUID toHubId, DeliveryType deliveryType) {
		try {
			return deliveryManagerFeignClient.getDeliveryManager(orderId, toHubId, deliveryType);
		} catch (FeignException.NotFound e) {
			// 배달 매니저 정보 없음
			throw new GlobalException(DeliveryErrorCode.DELIVERY_MANAGER_NOT_FOUND);
		} catch (FeignException e) {
			// 기타 Feign 문제
			throw new GlobalException(DeliveryErrorCode.FEIGN_ERROR);
		}
	}
}
