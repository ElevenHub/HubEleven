package com.hubEleven.hub.presentation.dto.response;

import com.hubEleven.hub.application.dto.RouteResult;
import java.util.List;
import java.util.UUID;

public record HubRouteResponseDto(
		UUID hubRouteId,
		UUID fromHubId,
		UUID toHubId,
		Double totalDistance,
		Integer totalDuration,
		List<HubRouteSegmentResponseDto> segments) {

	public static HubRouteResponseDto from(RouteResult result) {
		List<HubRouteSegmentResponseDto> segmentResponses =
				result.segments().stream().map(HubRouteSegmentResponseDto::from).toList();

		return new HubRouteResponseDto(
				result.hubRouteId(),
				result.fromHubId(),
				result.toHubId(),
				result.totalDistance(),
				result.totalDuration(),
				segmentResponses);
	}

	public static List<HubRouteResponseDto> fromList(List<RouteResult> results) {
		return results.stream().map(HubRouteResponseDto::from).toList();
	}
}
