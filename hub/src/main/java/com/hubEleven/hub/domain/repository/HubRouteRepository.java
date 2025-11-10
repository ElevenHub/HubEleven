package com.hubEleven.hub.domain.repository;

import com.hubEleven.hub.domain.model.HubRoute;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubRouteRepository {

	HubRoute save(HubRoute hubRoute);

	Optional<HubRoute> findById(UUID hubRouteId);

	Optional<HubRoute> findByFromHubIdAndToHubId(UUID fromHubId, UUID toHubId);

	List<HubRoute> findAll();

	List<HubRoute> findAllNotDeleted();
}
