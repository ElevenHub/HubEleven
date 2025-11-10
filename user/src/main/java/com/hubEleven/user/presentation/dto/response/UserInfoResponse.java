package com.hubEleven.user.presentation.dto.response;

import com.commonLib.common.code.StatusCode;
import com.hubEleven.user.domain.vo.Role;
import com.hubEleven.user.domain.vo.SignStatus;

import java.util.UUID;

public record UserInfoResponse(
		Long userId, String username, String name, String slackId, String phoneNumber, Role role, SignStatus status, UUID companyId) {}
