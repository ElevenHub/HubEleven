package com.hubEleven.hub.infrastructure.repository;

import com.hubEleven.hub.domain.model.Hub;
import com.hubEleven.hub.domain.repository.HubRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HubRepositoryAdapter implements HubRepository {

	private final JpaHubRepository jpaHubRepository;

	@Override
	public Hub save(Hub hub) {
		return jpaHubRepository.save(hub);
	}

	@Override
	public Optional<Hub> findById(UUID hubId) {
		return jpaHubRepository.findById(hubId);
	}

	@Override
	public List<Hub> findAll() {
		return jpaHubRepository.findAll();
	}

	@Override
	public List<Hub> findAllNotDeleted() {
		return jpaHubRepository.findAllNotDeleted();
	}

	@Override
	public boolean existsByName(String name) {
		return jpaHubRepository.existsByName(name);
	}
}
