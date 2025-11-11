package com.hubEleven.delivery.infrastructure.client;

import com.commonLib.common.response.ApiResponse;
import com.hubEleven.delivery.infrastructure.config.FeignClientConfig;
import com.hubEleven.delivery.infrastructure.dto.CompanyFeignResponseDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
		name = "company-service",
		contextId = "delivery-company-client",
		configuration = FeignClientConfig.class)
public interface CompanyFeignClient {
	@GetMapping("/v1/companies/{companyId}")
	ApiResponse<CompanyFeignResponseDto> getCompanyInfo(@PathVariable UUID companyId);
}
