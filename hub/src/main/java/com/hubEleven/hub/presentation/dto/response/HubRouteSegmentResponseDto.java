package com.hubEleven.hub.presentation.dto.response;

import com.hubEleven.hub.application.dto.RouteSegmentResult;
import java.util.UUID;

public record HubRouteSegmentResponseDto(
		UUID segmentId,
		UUID fromHubId,
		UUID toHubId,
		Integer sequence,
		Double distance,
		Integer duration) {

	public static HubRouteSegmentResponseDto from(RouteSegmentResult result) {
		return new HubRouteSegmentResponseDto(
				result.segmentId(),
				result.fromHubId(),
				result.toHubId(),
				result.sequence(),
				result.distance(),
				result.duration());
	}
}
