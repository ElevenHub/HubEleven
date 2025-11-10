package com.hubEleven.stock.presentation.dto.response;

import com.hubEleven.stock.application.dto.StockResult;
import java.util.UUID;

public record StockResponse(
		UUID stockId,
		UUID productId,
		String productName,
		UUID hubId,
		UUID companyId,
		Integer quantity) {

	public static StockResponse from(StockResult stockResult) {
		return new StockResponse(
				stockResult.stockId(),
				stockResult.productId(),
				stockResult.productName(),
				stockResult.hubId(),
				stockResult.companyId(),
				stockResult.quantity());
	}
}
