package com.hubEleven.user.presentation.dto.response;

import com.hubEleven.user.domain.vo.Role;
import java.util.UUID;

public record SignupResponse(
		Long userId,
		String username,
		String name,
		String slackId,
		Role role,
		UUID companyId) {}
