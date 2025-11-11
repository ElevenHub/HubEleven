package com.hubEleven.order.application.service;

import com.hubEleven.order.application.dto.OrderResult;
import com.hubEleven.order.presentation.dto.request.OrderRequests;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

	OrderResult create(OrderRequests.Create request);

	Page<OrderResult> searchOrders(String keyword, Pageable pageable);

	OrderResult getOrderDetail(UUID orderId);

	OrderResult updateOrder(UUID orderId, OrderRequests.Update request);

	void deleteOrder(UUID orderId, Long userId);

	void cancelOrder(UUID orderId, Long userId);
}
