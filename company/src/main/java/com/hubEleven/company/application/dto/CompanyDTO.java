package com.hubEleven.company.application.dto;

import com.hubEleven.company.domain.model.Company;
import com.hubEleven.company.domain.model.CompanyStatus;
import com.hubEleven.company.domain.model.CompanyType;
import java.time.LocalDateTime;
import java.util.UUID;

public record CompanyDTO(
		UUID companyId,
		UUID hubId,
		String name,
		CompanyType companyType,
		CompanyStatus status,
		String slackId,
		String address,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
	public static CompanyDTO from(Company company) {
		return new CompanyDTO(
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
