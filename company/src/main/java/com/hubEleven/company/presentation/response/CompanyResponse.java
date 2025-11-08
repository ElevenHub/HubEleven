package com.hubEleven.company.presentation.response;

import com.hubEleven.company.application.dto.CompanyDTO;
import com.hubEleven.company.domain.model.Company;
import com.hubEleven.company.domain.model.CompanyStatus;
import com.hubEleven.company.domain.model.CompanyType;
import java.time.LocalDateTime;
import java.util.UUID;

public record CompanyResponse(
		UUID companyId,
		UUID hubId,
		String name,
		CompanyType companyType,
		CompanyStatus status,
		String slackId,
		String address,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
	public static CompanyResponse from(CompanyDTO company) {
		return new CompanyResponse(
				company.companyId(),
				company.hubId(),
				company.name(),
				company.companyType(),
				company.status(),
				company.slackId(),
				company.address(),
				company.createdAt(),
				company.updatedAt());
	}

	public static CompanyResponse from(Company company) {
		return new CompanyResponse(
				company.getCompanyId(),
				company.getHubId(),
				company.getName(),
				company.getCompanyType(),
				company.getStatus(),
				company.getSlackId(),
				company.getAddress(),
				company.getCreatedAt(),
				company.getUpdatedAt());
	}
}
