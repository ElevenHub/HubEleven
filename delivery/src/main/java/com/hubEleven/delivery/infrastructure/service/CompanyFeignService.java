package com.hubEleven.delivery.infrastructure.service;

import com.hubEleven.delivery.infrastructure.client.CompanyFeignClient;
import com.hubEleven.delivery.infrastructure.dto.CompanyFeignResponseDto;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CompanyFeignService {
	private final CompanyFeignClient companyFeignClient;

	public CompanyFeignResponseDto getCompanyInfo(UUID companyId) {
		return companyFeignClient.getCompanyInfo(companyId);
	}
}
