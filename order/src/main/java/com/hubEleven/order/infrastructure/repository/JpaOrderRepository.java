package com.hubEleven.order.infrastructure.repository;

import com.hubEleven.order.domain.model.Order;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaOrderRepository extends JpaRepository<Order, UUID> {

	@Query(
			"SELECT o FROM Order o WHERE "
					+ "o.deletedAt IS NULL AND "
					+ "(:keyword IS NULL OR :keyword = '' OR o.note LIKE %:keyword%)")
	Page<Order> searchOrders(String keyword, Pageable pageable);
}
