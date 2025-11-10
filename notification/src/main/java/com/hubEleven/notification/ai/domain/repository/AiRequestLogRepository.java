package com.hubEleven.notification.ai.domain.repository;

import com.hubEleven.notification.ai.domain.model.AiRequestLog;
import java.util.Optional;
import java.util.UUID;

public interface AiRequestLogRepository {
	AiRequestLog save(AiRequestLog aiRequestLog);

	Optional<AiRequestLog> findById(UUID id);

	boolean existsByOrderId(UUID orderId);

	Optional<AiRequestLog> findByOrderId(UUID orderId);
}
