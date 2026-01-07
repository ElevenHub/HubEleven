package com.hubEleven.notification.ai.application.service;

import com.hubEleven.notification.ai.application.command.GenerateDispatchCommand;
import com.hubEleven.notification.ai.application.dto.response.GenerateMessageResponse;

public interface GenerateDispatchService {
	GenerateMessageResponse generate(GenerateDispatchCommand command);
}
