package com.hubEleven.order.presentation.controller;

import com.commonLib.common.request.CommonPageRequest;
import com.commonLib.common.response.ApiResponse;
import com.commonLib.common.response.CommonPageResponse;
import com.commonLib.common.utils.PagingUtils;
import com.hubEleven.order.application.dto.OrderResult;
import com.hubEleven.order.application.service.OrderService;
import com.hubEleven.order.presentation.dto.request.OrderRequests;
import com.hubEleven.order.presentation.dto.response.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO @AuthenticationPrincipal 권한로직
@Tag(name = "Order", description = "주문 API")
@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@Operation(summary = "주문 생성 API", description = "새로운 주문을 생성한다.")
	@PostMapping
	public ResponseEntity<ApiResponse<OrderResponse>> create(
			@Valid @RequestBody OrderRequests.Create request) {

		OrderResult result = orderService.create(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success(OrderResponse.from(result)));
	}

	@Operation(summary = "주문 전체  조회 API", description = "주문 전체 목록을 조회한다.")
	@GetMapping
	public ResponseEntity<ApiResponse<CommonPageResponse<OrderResponse>>> getOrders(
			CommonPageRequest request) {

		// Service 계층에서 Order 도메인 엔티티를 OrderResult DTO로 변환하여 조회
		Page<OrderResult> orders = orderService.searchOrders(request.keyword(), request.toPageable());

		// Application 계층의 OrderResult를 Presentation 계층의 OrderResponse로 변환
		CommonPageResponse<OrderResponse> response = PagingUtils.convert(orders, OrderResponse::from);

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}

	@Operation(summary = "주문 단건 조회 API", description = "주문 ID로 상품을 조회한다.")
	@GetMapping("/{orderId}")
	public ResponseEntity<ApiResponse<OrderResponse>> getOrderDetail(@PathVariable UUID orderId) {

		OrderResult result = orderService.getOrderDetail(orderId);

		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.success(OrderResponse.from(result)));
	}

	@Operation(summary = "주문 수정 API", description = "주문 정보를 수정한다.")
	@PatchMapping("/{orderId}")
	public ResponseEntity<ApiResponse<OrderResponse>> updateOrder(
			@PathVariable UUID orderId, @Valid @RequestBody OrderRequests.Update request) {

		OrderResult result = orderService.updateOrder(orderId, request);

		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.success(OrderResponse.from(result)));
	}

	@Operation(summary = "주문 삭제 API", description = "주문을 삭제한다.")
	@DeleteMapping("/{orderId}")
	public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable UUID orderId, Long userId) {

		orderService.deleteOrder(orderId, userId);

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
	}

	@Operation(summary = "주문 검색 API", description = "키워드 기반으로 주문을 검색한다.")
	@GetMapping("/search")
	public ResponseEntity<ApiResponse<CommonPageResponse<OrderResponse>>> searchOrders(
			CommonPageRequest request) {

		Page<OrderResult> orders = orderService.searchOrders(request.keyword(), request.toPageable());

		CommonPageResponse<OrderResponse> response = PagingUtils.convert(orders, OrderResponse::from);

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}

	@Operation(summary = "주문 취소 API", description = "주문을 취소한다.")
	@PostMapping("/{orderId}/cancel")
	public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable UUID orderId, Long userId) {

		orderService.cancelOrder(orderId, userId);

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
	}
}
