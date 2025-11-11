package com.hubEleven.order.infrastructure.repository;

import com.hubEleven.order.domain.model.Order;
import com.hubEleven.order.domain.repository.OrderRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

	private final JpaOrderRepository jpaOrderRepository;

	@Override
	public Order save(Order order) {
		return jpaOrderRepository.save(order);
	}

	@Override
	public Page<Order> searchOrders(String keyword, Pageable pageable) {
		return jpaOrderRepository.searchOrders(keyword, pageable);
	}

	@Override
	public Optional<Order> findById(UUID orderId) {
		return jpaOrderRepository.findById(orderId);
	}
}
