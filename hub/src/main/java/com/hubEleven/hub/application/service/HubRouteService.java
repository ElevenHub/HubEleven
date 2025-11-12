package com.hubEleven.hub.application.service;

import com.hubEleven.hub.application.dto.RouteResult;
import java.util.List;
import java.util.UUID;

public interface HubRouteService {

	void generateAllRoutes();

	List<RouteResult> getAllRoutes();

	RouteResult findRoute(UUID departureHubId, UUID arrivalHubId);
}
