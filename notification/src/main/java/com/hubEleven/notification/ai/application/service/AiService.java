package com.hubEleven.notification.ai.application.service;

import com.hubEleven.notification.ai.domain.model.AiRequestLog;
import java.util.UUID;

public interface AiService {
	AiRequestLog createRequested(UUID orderId, String prompt, String metadataText);

	void markSuccess(
			UUID logId,
			String finalDispatchDeadline,
			String messageBody,
			String rawResponse,
			String metadataText);

	void markFail(UUID logId, String failureReason, String metadataText);
}
