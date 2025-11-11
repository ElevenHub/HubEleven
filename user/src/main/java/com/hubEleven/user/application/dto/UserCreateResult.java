package com.hubEleven.user.application.dto;

import com.hubEleven.user.domain.model.User;
import com.hubEleven.user.domain.vo.Role;
import java.util.UUID;

public record UserCreateResult(
		Long userId,
		String username,
		String password,
		String name,
		String slackId,
		Role role,
		UUID companyId) {
	public static UserCreateResult from(User user) {
		return new UserCreateResult(
				user.getId(),
				user.getUsername(),
				user.getPassword(),
				user.getName(),
				user.getSlackId(),
				user.getRole(),
				user.getCompanyId());
	}
}
