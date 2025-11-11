package com.hubEleven.order.application.service;

import com.commonLib.common.exception.GlobalException;
import com.hubEleven.order.application.dto.OrderResult;
import com.hubEleven.order.domain.exception.OrderErrorCode;
import com.hubEleven.order.domain.model.Order;
import com.hubEleven.order.domain.repository.OrderRepository;
import com.hubEleven.order.infrastructure.client.CompanyFeignClient;
import com.hubEleven.order.infrastructure.client.CompanyFeignClient.CompanyResponse;
import com.hubEleven.order.infrastructure.client.ProductFeignClient;
import com.hubEleven.order.infrastructure.client.ProductFeignClient.ProductResponse;
import com.hubEleven.order.infrastructure.client.StockFeignClient;
import com.hubEleven.order.presentation.dto.request.OrderRequests;
import feign.FeignException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final ProductFeignClient productFeignClient;
	private final CompanyFeignClient companyFeignClient;
	private final StockFeignClient stockFeignClient;

	// 주문 존재 여부 확인 메서드
	private Order validateOrderExists(UUID orderId) {
		return orderRepository
				.findById(orderId)
				.orElseThrow(() -> new GlobalException(ORDER_NOT_FOUND));
	}

	@Override
	@Transactional
	public OrderResult create(OrderRequests.Create request) {

		// 요청 업체 존재 여부 확인
		try {
			CompanyResponse company = companyFeignClient.getCompany(request.requestorCompanyId());
			if (company == null) {
				throw new GlobalException(REQUESTOR_COMPANY_NOT_FOUND);
			}
		} catch (FeignException.NotFound e) {
			throw new GlobalException(REQUESTOR_COMPANY_NOT_FOUND);
		}

		// 수령 업체 존재 여부 확인
		try {
			CompanyResponse company = companyFeignClient.getCompany(request.recipientCompanyId());
			if (company == null) {
				throw new GlobalException(RECIPIENT_COMPANY_NOT_FOUND);
			}
		} catch (FeignException.NotFound e) {
			throw new GlobalException(RECIPIENT_COMPANY_NOT_FOUND);
		}

		// 상품 존재 여부 확인
		try {
			ProductResponse product = productFeignClient.getProduct(request.productId());
			if (product == null) {
				throw new GlobalException(PRODUCT_NOT_FOUND);
			}
		} catch (FeignException.NotFound e) {
			throw new GlobalException(PRODUCT_NOT_FOUND);
		}

		// 재고 차감
		try {
			StockFeignClient.StockDecreaseRequest stockRequest =
					new StockFeignClient.StockDecreaseRequest(request.productId(), request.quantity());
			stockFeignClient.decreaseStock(stockRequest);

		} catch (FeignException e) {
			// 재고 부족 or Stock 서비스 오류
			throw new GlobalException(OrderErrorCode.STOCK_INSUFFICIENT);
		}

		// 주문 생성 및 저장
		Order order =
				Order.create(
						request.requestorCompanyId(),
						request.recipientCompanyId(),
						request.productId(),
						request.deliveryId(),
						request.quantity(),
						request.note());

		Order savedOrder = orderRepository.save(order);

		return OrderResult.from(savedOrder);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<OrderResult> searchOrders(String keyword, Pageable pageable) {

		Page<Order> orders = orderRepository.searchOrders(keyword, pageable);

		return orders.map(OrderResult::from);
	}

	@Override
	@Transactional(readOnly = true)
	public OrderResult getOrderDetail(UUID orderId) {

		Order order = validateOrderExists(orderId);

		return OrderResult.from(order);
	}

	@Override
	@Transactional
	public OrderResult updateOrder(UUID orderId, OrderRequests.Update request) {

		Order order = validateOrderExists(orderId);

		order.update(request.quantity(), request.note());

		return OrderResult.from(orderRepository.save(order));
	}

	@Override
	@Transactional
	public void deleteOrder(UUID orderId, Long userId) {

		Order order = validateOrderExists(orderId);

		// 논리 삭제 처리
		order.delete(userId);
	}

	@Override
	@Transactional
	public void cancelOrder(UUID orderId, Long userId) {

		Order order = validateOrderExists(orderId);

		// 재고 복원 처리
		try {
			StockFeignClient.StockRestoreRequest request =
					new StockFeignClient.StockRestoreRequest(order.getProductId(), order.getQuantity());
			stockFeignClient.restoreStock(request);

		} catch (FeignException e) {
			throw new GlobalException(STOCK_RESTORE_FAILED);
		}

		// 취소건도 delete_at 필드에 기록
		order.delete(userId);
	}
}
