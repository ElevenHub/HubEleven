package com.hubEleven.user.presentation.dto.request;

import com.hubEleven.user.domain.vo.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UserUpdateRequest(
		@NotBlank(message = "이름은 필수입니다.") String name,
		@NotBlank(message = "슬랙 ID는 필수입니다.") String slackId,
		@NotBlank(message = "휴대폰번호는 필수입니다.") String phoneNumber,
		@NotNull(message = "권한은 필수입니다.") Role role,
		@NotNull(message = "업체 id는 필수입니다.") UUID companyId) {}
