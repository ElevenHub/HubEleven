package com.hubEleven.stock.domain.repository;

import com.hubEleven.stock.domain.model.Stock;

public interface StockRepository {

	Stock save(Stock stock);
}
