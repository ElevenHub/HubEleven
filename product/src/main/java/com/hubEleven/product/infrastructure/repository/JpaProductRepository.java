package com.hubEleven.product.infrastructure.repository;

import com.hubEleven.product.domain.model.Product;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaProductRepository extends JpaRepository<Product, UUID> {
	boolean existsByCompanyIdAndNameAndHubId(UUID companyId, String name, UUID hubId);

	@Query(
			"SELECT p FROM Product p WHERE "
					+ "p.deletedAt IS NULL AND "
					+ "(:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword%)")
	Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);
}
