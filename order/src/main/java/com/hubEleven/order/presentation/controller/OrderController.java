package com.hubEleven.order.presentation.controller;

import com.hubEleven.common.request.CommonPageRequest;
import com.hubEleven.common.response.ApiResponse;
import com.hubEleven.common.response.ApiResponseEntity;
import com.hubEleven.common.response.CommonPageResponse;
import com.hubEleven.common.utils.PagingUtils;
import com.hubEleven.order.application.dto.OrderResult;
import com.hubEleven.order.application.service.OrderService;
import com.hubEleven.order.presentation.dto.request.OrderRequests;
import com.hubEleven.order.presentation.dto.response.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping
    public ResponseEntity<ApiResponse<CommonPageResponse<OrderResponse>>> getOrders(CommonPageRequest request) {

        // Service 계층에서 Order 도메인 엔티티를 OrderResult DTO로 변환하여 조회
        Page<OrderResult> orders =
                orderService.searchOrders(
                        request.keyword(),
                        request.toPageable());

        // Application 계층의 OrderResult를 Presentation 계층의 OrderResponse로 변환
        CommonPageResponse<OrderResponse> response =
                PagingUtils.convert(orders, OrderResponse::from);

        return ApiResponseEntity.success(response);
    }
}
