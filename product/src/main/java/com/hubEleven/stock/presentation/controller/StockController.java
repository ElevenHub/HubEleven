package com.hubEleven.stock.presentation.controller;

import com.commonLib.common.response.ApiResponse;
import com.commonLib.common.response.ApiResponseEntity;
import com.hubEleven.stock.application.dto.StockResult;
import com.hubEleven.stock.application.service.StockService;
import com.hubEleven.stock.presentation.dto.request.StockRequests;
import com.hubEleven.stock.presentation.dto.response.StockResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO @AuthenticationPrincipal 권한로직
@RestController
@RequestMapping("/v1/stocks")
@RequiredArgsConstructor
public class StockController {

	private final StockService stockService;

	@PostMapping
	public ResponseEntity<ApiResponse<StockResponse>> create(
			@Valid @RequestBody StockRequests.Create request) {

		StockResult result = stockService.create(request);

		return ApiResponseEntity.success(StockResponse.from(result));
	}

	@GetMapping("/{productId}")
	public ResponseEntity<ApiResponse<StockResponse>> getStock(@PathVariable UUID productId) {

		StockResult result = stockService.getStockByProductId(productId);

		return ApiResponseEntity.success(StockResponse.from(result));
	}

    @PutMapping("/restore")
    public ResponseEntity<ApiResponse<StockResponse>> restoreStock(
            @Valid @RequestBody StockRequests.Restore request) {

        // 재고 복원 로직 호출
        StockResult result = stockService.restoreStock(request);

        return ApiResponseEntity.success(StockResponse.from(result));
    }
}
