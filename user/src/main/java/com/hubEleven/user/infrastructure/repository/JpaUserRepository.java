package com.hubEleven.user.infrastructure.repository;

import com.hubEleven.user.domain.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long> {

	List<User> findAllByUsername(String username);

	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);
}
