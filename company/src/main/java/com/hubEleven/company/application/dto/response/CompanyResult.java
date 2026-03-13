package com.hubEleven.company.application.dto.response;

import com.hubEleven.company.domain.model.Company;
import com.hubEleven.company.domain.vo.CompanyStatus;
import com.hubEleven.company.domain.vo.CompanyType;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record CompanyResult(
		UUID companyId,
		UUID hubId,
		String name,
		CompanyType companyType,
		CompanyStatus status,
		String slackId,
		String address,
		LocalDateTime createdAt,
		LocalDateTime updatedAt)
		implements Serializable {
	private static final long serialVersionUID = 1L;

	public static CompanyResult from(Company company) {
		return new CompanyResult(
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
