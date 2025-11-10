package com.hubEleven.user.domain.repository;

import com.hubEleven.user.domain.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

	User save(User user);

	List<User> findAllByUsername(String username);

	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);

	Optional<User> findById(Long id);

	void delete(User user);
}
