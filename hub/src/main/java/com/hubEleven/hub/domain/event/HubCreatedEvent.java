package com.hubEleven.hub.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class HubCreatedEvent {

	private final UUID hubId;
	private final Long userId;
	private final LocalDateTime occurredAt;

	public HubCreatedEvent(UUID hubId, Long userId) {
		this.userId = userId;
		this.hubId = hubId;
		this.occurredAt = LocalDateTime.now();
	}
}
