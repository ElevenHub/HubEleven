package com.hubEleven.company.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface SlackClient {
	@PostMapping("/v1/slack/messages")
	void send(@RequestBody SlackMessageRequest req);

	record SlackMessageRequest(String channelOruserId, String text) {}
}
