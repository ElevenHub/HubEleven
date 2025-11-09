package com.hubEleven.product.presentation.dto.response;

import com.hubEleven.product.application.dto.ProductResult;
import java.util.UUID;

public record ProductResponse(UUID productId, String name, UUID companyId, UUID hubId) {

	// Product Entity -> Response DTO 변환 용도
	public static ProductResponse from(ProductResult productResult) {
		return new ProductResponse(
				productResult.productId(),
				productResult.name(),
				productResult.companyId(),
				productResult.hubId());
	}
}
