package com.hubEleven.stock.domain.exception;

import com.commonLib.common.code.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StockErrorCode implements StatusCode {
    STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "재고를 찾을 수 없습니다."),
    DECREASE_QUANTITY_INVALID(HttpStatus.BAD_REQUEST, "재고 감소 수량은 1 이상이어야 합니다."),
    RESTORE_QUANTITY_INVALID(HttpStatus.BAD_REQUEST, "재고 복원 수량은 1 이상이어야 합니다."),
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다."),
    INITIAL_QUANTITY_INVALID(HttpStatus.BAD_REQUEST, "초기 재고 수량은 0 이상이어야 합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return name();
    }
}
