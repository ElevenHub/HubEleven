package com.hubEleven.product.infrastructure.repository;

import com.hubEleven.product.domain.model.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<Product, UUID> {
    boolean existsByCompanyIdAndNameAndHubId(UUID companyId, String name, UUID hubId);
}
