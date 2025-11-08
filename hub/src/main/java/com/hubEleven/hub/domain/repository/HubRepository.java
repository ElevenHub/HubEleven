package com.hubEleven.hub.domain.repository;

import com.hubEleven.hub.domain.model.Hub;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubRepository {

	Hub save(Hub hub);

	Optional<Hub> findById(UUID hubId);

	List<Hub> findAll();

	List<Hub> findAllNotDeleted();

	boolean existsByName(String name);
}
