package com.hubEleven.hub.application.util;

import java.util.List;
import java.util.UUID;

public record RouteCalculationResult(
		List<UUID> path, List<Double> segmentDistances, Double totalDistance) {}
