package com.hubEleven.user.infrastructure.repository;

import com.hubEleven.user.domain.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long> {

	List<User> findAllByUsername(String username);

	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);

	@Query("SELECT u FROM User u WHERE u.id = :id AND u.deletedAt IS NULL")
	Optional<User> findByIdAndDeletedAtIsNull(@Param("id") Long id);

	@Query(
			"SELECT u FROM User u WHERE "
					+ "u.deletedAt IS NULL AND "
					+ "(:keyword IS NULL OR :keyword = '' OR "
					+ "u.username LIKE %:keyword% OR "
					+ "u.name LIKE %:keyword%)")
	Page<User> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
