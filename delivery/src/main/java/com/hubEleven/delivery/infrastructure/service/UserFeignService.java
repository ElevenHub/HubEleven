package com.hubEleven.delivery.infrastructure.service;

import com.commonLib.common.exception.GlobalException;
import com.hubEleven.delivery.domain.DeliveryErrorCode;
import com.hubEleven.delivery.infrastructure.client.UserFeignClient;
import com.hubEleven.delivery.infrastructure.dto.UserFeignResponseDto;
import feign.FeignException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserFeignService {
	private final UserFeignClient userFeignClient;

	public UserFeignResponseDto getUserInfo(UUID companyId) {
		try {
			return userFeignClient.getUser(companyId, "companyManager");
		} catch (FeignException.NotFound e) {
			// 유저 정보 없음
			throw new GlobalException(DeliveryErrorCode.USER_NOT_FOUND);
		} catch (FeignException e) {
			// 기타 Feign 문제
			throw new GlobalException(DeliveryErrorCode.FEIGN_ERROR);
		}
	}
}
