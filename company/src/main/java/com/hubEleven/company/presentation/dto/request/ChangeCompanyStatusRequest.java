package com.hubEleven.company.presentation.dto.request;

import com.hubEleven.company.domain.vo.CompanyStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeCompanyStatusRequest(
		@NotNull(message = "업체 상태는 필수 입력 항목입니다.")
		CompanyStatus status) {
}
