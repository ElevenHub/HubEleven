package com.hubEleven.notification.ai.application.port;

import com.hubEleven.notification.ai.domain.vo.DispatchResult;

public interface DispatchAiPort {
	DispatchResult generate(String prompt);
}
