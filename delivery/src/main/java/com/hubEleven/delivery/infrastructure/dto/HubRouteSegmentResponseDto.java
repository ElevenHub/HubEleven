package com.hubEleven.delivery.infrastructure.dto;

import java.util.UUID;

public record HubRouteSegmentResponseDto(
		UUID segmentId,
		UUID fromHubId,
		UUID toHubId,
		Integer sequence,
		Double distance,
		Long duration) {

}
