package com.hubEleven.hub.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Location {

	@Column(name = "address", nullable = false, length = 255)
	private String address;

	@Column(name = "latitude", nullable = false)
	private Double latitude;

	@Column(name = "longitude", nullable = false)
	private Double longitude;

	public static Location of(String address, Double latitude, Double longitude) {
		validateAddress(address);
		validateLatitude(latitude);
		validateLongitude(longitude);
		return new Location(address, latitude, longitude);
	}

	private static void validateAddress(String address) {
		if (address == null || address.isBlank()) {
			throw new IllegalArgumentException("주소는 필수 입력 항목입니다");
		}
		if (address.length() > 255) {
			throw new IllegalArgumentException("주소는 255자 이하여야 합니다.");
		}
	}

	private static void validateLatitude(Double latitude) {
		if (latitude == null) {
			throw new IllegalArgumentException("위도는 필수 입력 항목입니다.");
		}
		if (latitude < -90.0 || latitude > 90.0) {
			throw new IllegalArgumentException("위도는 -90 이상 90 이하이어야 합니다.");
		}
	}

	private static void validateLongitude(Double longitude) {
		if (longitude == null) {
			throw new IllegalArgumentException("경도는 필수 입력 항목입니다.");
		}
		if (longitude < -180.0 || longitude > 180.0) {
			throw new IllegalArgumentException("경도는 -180 이상 180 이하이어야 합니다.");
		}
	}
}
