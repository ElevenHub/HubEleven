package com.hubEleven.stock.presentation.controller;

import com.hubEleven.common.response.ApiResponse;
import com.hubEleven.common.response.ApiResponseEntity;
import com.hubEleven.stock.application.dto.StockResult;
import com.hubEleven.stock.application.service.StockService;
import com.hubEleven.stock.presentation.dto.request.StockRequests;
import com.hubEleven.stock.presentation.dto.response.StockResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
}
