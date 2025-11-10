package com.hubEleven.order.domain.repository;

import com.hubEleven.order.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepository {

    Order save(Order order);

    Page<Order> searchOrders(String keyword, Pageable pageable);
}
