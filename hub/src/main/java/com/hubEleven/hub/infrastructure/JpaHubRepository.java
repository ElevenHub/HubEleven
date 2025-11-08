package com.hubEleven.hub.infrastructure;

import com.hubEleven.hub.domain.model.Hub;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaHubRepository extends JpaRepository<Hub, UUID> {

	@Query("SELECT h FROM Hub h WHERE h.deletedAt IS NULL")
	List<Hub> findAllNotDeleted();

	boolean existsByName(String name);
}
