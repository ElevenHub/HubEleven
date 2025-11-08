package com.hubEleven.hub.presentation.dto.response;

import com.hubEleven.hub.application.dto.HubResult;
import java.util.List;
import java.util.UUID;

public record HubResponseDto(
		UUID hubId, String name, String address, Double latitude, Double longitude, String regionCode) {

	public static HubResponseDto from(HubResult result) {
		return new HubResponseDto(
				result.hubId(),
				result.name(),
				result.address(),
				result.latitude(),
				result.longitude(),
				result.regionCode());
	}

	public static List<HubResponseDto> fromList(List<HubResult> results) {
		return results.stream().map(HubResponseDto::from).toList();
	}
}
