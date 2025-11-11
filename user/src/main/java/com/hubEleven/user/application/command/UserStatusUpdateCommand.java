package com.hubEleven.user.application.command;

import com.hubEleven.user.domain.vo.SignStatus;
import jakarta.validation.constraints.NotNull;

public record UserStatusUpdateCommand(@NotNull Long userId, @NotNull SignStatus status) {}
