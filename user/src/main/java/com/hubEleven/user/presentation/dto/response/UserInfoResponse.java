package com.hubEleven.user.presentation.dto.response;

import com.hubEleven.user.application.dto.UserInfoResult;
import com.hubEleven.user.domain.vo.Role;
import com.hubEleven.user.domain.vo.SignStatus;
import java.util.UUID;

public record UserInfoResponse(
		Long userId,
		String username,
		String name,
		String slackId,
		String phoneNumber,
		Role role,
		SignStatus status,
		UUID companyId) {
	public static UserInfoResponse from(UserInfoResult userInfoResult) {
		return new UserInfoResponse(
				userInfoResult.userId(),
				userInfoResult.username(),
				userInfoResult.name(),
				userInfoResult.slackId(),
				userInfoResult.phoneNumber(),
				userInfoResult.role(),
				userInfoResult.status(),
				userInfoResult.companyId());
	}
}
