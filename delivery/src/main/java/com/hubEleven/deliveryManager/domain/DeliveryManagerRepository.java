package com.hubEleven.deliveryManager.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryManagerRepository {

	DeliveryManager save(DeliveryManager deliveryManager);

	Optional<DeliveryManager> findById(Long id);

	List<DeliveryManager> findAll();

	Integer findMaxDeliveryOrderByHubId(UUID hubId);

	// 허브 담당자용 (hubId가 null인 경우)
	Optional<DeliveryManager> findFirstByHubIdIsNullOrderByLastDeliveryTimeAscDeliveryOrderAsc();

	// 회사 담당자용 (hubId 지정)
	Optional<DeliveryManager> findFirstByHubIdOrderByLastDeliveryTimeAscDeliveryOrderAsc(UUID hubId);
}
