package com.hubEleven.deliveryManager.domain;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;

public interface DeliveryManagerRepository {

	DeliveryManager save(DeliveryManager deliveryManager);

	Optional<DeliveryManager> findById(Long id);

	List<DeliveryManager> findAll();

	Page<DeliveryManager> findAll(Pageable pageable);

	Page<DeliveryManager> findAll(Specification<DeliveryManager> spec, Pageable pageable);

	Page<DeliveryManager> findAllByHubId(UUID hubId, Pageable pageable);

	Optional<DeliveryManager> findByDeliveryManagerIdAndDeletedAtIsNotNull(Long id);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Integer findMaxDeliveryOrderByHubId(UUID hubId);

	// 허브 담당자용 (hubId가 null인 경우)
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<DeliveryManager> findFirstByHubIdIsNullOrderByLastDeliveryTimeAscDeliveryOrderAsc();

	// 업체 담당자용 (hubId 지정)
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<DeliveryManager> findFirstByHubIdOrderByLastDeliveryTimeAscDeliveryOrderAsc(UUID hubId);

	boolean existsByHubIdAndDeliveryManagerId(UUID hubId, Long managerId);

	Integer findLastDeliveryOrderByHubIdForUpdate(@Param("hubId") UUID hubId);

	Integer findLastDeliveryOrderForNullHubForUpdate();
}
