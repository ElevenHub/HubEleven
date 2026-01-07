package com.hubEleven.delivery.application.port;

import com.hubEleven.delivery.domain.Delivery;
import java.util.UUID;

public interface DeliveryQueryPort {
	Delivery delivery(UUID id);
}
