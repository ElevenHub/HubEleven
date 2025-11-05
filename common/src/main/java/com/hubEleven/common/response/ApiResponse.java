package com.hubEleven.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"code", "message", "result"})
public record ApiResponse<T>(
		String code, String message, @JsonInclude(JsonInclude.Include.NON_NULL) T result) {}
