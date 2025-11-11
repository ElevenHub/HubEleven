package com.hubEleven.user.application.command;

import com.hubEleven.user.domain.vo.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UserUpdateCommand(
		@NotNull Long userId,
		@NotBlank String name,
		@NotBlank String slackId,
		@NotBlank String phoneNumber,
		@NotNull Role role,
		@NotNull UUID companyId) {}
