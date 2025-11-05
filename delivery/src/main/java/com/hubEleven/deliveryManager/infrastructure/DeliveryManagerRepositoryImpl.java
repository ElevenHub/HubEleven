package com.hubEleven.deliveryManager.infrastructure;

import com.hubEleven.deliveryManager.domain.DeliveryManager;
import com.hubEleven.deliveryManager.domain.DeliveryManagerRepository;
import com.hubEleven.deliveryManager.domain.DeliveryType;
import org.springframework.stereotype.Repository;

@Repository
public class DeliveryManagerRepositoryImpl implements DeliveryManagerRepository {

	private final DeliveryManagerJpaRepository deliveryManagerJpaRepository;

	public DeliveryManagerRepositoryImpl(DeliveryManagerJpaRepository deliveryManagerJpaRepository) {
		this.deliveryManagerJpaRepository = deliveryManagerJpaRepository;
	}

	@Override
	public Integer findMaxDeliveryOrderByDeliveryType(DeliveryType deliveryType) {
		return deliveryManagerJpaRepository.findMaxDeliveryOrderByDeliveryType(deliveryType);
	}

	@Override
	public DeliveryManager save(DeliveryManager deliveryManager) {
		return deliveryManagerJpaRepository.save(deliveryManager);
	}
}
