package com.hubEleven.delivery.infrastructure.client;

import com.hubEleven.delivery.infrastructure.dto.CompanyFeignResponseDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-service", contextId = "delivery-company-client")
public interface CompanyFeignClient {
	@GetMapping("/v1/hub/{companyId}")
	CompanyFeignResponseDto getCompanyInfo(@PathVariable UUID companyId);
}
