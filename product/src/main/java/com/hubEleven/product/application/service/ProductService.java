package com.hubEleven.product.application.service;

import com.hubEleven.product.application.dto.ProductResult;
import com.hubEleven.product.presentation.dto.request.ProductRequests;

public interface ProductService {
    ProductResult create(ProductRequests.Create request);
}
