package com.hubEleven.hub.application.dto;

import com.hubEleven.hub.domain.model.HubRouteSegment;
import java.util.UUID;

public record RouteSegmentResult(
		UUID segmentId,
		UUID fromHubId,
		UUID toHubId,
		Integer sequence,
		Double distance,
		Integer duration) {

	public static RouteSegmentResult from(HubRouteSegment segment) {
		return new RouteSegmentResult(
				segment.getSegmentId(),
				segment.getFromHubId(),
				segment.getToHubId(),
				segment.getSequence(),
				segment.getDistance(),
				segment.getDuration());
	}
}
