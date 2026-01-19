package com.hubEleven.deliveryManager.infrastructure.client;

import com.commonLib.common.response.ApiResponse;
import com.hubEleven.deliveryManager.infrastructure.dto.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", contextId = "deliveryManager-user-client")
public interface UserFeignClient {

	@GetMapping("/v1/user/{id}")
	ResponseEntity<ApiResponse<UserInfoResponse>> getUser(
			@PathVariable("id") Long id,
			@RequestHeader("X-User-Id") Long requestUserId,
			@RequestHeader("X-User-Role") String requestUserRole);
}
