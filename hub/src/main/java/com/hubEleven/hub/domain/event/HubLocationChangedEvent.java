package com.hubEleven.hub.domain.event;

import com.hubEleven.hub.domain.vo.Location;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class HubLocationChangedEvent {

	private final UUID hubId;
	private final Location newLocation;
	private final LocalDateTime occurredAt;

	public HubLocationChangedEvent(UUID hubId, Location newLocation) {
		this.hubId = hubId;
		this.newLocation = newLocation;
		this.occurredAt = LocalDateTime.now();
	}
}
