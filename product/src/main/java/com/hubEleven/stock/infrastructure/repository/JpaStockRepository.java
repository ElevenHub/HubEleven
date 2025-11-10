package com.hubEleven.stock.infrastructure.repository;

import com.hubEleven.stock.domain.model.Stock;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaStockRepository extends JpaRepository<Stock, UUID> {

    Optional<Stock> findByProductId(UUID productId);
}
