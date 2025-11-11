package com.hubEleven.stock.application.service;

import com.hubEleven.stock.application.dto.StockResult;
import com.hubEleven.stock.presentation.dto.request.StockRequests;
import java.util.UUID;

public interface StockService {

	StockResult create(StockRequests.Create request);

	StockResult getStockByProductId(UUID productId);

	StockResult decreaseStock(StockRequests.Decrease request);

	StockResult restoreStock(StockRequests.Restore request);
}
