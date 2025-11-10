package com.hubEleven.stock.application.service;

import com.hubEleven.stock.application.dto.StockResult;
import com.hubEleven.stock.presentation.dto.request.StockRequests.Create;
import jakarta.validation.Valid;

public interface StockService {

	StockResult create(@Valid Create request);
}
