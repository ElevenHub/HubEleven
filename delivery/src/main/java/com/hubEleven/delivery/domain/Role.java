package com.hubEleven.delivery.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
	MASTER("관리자"),
	HUB_MANAGER("허브관리자"),
	DELIVERY_MANAGER("배송담당자"),
	COMPANY_MANAGER("업체관리자");

	private final String description;

	public static boolean isMaster(Role role) {
		return role == MASTER;
	}

	public static boolean isHubManager(Role role) {
		return role == HUB_MANAGER;
	}

	public static boolean isDeliveryManager(Role role) {
		return role == DELIVERY_MANAGER;
	}

	public static boolean isCompanyManager(Role role) {
		return role == COMPANY_MANAGER;
	}
}
