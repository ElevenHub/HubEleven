package com.hubEleven.delivery.application.service;

import com.commonLib.common.exception.GlobalException;
import com.commonLib.common.response.ApiResponse;
import com.hubEleven.delivery.application.dto.DeliveryDetailResponseDto;
import com.hubEleven.delivery.application.dto.DeliveryRequestDto;
import com.hubEleven.delivery.application.dto.DeliveryResponseDto;
import com.hubEleven.delivery.domain.*;
import com.hubEleven.delivery.infrastructure.dto.*;
import com.hubEleven.delivery.infrastructure.service.*;
import com.hubEleven.deliveryManager.domain.DeliveryType;
import com.hubEleven.deliveryRoute.application.DeliveryRouteService;
import com.hubEleven.deliveryRoute.application.dto.DeliveryRouteRequestDto;
import com.hubEleven.deliveryRoute.application.dto.DeliveryRouteResponseDto;
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
public class DeliveryService {
	private final DeliveryRepository deliveryRepository;
	private final DeliveryRouteService deliveryRouteService;
	private final OrderFeignService orderFeignService;
	private final UserFeignService userFeignService;
	private final DeliveryManagerFeignService deliveryManagerFeignService;
	private final CompanyFeignService companyFeignService;
	private final HubRouteFeignService hubRouteFeignService;

	// Delivery 조회
	private Delivery delivery(UUID deliveryId) {
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

	// 배송 생성
	@Transactional
	public DeliveryResponseDto createDelivery(UUID orderId) {
		// 주문 ID를 기준으로 주문 정보 가져오기
        ApiResponse<OrderFeignResponseDto> order = orderFeignService.getOrderInfo(orderId);

		UUID fromCompanyId = order.result().requestorCompanyId(); // 요청업체
		UUID toCompanyId = order.result().recipientCompanyId(); // 수령 업체

		// 요청업체의 관리 허브 ID는 업체 테이블에 있음
		// 주문 정보에 있는 요청 업체 소속 허브를 출발 허브로 수령 업체 소속 허브를 도착 허브로 생각하고
		// 허브 경로에서 출발 허브 부터 도착 허브의 경로를 받아와서 그 갯수 만큼 배송 경로 생성
		UUID fromHubId = companyFeignService.getCompanyInfo(fromCompanyId).result().hubId();
		UUID toHubId = companyFeignService.getCompanyInfo(fromCompanyId).result().hubId();

		// 유저정보에서 수령인, 수령인 슬랙ID 받아오기
        ApiResponse<UserFeignResponseDto> toUser = userFeignService.getUserInfo(toCompanyId, Role.COMPANY_MANAGER);

		// 배송담당자에서 배송담당자 ID 받아오기
		ApiResponse<DeliveryManagerFeignResponseDto> deliveryManager =
				deliveryManagerFeignService.getDeliveryManagerInfo(orderId, toHubId, DeliveryType.COMPANY);

		// 배송 생성
		Delivery delivery =
				Delivery.create(
						orderId,
						DeliveryStatus.HUB_WAITHING,
						fromCompanyId,
						toCompanyId,
						toUser.result().name(),
						toUser.result().slackId(),
						deliveryManager.result().deliveryManagerId());

		// 허브 경로에 출발허브ID 와 도착허브ID를 넘기고 경로를 받는다.
		ApiResponse<HubRouteFeignResponseDto> hubRouteFeign =
				hubRouteFeignService.getRoute(fromHubId, toHubId);
		List<HubRouteSegmentResponseDto> hubRoute = hubRouteFeign.result().segments().stream().toList();
		for (int seq = 0; seq < hubRoute.size(); seq++) {
            HubRouteSegmentResponseDto deliveryRoute = hubRoute.get(seq);
			// 배송 담당자 ID
			ApiResponse<DeliveryManagerFeignResponseDto> hubDeliveryManager =
					deliveryManagerFeignService.getDeliveryManagerInfo(
							orderId, deliveryRoute.toHubId(), DeliveryType.HUB);
			Long deliveryManagerId = hubDeliveryManager.result().deliveryManagerId();

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
		Delivery delivery = delivery(deliveryId);

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

	// 배송 삭제
	@Transactional
	public void deleteDelivery(UUID deliveryId) {
		Delivery delivery = delivery(deliveryId);

		// todo userId JWT에서 받아오는 방식으로 변경 예정
		// 1. 배송 정보 삭제 (논리삭제)
		Long userId = 1234567890L;
		delivery.delete(userId);

		// 2. 배송 경로 삭제 (논리삭제)
		deliveryRouteService.deleteRoute(delivery, userId);
	}

	// 배송 경로 수정
	@Transactional
	public DeliveryRouteResponseDto updateDeliveryRoute(
			UUID deliveryId, DeliveryRouteRequestDto deliveryRouteRequestDto) {
		// 배달 존재 여부 확인
		Delivery delivery = delivery(deliveryId);

		// 배달 경로 수정
		DeliveryRoute deliveryRoute =
				deliveryRouteService.updateRoute(delivery, deliveryRouteRequestDto);
		return DeliveryRouteResponseDto.from(deliveryRoute);
	}
}
