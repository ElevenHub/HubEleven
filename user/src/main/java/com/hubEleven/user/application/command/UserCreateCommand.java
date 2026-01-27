package com.hubEleven.user.application.command;

import com.hubEleven.user.domain.vo.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;

public record UserCreateCommand(

				String username,
				String password,
				String name,
				String slackId,
				String phoneNumber,
				Role role,
				UUID companyId) {}
