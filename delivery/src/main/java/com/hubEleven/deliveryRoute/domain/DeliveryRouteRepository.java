package com.hubEleven.deliveryRoute.domain;

import com.hubEleven.delivery.domain.Delivery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRouteRepository extends JpaRepository<DeliveryRoute, UUID> {
	List<DeliveryRoute> findByDelivery(Delivery delivery);

	Optional<DeliveryRoute> findByDeliveryAndToHubId(Delivery delivery, UUID toHubId);
}
