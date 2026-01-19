package com.hubEleven.stock.presentation.controller;

import com.commonLib.common.response.ApiResponse;
import com.hubEleven.stock.application.dto.StockResult;
import com.hubEleven.stock.application.service.StockService;
import com.hubEleven.stock.presentation.dto.request.StockRequests;
import com.hubEleven.stock.presentation.dto.response.StockResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO @AuthenticationPrincipal 권한로직
@Tag(name = "Stock", description = "재고 API")
@RestController
@RequestMapping("/v1/stocks")
@RequiredArgsConstructor
public class StockController {

	private final StockService stockService;

	@Operation(summary = "재고 생성 API", description = "새로운 재고를 생성한다.")
	@PostMapping
	public ResponseEntity<ApiResponse<StockResponse>> create(
			@Valid @RequestBody StockRequests.Create request) {

		StockResult result = stockService.create(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(StockResponse.from(result)));
	}

	@Operation(summary = "재고 단건 조회 API", description = "상품 ID로 재고 상세 정보를 조회한다.")
	@GetMapping("/{productId}")
	public ResponseEntity<ApiResponse<StockResponse>> getStock(@PathVariable UUID productId) {

		StockResult result = stockService.getStockByProductId(productId);

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(StockResponse.from(result)));
	}

	@Operation(summary = "재고 감소 API", description = "상품 주문시 재고가 감소한다.")
	@PutMapping
	public ResponseEntity<ApiResponse<StockResponse>> decreaseStock(
			@Valid @RequestBody StockRequests.Decrease request) {

		// 재고 감소 로직 호출
		StockResult result = stockService.decreaseStock(request);

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(StockResponse.from(result)));
	}

	@Operation(summary = "재고 복원 API", description = "상품 취소시 재고가 복원된다.")
	@PutMapping("/restore")
	public ResponseEntity<ApiResponse<StockResponse>> restoreStock(
			@Valid @RequestBody StockRequests.Restore request) {

		// 재고 복원 로직 호출
		StockResult result = stockService.restoreStock(request);

		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(StockResponse.from(result)));
	}
}