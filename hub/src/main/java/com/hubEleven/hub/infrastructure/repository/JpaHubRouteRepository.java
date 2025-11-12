package com.hubEleven.hub.infrastructure.repository;

import com.hubEleven.hub.domain.model.HubRoute;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaHubRouteRepository extends JpaRepository<HubRoute, UUID> {

	@Query("SELECT hr FROM HubRoute hr WHERE hr.deletedAt IS NULL")
	List<HubRoute> findAllNotDeleted();

	@Query(
			"SELECT hr FROM HubRoute hr "
					+ "WHERE hr.fromHubId = :fromHubId "
					+ "AND hr.toHubId = :toHubId "
					+ "AND hr.deletedAt IS NULL")
	Optional<HubRoute> findByFromHubIdAndToHubId(
			@Param("fromHubId") UUID fromHubId, @Param("toHubId") UUID toHubId);
}
