package com.hubEleven.stock.application.service;

import static com.hubEleven.product.domain.exception.ProductErrorCode.PRODUCT_NOT_FOUND;
import static com.hubEleven.stock.domain.exception.StockErrorCode.STOCK_NOT_FOUND;

import com.commonLib.common.exception.GlobalException;
import com.hubEleven.product.domain.model.Product;
import com.hubEleven.product.domain.repository.ProductRepository;
import com.hubEleven.stock.application.dto.StockResult;
import com.hubEleven.stock.domain.model.Stock;
import com.hubEleven.stock.domain.repository.StockRepository;
import com.hubEleven.stock.presentation.dto.request.StockRequests;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;

    private Product getProductOrThrow(UUID productId) {
        return productRepository
                .findByIdNotDeleted(productId)
                .orElseThrow(() -> new GlobalException(PRODUCT_NOT_FOUND));
    }

    private Stock getStockOrThrow(UUID productId) {
        return stockRepository
                .findByProductId(productId)
                .orElseThrow(() -> new GlobalException(STOCK_NOT_FOUND));
    }

    @Override
    @Transactional
    public StockResult create(StockRequests.Create request) {

        Product product = getProductOrThrow(request.productId());

        Stock stock =
                Stock.create(
                        request.productId(),
                        request.companyId(),
                        request.hubId(),
                        request.quantity());

        Stock savedStock = stockRepository.save(stock);

        return StockResult.from(savedStock, product.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public StockResult getStockByProductId(UUID productId) {

        Product product = getProductOrThrow(productId);

        Stock stock = getStockOrThrow(productId);

        return StockResult.from(stock, product.getName());
    }

    @Override
    @Transactional
    public StockResult decreaseStock(StockRequests.Decrease request) {

        Product product = getProductOrThrow(request.productId());

        Stock stock = getStockOrThrow(request.productId());

        stock.decreaseQuantity(request.quantity());

        return StockResult.from(stock, product.getName());
    }

    @Override
    @Transactional
    public StockResult restoreStock(StockRequests.Restore request) {

        Product product = getProductOrThrow(request.productId());

        Stock stock = getStockOrThrow(request.productId());

        stock.restoreQuantity(request.quantity());

        Stock updatedStock = stockRepository.save(stock);

        return StockResult.from(updatedStock, product.getName());
    }
}
