package com.hubEleven.notification.ai.infrastructure.repository;

import com.hubEleven.notification.ai.domain.model.AiRequestLog;
import com.hubEleven.notification.ai.domain.repository.AiRequestLogRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AiRequestLogRepositoryAdapter implements AiRequestLogRepository {

	private final JpaAiRequestLogRepository jpa;

	@Override
	public AiRequestLog save(AiRequestLog aiRequestLog) {
		return jpa.save(aiRequestLog);
	}

	@Override
	public Optional<AiRequestLog> findById(UUID id) {
		return jpa.findById(id);
	}

	@Override
	public boolean existsByOrderId(UUID orderId) {
		return jpa.existsByOrderId(orderId);
	}

	@Override
	public Optional<AiRequestLog> findByOrderId(UUID orderId) {
		return jpa.findByOrderId(orderId);
	}
}
