package com.hubEleven.product.domain.repository;

import com.hubEleven.product.domain.model.Product;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {
	Product save(Product product);

	boolean existsByCompanyIdAndNameAndHubId(UUID companyId, String name, UUID hubId);

	Page<Product> searchProducts(String keyword, Pageable pageable);

	Optional<Product> findById(UUID productId);

	void delete(Product product);
}
