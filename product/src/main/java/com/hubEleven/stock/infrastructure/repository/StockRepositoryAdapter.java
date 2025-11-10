package com.hubEleven.stock.infrastructure.repository;

import com.hubEleven.stock.domain.model.Stock;
import com.hubEleven.stock.domain.repository.StockRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StockRepositoryAdapter implements StockRepository {

	private final JpaStockRepository jpaStockRepository;

	@Override
	public Stock save(Stock stock) {
		return jpaStockRepository.save(stock);
	}

	@Override
	public Optional<Stock> findByProductId(UUID productId) {
		return jpaStockRepository.findByProductId(productId);
	}
}
