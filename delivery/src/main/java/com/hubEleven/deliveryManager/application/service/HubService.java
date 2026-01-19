package com.hubEleven.deliveryManager.application.service;

import com.commonLib.common.exception.GlobalException;
import com.commonLib.common.response.ApiResponse;
import com.hubEleven.deliveryManager.domain.exception.DeliveryManagerErrorCode;
import com.hubEleven.deliveryManager.infrastructure.client.HubFeignClient;
import com.hubEleven.deliveryManager.infrastructure.dto.HubResponseDto;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HubService {
	private final HubFeignClient hubFeignClient;

	public HubService(HubFeignClient hubFeignClient) {
		this.hubFeignClient = hubFeignClient;
	}

	public HubResponseDto getHub(UUID hubId) {
		try {
			ResponseEntity<ApiResponse<HubResponseDto>> response = hubFeignClient.getHub(hubId);

			if (response == null) {
				log.info("허브 조회 실패: 응답이 비어 있음 (hubId: {})", hubId);
				throw new GlobalException(DeliveryManagerErrorCode.HUB_NOT_FOUND);
			}

			return response.getBody().data();

		} catch (Exception e) {
			// 로깅, 예외 변환 등
			log.info("잘못된 허브 ID");
			throw new GlobalException(DeliveryManagerErrorCode.HUB_NOT_FOUND);
		}
	}
}
