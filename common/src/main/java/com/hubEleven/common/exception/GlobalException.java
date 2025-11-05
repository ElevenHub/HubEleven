package com.hubEleven.common.exception;

import com.hubEleven.common.code.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GlobalException extends RuntimeException {

	private final StatusCode errorCode;
}
