package com.hubEleven.product.application.service;

import com.hubEleven.product.application.dto.ProductResult;
import com.hubEleven.product.presentation.dto.request.ProductRequests;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResult create(ProductRequests.Create request);

    Page<ProductResult> searchProducts(String keyword, Pageable pageable);
}
