package com.hubEleven.company.domain.model;

import static com.hubEleven.company.domain.exception.CompanyErrorCode.COMPANY_NOT_FOUND;

import com.commonLib.common.exception.GlobalException;
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
		throw new GlobalException(COMPANY_NOT_FOUND);
	}
}
