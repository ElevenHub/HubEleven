package com.hubEleven.deliveryManager.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryType {
	HUB("허브 배송 담당자"),
	COMPANY("업체 배송 담당자");

	private final String description;

	public boolean isHubManager() {
		return this == HUB;
	}

	public boolean isCompanyManager() {
		return this == COMPANY;
	}
}
