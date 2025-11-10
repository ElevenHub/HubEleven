package com.hubEleven.product.application.dto;

import com.hubEleven.product.domain.model.Product;
import java.util.UUID;

public record ProductResult(UUID productId, String name, UUID companyId, UUID hubId) {

	public static ProductResult from(Product product) {
		return new ProductResult(
				product.getProductId(), product.getName(), product.getCompanyId(), product.getHubId());
	}
}
