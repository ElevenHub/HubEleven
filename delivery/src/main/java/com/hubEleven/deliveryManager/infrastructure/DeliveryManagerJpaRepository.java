package com.hubEleven.deliveryManager.infrastructure;

import com.hubEleven.deliveryManager.domain.DeliveryManager;
import com.hubEleven.deliveryManager.domain.DeliveryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryManagerJpaRepository extends JpaRepository<DeliveryManager, Long> {

	@Query(
			"SELECT MAX(dm.deliveryOrder) FROM DeliveryManager dm WHERE dm.deliveryType = :deliveryType")
	Integer findMaxDeliveryOrderByDeliveryType(DeliveryType deliveryType);
}
