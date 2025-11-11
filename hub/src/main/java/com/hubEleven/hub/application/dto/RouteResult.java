package com.hubEleven.hub.application.dto;

import com.hubEleven.hub.domain.model.HubRoute;
import java.util.List;
import java.util.UUID;

public record RouteResult(
		UUID hubRouteId,
		UUID fromHubId,
		UUID toHubId,
		Double totalDistance,
		Integer totalDuration,
		List<RouteSegmentResult> segments) {

	public static RouteResult from(HubRoute hubRoute) {
		List<RouteSegmentResult> segmentResults =
				hubRoute.getSegments().stream().map(RouteSegmentResult::from).toList();

		return new RouteResult(
				hubRoute.getHubRouteId(),
				hubRoute.getFromHubId(),
				hubRoute.getToHubId(),
				hubRoute.getTotalDistance(),
				hubRoute.getTotalDuration(),
				segmentResults);
	}
}
