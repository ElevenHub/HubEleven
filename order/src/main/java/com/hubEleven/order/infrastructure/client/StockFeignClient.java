package com.hubEleven.order.infrastructure.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "stock-service")
public interface StockFeignClient {

    // 재고 차감 (주문 생성 시)
    @PutMapping("/v1/stocks/decrease")
    void decreaseStock(@RequestBody StockDecreaseRequest request);

    // 재고 복원 (주문 취소 시)
    @PutMapping("/v1/stocks/restore")
    void restoreStock(@RequestBody StockRestoreRequest request);

    // 재고 차감 요청 DTO
    record StockDecreaseRequest(
            UUID productId,
            Long quantity
    ) {}

    // 재고 복원 요청 DTO
    record StockRestoreRequest(
            UUID productId,
            Long quantity
    ) {}

}
