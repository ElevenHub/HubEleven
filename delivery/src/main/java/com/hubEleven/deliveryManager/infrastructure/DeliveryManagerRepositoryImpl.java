package com.hubEleven.deliveryManager.infrastructure;

import com.hubEleven.deliveryManager.domain.DeliveryManager;
import com.hubEleven.deliveryManager.domain.DeliveryManagerRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public class DeliveryManagerRepositoryImpl implements DeliveryManagerRepository {

	private final DeliveryManagerJpaRepository deliveryManagerJpaRepository;

	public DeliveryManagerRepositoryImpl(DeliveryManagerJpaRepository deliveryManagerJpaRepository) {
		this.deliveryManagerJpaRepository = deliveryManagerJpaRepository;
	}

	@Override
	public DeliveryManager save(DeliveryManager deliveryManager) {
		return deliveryManagerJpaRepository.save(deliveryManager);
	}

	@Override
	public Optional<DeliveryManager> findById(Long id) {
		return deliveryManagerJpaRepository.findById(id);
	}

	@Override
	public List<DeliveryManager> findAll() {
		return deliveryManagerJpaRepository.findAll();
	}

	@Override
	public Page<DeliveryManager> findAll(Pageable pageable) {
		return deliveryManagerJpaRepository.findAll(pageable);
	}

	// ✅ Specification 기반 조회 위임
	@Override
	public Page<DeliveryManager> findAll(Specification<DeliveryManager> spec, Pageable pageable) {
		return deliveryManagerJpaRepository.findAll(spec, pageable);
	}

	@Override
	public Page<DeliveryManager> findAllByHubId(UUID hubId, Pageable pageable) {
		return deliveryManagerJpaRepository.findAllByHubId(hubId, pageable);
	}

	@Override
	public Optional<DeliveryManager> findByDeliveryManagerIdAndDeletedAtIsNotNull(Long id) {
		return deliveryManagerJpaRepository.findByDeliveryManagerIdAndDeletedAtIsNotNull(id);
	}

	@Override
	public Integer findMaxDeliveryOrderByHubId(UUID hubId) {
		return deliveryManagerJpaRepository.findMaxDeliveryOrderByHubId(hubId);
	}

	@Override
	public Optional<DeliveryManager>
			findFirstByHubIdIsNullOrderByLastDeliveryTimeAscDeliveryOrderAsc() {
		return deliveryManagerJpaRepository
				.findFirstByHubIdIsNullOrderByLastDeliveryTimeAscDeliveryOrderAsc();
	}

	@Override
	public Optional<DeliveryManager> findFirstByHubIdOrderByLastDeliveryTimeAscDeliveryOrderAsc(
			UUID hubId) {
		return deliveryManagerJpaRepository.findFirstByHubIdOrderByLastDeliveryTimeAscDeliveryOrderAsc(
				hubId);
	}

	@Override
	public boolean existsByHubIdAndDeliveryManagerId(UUID hubId, Long managerId) {
		return deliveryManagerJpaRepository.existsByHubIdAndDeliveryManagerId(hubId, managerId);
	}

	@Override
	public Integer findLastDeliveryOrderByHubIdForUpdate(UUID hubId) {
		return deliveryManagerJpaRepository.findLastDeliveryOrderByHubIdForUpdate(hubId);
	}

	@Override
	public Integer findLastDeliveryOrderForNullHubForUpdate() {
		return deliveryManagerJpaRepository.findLastDeliveryOrderByHubIdForUpdate(null);
	}
}
