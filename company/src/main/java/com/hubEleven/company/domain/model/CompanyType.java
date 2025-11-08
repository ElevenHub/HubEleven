package com.hubEleven.company.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CompanyType {
	PRODUCER("생산업체"),
	RECEIVER("수령업체");

	private final String displayName;

	CompanyType(String displayName) {
		this.displayName = displayName;
	}

	@JsonValue
	public String getDisplayName() {
		return displayName;
	}

	@JsonCreator
	public static CompanyType fromDisplayName(String displayName) {
		for (CompanyType type : values()) {
			if (type.getDisplayName().equals(displayName)) {
				return type;
			}
		}
		throw new IllegalArgumentException("지원하지 않는 업체입니다.");
	}
}
