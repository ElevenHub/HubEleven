package com.hubEleven.company.presentation.dto.response;

import com.hubEleven.company.application.dto.response.CompanyResult;
import com.hubEleven.company.domain.vo.CompanyStatus;
import com.hubEleven.company.domain.vo.CompanyType;
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
	public static CompanyResponse from(CompanyResult company) {
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
}
