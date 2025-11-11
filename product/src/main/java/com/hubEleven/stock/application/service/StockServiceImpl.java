package com.hubEleven.stock.application.service;

import static com.hubEleven.product.domain.exception.ProductErrorCode.PRODUCT_DELETED;
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

	// 상품 존재 여부 확인 메서드
	private Product validateProductExists(UUID productId) {
		return productRepository
				.findById(productId)
				.orElseThrow(() -> new GlobalException(PRODUCT_NOT_FOUND));
	}

	// 재고 존재 여부 확인 메서드
	private Stock validateStockExists(UUID productId) {
		return stockRepository
				.findByProductId(productId)
				.orElseThrow(() -> new GlobalException(STOCK_NOT_FOUND));
	}

	@Override
	@Transactional
	public StockResult create(StockRequests.Create request) {
		// 상품 존재 여부 확인
		Product product = validateProductExists(request.productId());

		Stock stock =
				Stock.create(request.quantity(), request.productId(), request.companyId(), request.hubId());

		Stock savedStock = stockRepository.save(stock);

		return StockResult.from(savedStock, product.getName());
	}

	@Override
	@Transactional(readOnly = true)
	public StockResult getStockByProductId(UUID productId) {

		// 상품 존재 여부 확인
		Product product = validateProductExists(productId);

		// 삭제된 상품인지 확인
		if (product.isDeleted()) {
			throw new GlobalException(PRODUCT_DELETED);
		}

		// 재고 조회
		Stock stock = validateStockExists(productId);

		return StockResult.from(stock, product.getName());
	}

	@Override
	@Transactional
	public StockResult decreaseStock(StockRequests.Decrease request) {

		// 상품 존재 여부 확인
		Product product = validateProductExists(request.productId());

		// 재고 조회
		Stock stock = validateStockExists(request.productId());

		// 재고 차감
		stock.decreaseQuantity(request.quantity()); // ToDo : 재고 부족 예외 처리

		return StockResult.from(stock, product.getName());
	}

	@Override
	@Transactional
	public StockResult restoreStock(StockRequests.Restore request) {
		// 상품 존재 여부 확인
		Product product = validateProductExists(request.productId());

		// 재고 조회
		Stock stock = validateStockExists(request.productId());

		// 재고 복원 로직
		stock.restoreQuantity(request.quantity()); // ToDo : 재고 복원 예외 처리

		Stock updatedStock = stockRepository.save(stock);

		return StockResult.from(updatedStock, product.getName());
	}
}
