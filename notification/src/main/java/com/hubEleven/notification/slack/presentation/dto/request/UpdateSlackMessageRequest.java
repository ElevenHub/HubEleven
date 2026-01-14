package com.hubEleven.notification.slack.presentation.dto.request;

import com.hubEleven.notification.slack.application.command.UpdateSlackMessageCommand;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record UpdateSlackMessageRequest(
		@NotBlank(message = "메시지 내용은 필수 입력 항목입니다.")
		String message
) {
	public UpdateSlackMessageCommand toCommand(UUID messageId) {
		return new UpdateSlackMessageCommand(messageId, message);
	}
}
