package com.hubEleven.company.presentation.dto.request;

import com.hubEleven.company.domain.vo.CompanyType;
import jakarta.validation.constraints.Size;

public record UpdateCompanyRequest(
		@Size(max = 150, message = "업체명은 150자 이하여야 합니다.") String name,
		CompanyType type,
		@Size(max = 100, message = "Slack ID는 100자 이하여야 합니다.") String slackId,
		@Size(max = 300, message = "주소는 300자 이하여야 합니다.") String address) {}
