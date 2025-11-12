package com.hubEleven.hub.infrastructure.repository;

import com.hubEleven.hub.domain.model.HubRoute;
import com.hubEleven.hub.domain.repository.HubRouteRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HubRouteRepositoryAdapter implements HubRouteRepository {

	private final JpaHubRouteRepository jpaHubRouteRepository;

	@Override
	public HubRoute save(HubRoute hubRoute) {
		return jpaHubRouteRepository.save(hubRoute);
	}

	@Override
	public Optional<HubRoute> findById(UUID hubRouteId) {
		return jpaHubRouteRepository.findById(hubRouteId);
	}

	@Override
	public Optional<HubRoute> findByFromHubIdAndToHubId(UUID fromHubId, UUID toHubId) {
		return jpaHubRouteRepository.findByFromHubIdAndToHubId(fromHubId, toHubId);
	}

	@Override
	public List<HubRoute> findAll() {
		return jpaHubRouteRepository.findAll();
	}

	@Override
	public List<HubRoute> findAllNotDeleted() {
		return jpaHubRouteRepository.findAllNotDeleted();
	}

	@Override
	public void softDeleteAll(Long deletedBy) {
		List<HubRoute> routes = jpaHubRouteRepository.findAllNotDeleted();
		routes.forEach(route -> route.softDelete(deletedBy));
		jpaHubRouteRepository.saveAll(routes);
	}
}
