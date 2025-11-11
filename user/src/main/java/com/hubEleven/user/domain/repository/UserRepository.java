package com.hubEleven.user.domain.repository;

import com.hubEleven.user.domain.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

	User save(User user);

	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);

	Optional<User> findByIdAndNotDeleted(Long id);

	Page<User> searchUsers(String keyword, Pageable pageable);
}
