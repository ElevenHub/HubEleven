package com.hubEleven.product.domain.repository;

import com.hubEleven.product.domain.model.Product;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);
    boolean existsByCompanyIdAndNameAndHubId(UUID companyId, String name, UUID hubId);
}
