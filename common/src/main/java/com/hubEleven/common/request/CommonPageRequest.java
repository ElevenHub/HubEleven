package com.hubEleven.common.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record CommonPageRequest(
		int page, int size, SortType sortType, Sort.Direction direction, String keyword) {
	public CommonPageRequest {
		if (page < 0) {
			page = 0;
		}

		if (size != 10 && size != 30 && size != 50) {
			size = 10;
		}

		if (sortType == null) {
			sortType = SortType.CREATED_AT;
		}

		if (direction == null) {
			direction = Sort.Direction.DESC;
		}
	}

	public Pageable toPageable() {
		return PageRequest.of(page, size, Sort.by(direction, sortType.getFieldName()));
	}

	public enum SortType {
		CREATED_AT("createdAt"),
		UPDATED_AT("updatedAt");

		private final String fieldName;

		SortType(String fieldName) {
			this.fieldName = fieldName;
		}

		public String getFieldName() {
			return fieldName;
		}
	}
}
