package com.hubEleven.delivery.application.command.service;

import com.hubEleven.delivery.application.dto.DeliveryRequestDto;
import com.hubEleven.delivery.application.dto.DeliveryResponseDto;
import com.hubEleven.delivery.application.port.DeliveryQueryPort;
import com.hubEleven.delivery.domain.Delivery;
import com.hubEleven.delivery.domain.DeliveryRepository;
import com.hubEleven.delivery.domain.DeliveryStatus;
import com.hubEleven.delivery.domain.Role;
import com.hubEleven.delivery.infrastructure.dto.*;
import com.hubEleven.delivery.infrastructure.service.*;
import com.hubEleven.deliveryManager.domain.DeliveryType;
import com.hubEleven.deliveryRoute.application.DeliveryRouteService;
import com.hubEleven.deliveryRoute.application.dto.DeliveryRouteRequestDto;
import com.hubEleven.deliveryRoute.application.dto.DeliveryRouteResponseDto;
import com.hubEleven.deliveryRoute.domain.DeliveryRoute;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryCommandService {
	// 쓰기
	private final DeliveryRepository deliveryRepository;
	private final DeliveryRouteService deliveryRouteService;

	// 인터페이스 주입
	private final DeliveryQueryPort queryPort;

	private final OrderFeignService orderFeignService;
	private final CompanyFeignService companyFeignService;
	private final UserFeignService userFeignService;
	private final DeliveryManagerFeignService deliveryManagerFeignService;
	private final HubRouteFeignService hubRouteFeignService;

	// 배송 생성
	@Transactional
	public DeliveryResponseDto createDelivery(UUID orderId) {
		// 주문 ID를 기준으로 주문 정보 가져오기
		OrderFeignResponseDto order = orderFeignService.getOrderInfo(orderId).data();

		UUID fromCompanyId = order.requestorCompanyId(); // 요청업체
		UUID toCompanyId = order.recipientCompanyId(); // 수령 업체

		// 요청업체의 관리 허브 ID는 업체 테이블에 있음
		// 주문 정보에 있는 요청 업체 소속 허브를 출발 허브로 수령 업체 소속 허브를 도착 허브로 생각하고
		// 허브 경로에서 출발 허브 부터 도착 허브의 경로를 받아와서 그 갯수 만큼 배송 경로 생성
		UUID fromHubId = companyFeignService.getCompanyInfo(fromCompanyId).data().hubId();
		UUID toHubId = companyFeignService.getCompanyInfo(fromCompanyId).data().hubId();

		// 유저정보에서 수령인, 수령인 슬랙ID 받아오기
		UserFeignResponseDto toUser = userFeignService.getUserInfo(toCompanyId, Role.COMPANY_MANAGER).data();

		// 배송담당자에서 배송담당자 ID 받아오기
		DeliveryManagerFeignResponseDto deliveryManager =
				deliveryManagerFeignService.getDeliveryManagerInfo(orderId, toHubId, DeliveryType.COMPANY).data();

		// 배송 생성
		Delivery delivery =
				Delivery.create(
						orderId,
						DeliveryStatus.HUB_WAITHING,
						fromCompanyId,
						toCompanyId,
						toUser.name(),
						toUser.slackId(),
						deliveryManager.deliveryManagerId());

		// 허브 경로에 출발허브ID 와 도착허브ID를 넘기고 경로를 받는다.
		HubRouteFeignResponseDto hubRouteFeign = hubRouteFeignService.getRoute(fromHubId, toHubId).data();
		List<HubRouteSegmentResponseDto> hubRoute = hubRouteFeign.segments().stream().toList();
		for (int seq = 0; seq < hubRoute.size(); seq++) {
			HubRouteSegmentResponseDto deliveryRoute = hubRoute.get(seq);
			// 배송 담당자 ID
			DeliveryManagerFeignResponseDto hubDeliveryManager =
					deliveryManagerFeignService.getDeliveryManagerInfo(
							orderId, deliveryRoute.toHubId(), DeliveryType.HUB).data();
			Long deliveryManagerId = hubDeliveryManager.deliveryManagerId();

			// 배송 경로 생성 요청
			DeliveryRoute route = deliveryRouteService.creatRoute(deliveryRoute, seq, deliveryManagerId);

			delivery.addRoute(route);
		}

		// 배송 저장 할댸 배송 경로까지 함께 저장
		deliveryRepository.save(delivery);

		// 엔티티 -> DTO로 변환해서 반환
		return DeliveryResponseDto.from(delivery);
	}

	// 배송 수정
	@Transactional
	public DeliveryResponseDto updateDelivery(UUID deliveryId, DeliveryRequestDto requestDto) {
		// 배송 정보 조회
		Delivery delivery = queryPort.delivery(deliveryId);

		// 배송 수정
		delivery.update(
				requestDto.status(),
				requestDto.fromHubId(),
				requestDto.toHubId(),
				requestDto.recipientName(),
				requestDto.recipientSlackId(),
				requestDto.deliveryManagerId());

		return DeliveryResponseDto.from(delivery);
	}

	// 배송 경로 수정
	@Transactional
	public DeliveryRouteResponseDto updateDeliveryRoute(
			UUID deliveryId, DeliveryRouteRequestDto deliveryRouteRequestDto) {
		// 배달 존재 여부 확인
		Delivery delivery = queryPort.delivery(deliveryId);

		// 배달 경로 수정
		DeliveryRoute deliveryRoute =
				deliveryRouteService.updateRoute(delivery, deliveryRouteRequestDto);
		return DeliveryRouteResponseDto.from(deliveryRoute);
	}

	// 배송 삭제
	@Transactional
	public void deleteDelivery(UUID deliveryId, Long userId) {
		Delivery delivery = queryPort.delivery(deliveryId);

		// 1. 배송 정보 삭제 (논리삭제)
		delivery.delete(userId);

		// 2. 배송 경로 삭제 (논리삭제)
		deliveryRouteService.deleteRoute(delivery, userId);
	}
}
