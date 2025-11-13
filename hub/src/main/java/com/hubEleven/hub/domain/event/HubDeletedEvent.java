package com.hubEleven.hub.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class HubDeletedEvent {

	private final UUID hubId;
	private final Long userId;
	private final LocalDateTime occurredAt;

	public HubDeletedEvent(UUID hubId, Long userId) {
		this.hubId = hubId;
		this.userId = userId;
		this.occurredAt = LocalDateTime.now();
	}
}
