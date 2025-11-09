package com.hubEleven.product.infrastructure.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company-service")
public interface CompanyFeignClient {

    @GetMapping("/api/v1/companies/{companyId}")
    CompanyResponse getCompany(@PathVariable UUID companyId);

    record CompanyResponse(UUID companyId, String name) {}
}
