package com.hubEleven.user.presentation.dto.request;

import com.hubEleven.user.domain.vo.SignStatus;
import jakarta.validation.constraints.NotNull;

public record UserStatusUpdateRequest(@NotNull(message = "상태는 필수입니다.") SignStatus status) {}
