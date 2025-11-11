package com.hubEleven.hub.infrastructure.repository;

import com.hubEleven.hub.domain.model.HubRoute;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaHubRouteRepository extends JpaRepository<HubRoute, UUID> {

	@Query("SELECT hr FROM HubRoute hr WHERE hr.deletedAt IS NULL")
	List<HubRoute> findAllNotDeleted();

	Optional<HubRoute> findByFromHubIdAndToHubId(UUID fromHubId, UUID toHubId);
}
