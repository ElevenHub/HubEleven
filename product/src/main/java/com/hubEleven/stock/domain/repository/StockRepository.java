package com.hubEleven.stock.domain.repository;

import com.hubEleven.stock.domain.model.Stock;
import java.util.Optional;
import java.util.UUID;

public interface StockRepository {

	Stock save(Stock stock);

	Optional<Stock> findByProductId(UUID productId);
}
