package com.hubEleven.order.infrastructure.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "stock-service")
public interface StockFeignClient {

    // 재고 복원
    @PutMapping("/v1/stocks/restore")
    void restoreStock(@RequestBody StockRestoreRequest request);

    record StockRestoreRequest(
            UUID productId,
            Long quantity
    ) {}

}
