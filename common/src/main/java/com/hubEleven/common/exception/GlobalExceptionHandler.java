package com.hubEleven.common.exception;

import static com.hubEleven.common.code.ErrorCode.*;

import com.hubEleven.common.response.ApiResponse;
import com.hubEleven.common.response.ApiResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(GlobalException.class)
	ResponseEntity<?> globalExceptionHandler(GlobalException e) {
		return ApiResponseEntity.onFailure(e.getErrorCode());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Object>> handleException(final Exception e) {
		return ApiResponseEntity.onFailure(SERVER_ERROR);
	}
}
