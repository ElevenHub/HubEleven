package com.hubEleven.deliveryRoute.application;

import com.hubEleven.delivery.domain.Delivery;
import com.hubEleven.delivery.infrastructure.dto.HubRouteFeignResponseDto;
import com.hubEleven.deliveryRoute.application.dto.DeliveryRouteRequestDto;
import com.hubEleven.deliveryRoute.domain.DeliveryRoute;
import com.hubEleven.deliveryRoute.domain.DeliveryRouteRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryRouteService {
	DeliveryRouteRepository deliveryRouteRepository;

	// 배달 경로 생성
	@Transactional
	public DeliveryRoute creatRoute(
			HubRouteFeignResponseDto hubRoute, int seq, Long deliveryManagerId) {

		return DeliveryRoute.create(hubRoute, seq, deliveryManagerId);
	}

	// 배달 경로 수정
	@Transactional
	public DeliveryRoute updateRoute(
			UUID deliveryId, DeliveryRouteRequestDto deliveryRouteRequestDto) {
		DeliveryRoute deliveryRoute =
				deliveryRouteRepository
						.findByDeliveryIdAndToHubId(deliveryId, deliveryRouteRequestDto.toHubId())
						.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 배송 경로입니다."));

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
		DeliveryRoute deliveryRoute = deliveryRouteRepository.findByDelivery(delivery);
		deliveryRoute.delete(userId);
	}
}
