package com.hubEleven.company.presentation.dto.request;

import com.hubEleven.company.domain.vo.CompanyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CreateCompanyRequest(
		@NotNull(message = "허브 ID는 필수 입력 항목입니다.") UUID hubId,
		@NotBlank(message = "업체명은 필수 입력 항목입니다.") @Size(max = 150, message = "업체명은 150자 이하여야 합니다.")
				String name,
		@NotNull(message = "업체 타입은 필수 입력 항목입니다.") CompanyType type,
		@Size(max = 100, message = "Slack ID는 100자 이하여야 합니다.") String slackId,
		@Size(max = 300, message = "주소는 300자 이하여야 합니다.") String address) {}
