package com.hubEleven.hub.application.command;

public record CreateHubCommand(
		String name,
		String address,
		Double latitude,
		Double longitude,
		String regionCode,
		Long userId) {}
