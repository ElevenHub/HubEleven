package com.hubEleven.stock.application.dto;

import com.hubEleven.stock.domain.model.Stock;
import java.util.UUID;

// Service 계층의 반환용 DTO (Domain 엔티티와 API 응답 사이의 중간 계층)
public record StockResult(
		UUID stockId, UUID productId, String productName, UUID hubId, UUID companyId, int quantity) {

	public static StockResult from(Stock stock, String productName) {
		return new StockResult(
				stock.getStockId(),
				stock.getProductId(),
				productName, // productName 포함
				stock.getHubId(),
				stock.getCompanyId(),
				stock.getQuantity());
	}
}
