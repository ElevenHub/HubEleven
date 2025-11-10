package com.hubEleven.order.presentation.controller;

import com.hubEleven.common.response.ApiResponse;
import com.hubEleven.common.response.ApiResponseEntity;
import com.hubEleven.order.application.dto.OrderResult;
import com.hubEleven.order.application.service.OrderService;
import com.hubEleven.order.presentation.dto.request.OrderRequests;
import com.hubEleven.order.presentation.dto.response.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> create(
            @Valid @RequestBody OrderRequests.Create request) {

        OrderResult result = orderService.create(request);

        return ApiResponseEntity.success(OrderResponse.from(result));
    }
}
