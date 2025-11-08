package com.hubEleven.hub.application.dto;

import com.hubEleven.hub.domain.model.Hub;
import java.util.UUID;

public record HubResult(
		UUID hubId, String name, String address, Double latitude, Double longitude, String regionCode) {

	public static HubResult from(Hub hub) {
		return new HubResult(
				hub.getHubId(),
				hub.getName(),
				hub.getLocation().getAddress(),
				hub.getLocation().getLatitude(),
				hub.getLocation().getLongitude(),
				hub.getRegionCode());
	}
}
