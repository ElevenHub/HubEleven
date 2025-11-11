package com.hubEleven.stock.application.service;

import com.hubEleven.stock.application.dto.StockResult;
import com.hubEleven.stock.presentation.dto.request.StockRequests;
import com.hubEleven.stock.presentation.dto.request.StockRequests.Create;
import jakarta.validation.Valid;
import java.util.UUID;

public interface StockService {

	StockResult create(@Valid Create request);

	StockResult getStockByProductId(UUID productId);

    StockResult restoreStock(StockRequests.Restore request);
}
