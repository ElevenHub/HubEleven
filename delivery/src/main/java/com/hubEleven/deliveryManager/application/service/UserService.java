package com.hubEleven.deliveryManager.application.service;

import com.commonLib.common.exception.GlobalException;
import com.commonLib.common.response.ApiResponse;
import com.hubEleven.deliveryManager.domain.exception.DeliveryManagerErrorCode;
import com.hubEleven.deliveryManager.infrastructure.client.UserFeignClient;
import com.hubEleven.deliveryManager.infrastructure.dto.UserInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
	private final UserFeignClient userFeignClient;

	public UserService(UserFeignClient userFeignClient) {
		this.userFeignClient = userFeignClient;
	}

	public UserInfoResponse getUser(Long id, Long requestUserId, String requestUserRole) {

		try {
			ResponseEntity<ApiResponse<UserInfoResponse>> response =
					userFeignClient.getUser(id, requestUserId, requestUserRole);
			// 응답 검증
			if (response == null) {
				log.warn("유저 조회 실패: 응답이 비어 있음 (id: {})", id);
				throw new GlobalException(DeliveryManagerErrorCode.USER_NOT_FOUND);
			}
			return response.getBody().data();
		} catch (Exception e) {
			log.error("유저 조회 중 예외 발생 (id: {}): {}", id, e.getMessage());
			throw new GlobalException(DeliveryManagerErrorCode.USER_NOT_FOUND);
		}
	}
}
