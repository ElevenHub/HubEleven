package com.hubEleven.user.application.dto;

import com.hubEleven.user.domain.model.User;
import com.hubEleven.user.domain.vo.Role;
import com.hubEleven.user.domain.vo.SignStatus;
import java.util.UUID;

public record UserInfoResult(
		Long userId,
		String username,
		String password,
		String name,
		String slackId,
		String phoneNumber,
		Role role,
		SignStatus status,
		UUID companyId) {
	public static UserInfoResult from(User user) {
		return new UserInfoResult(
				user.getId(),
				user.getUsername(),
				user.getPassword(),
				user.getName(),
				user.getSlackId(),
				user.getPhoneNumber(),
				user.getRole(),
				user.getStatus(),
				user.getCompanyId());
	}
}
