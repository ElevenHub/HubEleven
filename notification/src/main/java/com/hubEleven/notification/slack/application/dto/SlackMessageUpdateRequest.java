package com.hubEleven.notification.slack.application.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record SlackMessageUpdateRequest(
		@NotBlank(message = "메시지 내용은 필수 입력 항목입니다.") String message, LocalDateTime createdAt) {}
