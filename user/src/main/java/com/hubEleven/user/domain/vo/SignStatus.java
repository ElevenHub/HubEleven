package com.hubEleven.user.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SignStatus {
	PENDING("승인대기"),
	APPROVED("승인완료"),
	REJECTED("승인거절");

	private final String description;
}
