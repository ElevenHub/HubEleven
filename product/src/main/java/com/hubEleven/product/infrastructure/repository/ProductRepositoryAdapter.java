package com.hubEleven.product.infrastructure.repository;

import com.hubEleven.product.domain.model.Product;
import com.hubEleven.product.domain.repository.ProductRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

	private final JpaProductRepository jpaProductRepository;

	@Override
	public Product save(Product product) {
		return jpaProductRepository.save(product);
	}

	@Override
	public boolean existsByCompanyIdAndNameAndHubId(UUID companyId, String name, UUID hubId) {
		return jpaProductRepository.existsByCompanyIdAndNameAndHubId(companyId, name, hubId);
	}

	@Override
	public Page<Product> searchProducts(String keyword, Pageable pageable) {
		return jpaProductRepository.searchProducts(keyword, pageable);
	}

	@Override
	public Optional<Product> findById(UUID productId) {
		return jpaProductRepository.findById(productId);
	}

	@Override
	public void delete(Product product) {
		jpaProductRepository.delete(product);
	}
}
