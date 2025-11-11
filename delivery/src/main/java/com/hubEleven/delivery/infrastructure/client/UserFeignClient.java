package com.hubEleven.delivery.infrastructure.client;

import com.hubEleven.delivery.infrastructure.config.FeignClientConfig;
import com.hubEleven.delivery.infrastructure.dto.UserFeignResponseDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", contextId = "delivery-user-client" , configuration = FeignClientConfig.class)
public interface UserFeignClient {
	@GetMapping("/v1/user")
	UserFeignResponseDto getUser(@RequestParam UUID companyId, @RequestParam String deliveryType);
}
