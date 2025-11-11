package com.hubEleven.delivery.infrastructure.dto;

import java.util.List;
import java.util.UUID;

public record HubRouteFeignResponseDto(
		UUID hubRouteId, UUID fromHubId, UUID toHubId, Double distance,
        Integer totalDuration,
        List<HubRouteSegmentResponseDto> segments) {}
