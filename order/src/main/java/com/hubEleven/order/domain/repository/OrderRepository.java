package com.hubEleven.order.domain.repository;

import com.hubEleven.order.domain.model.Order;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepository {

    Order save(Order order);

    Page<Order> searchOrders(String keyword, Pageable pageable);

    Optional<Order> findById(UUID orderId);
}
