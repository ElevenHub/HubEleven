package com.hubEleven.product.infrastructure.repository;

import com.hubEleven.product.domain.model.Product;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaProductRepository extends JpaRepository<Product, UUID> {

    boolean existsByCompanyIdAndNameAndHubId(UUID companyId, String name, UUID hubId);

    // keyword가 null인 경우 모든 제품을 반환하고, 그렇지 않은 경우 이름에 keyword가 포함된 제품을 반환
    @Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR p.name LIKE %:keyword%) " +
            "AND p.deletedAt IS NULL")
	Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.productId = :productId AND p.deletedAt IS NULL")
    Optional<Product> findByIdNotDeleted(@Param("productId") UUID productId);
}
