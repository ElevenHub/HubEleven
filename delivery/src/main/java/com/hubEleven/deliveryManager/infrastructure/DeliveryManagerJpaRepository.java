package com.hubEleven.deliveryManager.infrastructure;

import com.hubEleven.deliveryManager.domain.DeliveryManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeliveryManagerJpaRepository extends JpaRepository<DeliveryManager, Long> {

	List<DeliveryManager> findByHubId(UUID hubId);

	@Query(
			"""
		SELECT MAX(dm.deliveryOrder)
		FROM DeliveryManager dm
		WHERE (:hubId IS NULL AND dm.hubId IS NULL)
			OR (:hubId IS NOT NULL AND dm.hubId = :hubId)
""")
	Integer findMaxDeliveryOrderByHubId(@Param("hubId") UUID hubId);

	// 허브 담당자용 (hubId가 null인 경우)
	Optional<DeliveryManager> findFirstByHubIdIsNullOrderByLastDeliveryTimeAscDeliveryOrderAsc();

	// 회사 담당자용 (hubId 지정)
	Optional<DeliveryManager> findFirstByHubIdOrderByLastDeliveryTimeAscDeliveryOrderAsc(UUID hubId);
}
