package com.hubEleven.hub.application.command;

import java.util.UUID;

public record UpdateHubCommand(
		UUID hubId,
		String name,
		String address,
		Double latitude,
		Double longitude,
		String regionCode) {}
