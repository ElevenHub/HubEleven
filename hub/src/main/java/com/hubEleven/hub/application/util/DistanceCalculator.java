package com.hubEleven.hub.application.util;

import org.springframework.stereotype.Component;

@Component
public class DistanceCalculator {

	private static final double EARTH_RADIUS = 6378.137;

	public Double calculate(Double lat1, Double lng1, Double lat2, Double lng2) {

		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);

		double a =
				Math.sin(dLat / 2) * Math.sin(dLat / 2)
						+ Math.cos(Math.toRadians(lat1))
								* Math.cos(Math.toRadians(lat2))
								* Math.sin(dLng / 2)
								* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return EARTH_RADIUS * c;
	}
}
