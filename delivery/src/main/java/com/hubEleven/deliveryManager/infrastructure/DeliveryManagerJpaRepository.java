package com.hubEleven.deliveryManager.infrastructure;

import com.hubEleven.deliveryManager.domain.DeliveryManager;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeliveryManagerJpaRepository
		extends JpaRepository<DeliveryManager, Long>, JpaSpecificationExecutor<DeliveryManager> {

	Optional<DeliveryManager> findByDeliveryManagerIdAndDeletedAtIsNotNull(Long id);

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

	boolean existsByHubIdAndDeliveryManagerId(UUID hubId, Long managerId);

	Page<DeliveryManager> findAllByHubId(UUID hubId, Pageable pageable);

	// hubId NOT NULL 일 때: 마지막 delivery_order 를 FOR UPDATE로 잠근다.
	@Query(
			value =
					"""
				SELECT delivery_order
				FROM delivery_manager
				WHERE hub_id = :hubId
				ORDER BY delivery_order DESC
				LIMIT 1
				FOR UPDATE
				""",
			nativeQuery = true)
	Integer findLastDeliveryOrderByHubIdForUpdate(@Param("hubId") UUID hubId);

	// hubId IS NULL 일 때
	@Query(
			value =
					"""
				SELECT delivery_order
				FROM delivery_manager
				WHERE hub_id IS NULL
				ORDER BY delivery_order DESC
				LIMIT 1
				FOR UPDATE
				""",
			nativeQuery = true)
	Integer findLastDeliveryOrderForNullHubForUpdate();
}
