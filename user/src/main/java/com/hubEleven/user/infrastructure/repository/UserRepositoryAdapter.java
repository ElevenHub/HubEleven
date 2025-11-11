package com.hubEleven.user.infrastructure.repository;

import com.hubEleven.user.domain.model.User;
import com.hubEleven.user.domain.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

	private final JpaUserRepository jpaUserRepository;

	@Override
	public User save(User user) {
		return jpaUserRepository.save(user);
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return jpaUserRepository.findByUsername(username);
	}

	@Override
	public boolean existsByUsername(String username) {
		return jpaUserRepository.existsByUsername(username);
	}

	@Override
	public Optional<User> findByIdAndNotDeleted(Long id) {
		return jpaUserRepository.findByIdAndDeletedAtIsNull(id);
	}

	@Override
	public Page<User> searchUsers(String keyword, Pageable pageable) {
		return jpaUserRepository.searchByKeyword(keyword, pageable);
	}
}
