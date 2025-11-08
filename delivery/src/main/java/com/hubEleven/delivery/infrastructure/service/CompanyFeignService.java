package com.hubEleven.delivery.infrastructure.service;

import com.hubEleven.delivery.infrastructure.client.CompanyFeignClient;
import com.hubEleven.delivery.infrastructure.dto.CompanyFeignResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CompanyFeignService {
    private final CompanyFeignClient companyFeignClient;

    public CompanyFeignResponseDto getCompanyInfo(UUID companyId) {
        return companyFeignClient.getCompanyInfo(companyId);
    }
}
