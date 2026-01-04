package com.hubEleven.notification.ai.domain.service;

import com.hubEleven.notification.ai.domain.vo.DispatchContext;

public interface PromptService {
	String buildDispatchGuidancePrompt(DispatchContext ctx);
}
