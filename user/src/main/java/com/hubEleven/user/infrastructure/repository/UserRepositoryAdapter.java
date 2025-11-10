package com.hubEleven.user.infrastructure.repository;

import com.hubEleven.user.domain.model.User;
import com.hubEleven.user.domain.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
	public List<User> findAllByUsername(String username) {
		return jpaUserRepository.findAllByUsername(username);
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
	public Optional<User> findById(Long id) {
		return jpaUserRepository.findById(id);
	}

	@Override
	public void delete(User user) {
		jpaUserRepository.delete(user);
	}
}
