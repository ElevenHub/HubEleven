package com.hubEleven.order.domain.repository;

import com.hubEleven.order.domain.model.Order;

public interface OrderRepository {

    Order save(Order order);
}
