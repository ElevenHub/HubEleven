package com.hubEleven.order.infrastructure.repository;

import com.hubEleven.order.domain.model.Order;
import feign.Param;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaOrderRepository extends JpaRepository<Order, UUID> {

	// keyword가 null이거나 빈 문자열인 경우 모든 주문을 반환하고, 그렇지 않은 경우 노트에 keyword가 포함된 주문을 반환
	@Query(
			"SELECT o FROM Order o WHERE "
					+ "o.deletedAt IS NULL AND "
					+ "(:keyword IS NULL OR :keyword = '' OR o.note LIKE %:keyword%)")
	Page<Order> searchOrders(String keyword, Pageable pageable);

	@Query("SELECT o FROM Order o WHERE o.orderId = :ordertId AND o.deletedAt IS NULL")
	Optional<Order> findByIdNotDeleted(@Param("orderId") UUID orderId);
}
