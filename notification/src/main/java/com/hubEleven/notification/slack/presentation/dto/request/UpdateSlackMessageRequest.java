package com.hubEleven.notification.slack.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record UpdateSlackMessageRequest(
		@NotBlank(message = "메시지 내용은 필수 입력 항목입니다.") String message, LocalDateTime createdAt) {}
