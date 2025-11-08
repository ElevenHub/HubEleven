package com.hubEleven.deliveryRoute.domain;

import com.hubEleven.delivery.domain.Delivery;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRouteRepository extends JpaRepository<DeliveryRoute, UUID> {
	DeliveryRoute findByDelivery(Delivery delivery);

	Optional<DeliveryRoute> findByDeliveryAndToHubId(Delivery delivery, UUID toHubId);
}
