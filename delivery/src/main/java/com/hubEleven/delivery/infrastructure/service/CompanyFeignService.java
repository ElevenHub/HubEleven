package com.hubEleven.delivery.infrastructure.service;

import com.commonLib.common.exception.GlobalException;
import com.hubEleven.delivery.domain.DeliveryErrorCode;
import com.hubEleven.delivery.infrastructure.client.CompanyFeignClient;
import com.hubEleven.delivery.infrastructure.dto.CompanyFeignResponseDto;
import feign.FeignException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CompanyFeignService {
	private final CompanyFeignClient companyFeignClient;

	public CompanyFeignResponseDto getCompanyInfo(UUID companyId) {
		try {
			return companyFeignClient.getCompanyInfo(companyId);
		} catch (FeignException.NotFound e) {
			// 업체 정보 없음
			throw new GlobalException(DeliveryErrorCode.COMPANY_NOT_FOUND);
		} catch (FeignException e) {
			// 기타 Feign 문제
			throw new GlobalException(DeliveryErrorCode.FEIGN_ERROR);
		}
	}
}
