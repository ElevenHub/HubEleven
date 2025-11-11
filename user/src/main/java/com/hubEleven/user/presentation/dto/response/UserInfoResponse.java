package com.hubEleven.user.presentation.dto.response;

import com.hubEleven.user.application.dto.UserInfo;
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
	public static UserInfoResponse from(UserInfo userInfo) {
		return new UserInfoResponse(
				userInfo.userId(),
				userInfo.username(),
				userInfo.name(),
				userInfo.slackId(),
				userInfo.phoneNumber(),
				userInfo.role(),
				userInfo.status(),
				userInfo.companyId());
	}
}
