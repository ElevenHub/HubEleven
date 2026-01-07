package com.hubEleven.delivery.application.query.service;

import com.commonLib.common.exception.GlobalException;
import com.hubEleven.delivery.application.dto.DeliveryDetailResponseDto;
import com.hubEleven.delivery.application.dto.DeliveryResponseDto;
import com.hubEleven.delivery.application.port.DeliveryQueryPort;
import com.hubEleven.delivery.domain.Delivery;
import com.hubEleven.delivery.domain.DeliveryErrorCode;
import com.hubEleven.delivery.domain.DeliveryRepository;
import com.hubEleven.delivery.domain.DeliveryStatus;
import com.hubEleven.deliveryRoute.domain.DeliveryRoute;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryQueryService implements DeliveryQueryPort {
	private final DeliveryRepository deliveryRepository;

	// Delivery 조회
	@Override
	public Delivery delivery(UUID deliveryId) {
		return deliveryRepository
				.findById(deliveryId)
				.orElseThrow(() -> new GlobalException(DeliveryErrorCode.DELIVERY_NOT_FOUND));
	}

	// 배송 검색
	public Page<DeliveryResponseDto> searchDelivery(
			UUID deliveryId,
			UUID orderId,
			DeliveryStatus status,
			UUID fromHubId,
			UUID toHubId,
			String recipientName,
			String recipientSlackId,
			String deliveryManagerId,
			int page,
			int size,
			String sort) {
		// 1. Pageable 객체 생성 (정렬 기준 : sort)
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());

		// 2. Specification를 사용하여 동적 쿼리
		Specification<Delivery> spec =
				(root, query, cb) -> {
					List<Predicate> predicates = new ArrayList<>();
					if (deliveryId != null) predicates.add(cb.equal(root.get("id"), deliveryId));
					if (orderId != null) predicates.add(cb.equal(root.get("orderId"), orderId));
					if (status != null) predicates.add(cb.equal(root.get("status"), status));
					if (fromHubId != null) predicates.add(cb.equal(root.get("fromHubId"), fromHubId));
					if (toHubId != null) predicates.add(cb.equal(root.get("toHubId"), toHubId));
					if (recipientName != null)
						predicates.add(cb.like(root.get("recipientName"), "%" + recipientName + "%"));
					if (recipientSlackId != null)
						predicates.add(cb.like(root.get("recipientSlackId"), "%" + recipientSlackId + "%"));
					if (deliveryManagerId != null)
						predicates.add(cb.equal(root.get("deliveryManagerId"), deliveryManagerId));
					return cb.and(predicates.toArray(new Predicate[0]));
				};

		// 3. 목록 조회
		Page<Delivery> deliveryPage = deliveryRepository.findAll(spec, pageable);
		return deliveryPage.map(DeliveryResponseDto::from);
	}

	// 배송 목록 조회
	public Page<DeliveryResponseDto> getDeliveryList(int page, int size, String sort) {
		// 1. Pageable 객체 생성 (정렬 기준 : sort)
		Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());

		// 2. Repository에서 페이지 & 정렬하여 배달 목록 조회
		Page<Delivery> deliveryPage = deliveryRepository.findAll(pageable);

		// 3. Entity -> DTO 반환
		return deliveryPage.map(DeliveryResponseDto::from);
	}

	// 배송 상세 조회
	@Transactional
	public DeliveryDetailResponseDto getDelivery(UUID deliveryId) {
		// 배송 정보 조회
		Delivery deliveryInfo = delivery(deliveryId);

		// 배송 경로 조회
		List<DeliveryRoute> deliveryRouteList = deliveryInfo.getDeliveryRoutes();
		if (deliveryRouteList == null || deliveryRouteList.isEmpty()) {
			throw new GlobalException(DeliveryErrorCode.DELIVERY_ROUTE_NOT_FOUND);
		}

		// 엔티티 -> DTO로 변환해서 반환
		return DeliveryDetailResponseDto.from(delivery(deliveryId), deliveryRouteList);
	}
}
