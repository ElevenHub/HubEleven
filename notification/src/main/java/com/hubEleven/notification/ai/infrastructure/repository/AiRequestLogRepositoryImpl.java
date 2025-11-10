package com.hubEleven.notification.ai.infrastructure.repository;

import com.hubEleven.notification.ai.domain.model.AiRequestLog;
import com.hubEleven.notification.ai.domain.repository.AiRequestLogRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AiRequestLogRepositoryImpl implements AiRequestLogRepository {

	private final JpaAiRequestLogRepository repository;

	@Override
	public AiRequestLog save(AiRequestLog aiRequestLog) {
		return repository.save(aiRequestLog);
	}

	@Override
	public Optional<AiRequestLog> findById(UUID id) {
		return repository.findById(id);
	}

	@Override
	public boolean existsByOrderId(UUID orderId) {
		return repository.existsByOrderId(orderId);
	}

	@Override
	public Optional<AiRequestLog> findByOrderId(UUID orderId) {
		return repository.findByOrderId(orderId);
	}
}
