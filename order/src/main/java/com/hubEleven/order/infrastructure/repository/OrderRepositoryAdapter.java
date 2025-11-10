package com.hubEleven.order.infrastructure.repository;

import com.hubEleven.order.domain.model.Order;
import com.hubEleven.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    @Override
    public Order save(Order order) {return jpaOrderRepository.save(order);}

}
