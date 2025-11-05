package com.hubEleven.deliveryManager.domain;

public interface DeliveryManagerRepository {

	// deliveryType에 따른 최대 순번값 조회
	Integer findMaxDeliveryOrderByDeliveryType(DeliveryType type);

	DeliveryManager save(DeliveryManager deliveryManager);
}
