package com.hubEleven.delivery.presentation;

import com.commonLib.common.exception.GlobalException;
import com.commonLib.common.response.ApiResponse;
import com.commonLib.common.response.ApiResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.commonLib.common.code.ErrorCode.SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GlobalException.class)
    public Mono<ResponseEntity<ApiResponse<Object>>> handleGlobalException(GlobalException e, ServerWebExchange exchange) { // 💡 ServerWebExchange 추가

        // GlobalException 내부의 ErrorCode에서 HttpStatus를 가져옵니다.
        HttpStatus status = e.getErrorCode().getHttpStatus(); // DeliveryErrorCode는 HttpStatus를 가집니다.

        // HTTP 상태 코드 설정
        exchange.getResponse().setStatusCode(status);

        // Mono로 감싸서 반환 (WebFlux 환경에 적합)
        // ApiResponseEntity.onFailure가 ErrorResponse를 반환한다고 가정
        return Mono.just(ApiResponseEntity.onFailure(e.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(final Exception e) {
        return ApiResponseEntity.onFailure(SERVER_ERROR);
    }
}
