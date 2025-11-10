package com.hubEleven.order.infrastructure.repository;

import com.hubEleven.order.domain.model.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderRepository extends JpaRepository<Order, UUID> {

}
