package com.hubEleven.deliveryRoute.application;

import com.commonLib.common.exception.GlobalException;
import com.hubEleven.delivery.domain.Delivery;
import com.hubEleven.delivery.domain.DeliveryErrorCode;
import com.hubEleven.delivery.infrastructure.dto.HubRouteSegmentResponseDto;
import com.hubEleven.deliveryRoute.application.dto.DeliveryRouteRequestDto;
import com.hubEleven.deliveryRoute.domain.DeliveryRoute;
import com.hubEleven.deliveryRoute.domain.DeliveryRouteRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryRouteService {
	private final DeliveryRouteRepository deliveryRouteRepository;

	// 배달 경로 생성
	@Transactional
	public DeliveryRoute creatRoute(
			HubRouteSegmentResponseDto hubRoute, int seq, Long deliveryManagerId) {

		return DeliveryRoute.create(hubRoute, seq, deliveryManagerId);
	}

	// 배달 경로 수정
	@Transactional
	public DeliveryRoute updateRoute(
			Delivery delivery, DeliveryRouteRequestDto deliveryRouteRequestDto) {

		DeliveryRoute deliveryRoute =
				deliveryRouteRepository
						.findByDeliveryAndToHubId(delivery, deliveryRouteRequestDto.toHubId())
						.orElseThrow(() -> new GlobalException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

		deliveryRoute.update(
				deliveryRouteRequestDto.seq(),
				deliveryRouteRequestDto.toHubId(),
				deliveryRouteRequestDto.deliveryManagerId(),
				deliveryRouteRequestDto.status(),
				deliveryRouteRequestDto.expectedDistance(),
				deliveryRouteRequestDto.expectedDuration(),
				deliveryRouteRequestDto.actualDistance(),
				deliveryRouteRequestDto.actualDuration());

		return deliveryRoute;
	}

	// 배달 경로 삭제
	@Transactional
	public void deleteRoute(Delivery delivery, Long userId) {
		List<DeliveryRoute> deliveryRoute = deliveryRouteRepository.findByDelivery(delivery);
		if (deliveryRoute == null || deliveryRoute.isEmpty()) {
			throw new GlobalException(DeliveryErrorCode.DELIVERY_ROUTE_NOT_FOUND);
		}

		for (DeliveryRoute route : deliveryRoute) {
			route.delete(userId);
		}
	}
}
