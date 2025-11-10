package com.hubEleven.order.infrastructure.client;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductFeignClient {

    @GetMapping("/v1/products/{productId}")
    ProductResponse getProduct(@PathVariable UUID productId);

    record ProductResponse(UUID productId, String name) {}
}
