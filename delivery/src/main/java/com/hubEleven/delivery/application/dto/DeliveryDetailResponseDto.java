package com.hubEleven.delivery.application.dto;

import com.hubEleven.delivery.domain.Delivery;
import com.hubEleven.deliveryRoute.application.dto.DeliveryRouteResponseDto;
import com.hubEleven.deliveryRoute.domain.DeliveryRoute;
import java.util.List;

public record DeliveryDetailResponseDto(
		DeliveryResponseDto deliveryInfo, List<DeliveryRouteResponseDto> deliveryRoutes) {
	public static DeliveryDetailResponseDto from(
			Delivery delivery, List<DeliveryRoute> deliveryRoutes) {
		// 1. 배송 정보 -> DTO 변환
		DeliveryResponseDto deliveryInfo = DeliveryResponseDto.from(delivery);

		// 2. 배송 경로 -> DTO 변환
		List<DeliveryRouteResponseDto> deliveryRouteInfo =
				deliveryRoutes.stream()
						.map(DeliveryRouteResponseDto::from) // 각각을 DTO로 변환
						.toList();

		return new DeliveryDetailResponseDto(deliveryInfo, deliveryRouteInfo);
	}
}
