package com.hubEleven.hub.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class HubDeletedEvent {

	private final UUID hubId;
	private final LocalDateTime occurredAt;

	public HubDeletedEvent(UUID hubId) {
		this.hubId = hubId;
		this.occurredAt = LocalDateTime.now();
	}
}
