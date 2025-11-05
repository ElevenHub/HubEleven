package com.hubEleven.deliveryManager.application;

import com.hubEleven.deliveryManager.domain.DeliveryManager;
import com.hubEleven.deliveryManager.domain.DeliveryManagerRepository;
import com.hubEleven.deliveryManager.domain.DeliveryType;
import com.hubEleven.deliveryManager.presentation.dto.request.DeliveryManagerCreateRequestDto;
import com.hubEleven.deliveryManager.presentation.dto.response.DeliveryManagerResponseDto;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class DeliveryManagerService {

	private final DeliveryManagerRepository deliveryManagerRepository;

	public DeliveryManagerService(DeliveryManagerRepository deliveryManagerRepository) {
		this.deliveryManagerRepository = deliveryManagerRepository;
	}

	// 배송 담당자 생성
	@Transactional
	public DeliveryManagerResponseDto createDeliveryManager(
			DeliveryManagerCreateRequestDto createRequestDto) {
		// plan : 요청 dto로 유저id만 받고, 유저서비스에서 id로 정보를 조회해 나머지 엔티티 필드 채우기(hubID, slackId, DeliveryType,
		// deliveryOrder(배송순번))
		/**
		 * TODO : 1. 생성 권한이 있는지 확인하기 (로그인한 유저가 마스터 혹은 허브관리자인가?) -> 컨트롤러에서 2. RequestDto에서 id 꺼내와
		 * FeignClient로 User-Service 서버에서 user정보 받아오기 (user정보에 id, slackId, 권한, 소속업체(허브)ID 존재) 3. 위에서
		 * 받아온 user정보에서 id 존재 여부 조회 후, role이 배송담당자인지 확인, 소속업체 확인하기(허브면 허브가 존재하는가?) 3-1 요청한 id가 이미 배달기사로
		 * 등록된 아이디가 아닌지 검증 3-2 소속업체 id가 NULL이면 허브배송 담당자, NULL이 아니면 업체담당자(소속업체 id가 허브id가 맞는지도 검증해야 할까?)
		 * (배송담당자는 소속이 없거나, 허브소속이니까, 유저정보에서 배송담당자 권한이면서 소속이 있다면 그 소속은 허브라고 생각하고 검증을 안해도 되는가? -> 유저에서 검증
		 * ) 4. 배송순번 부여하기 (전략: 배송담당자 타입별로 순번을 각각 부여해야하므로, 배송담당자 타입으로 조회하여 순번최댓값을 가져오고, +1 증가시켜 부여) ->
		 * 동시성 문제 생각할 것
		 */
		log.info("[Service] 배달 담당자 생성 요청");
		// 임시 데이터----------------------------
		Long id = createRequestDto.deliveryManagerId();
		UUID hubId = UUID.randomUUID();
		String slackId = "slack001";
		DeliveryType deliveryType = DeliveryType.COMPANY;
		// ----------------------------------

		// TODO: DB에 이미 존재하는 id 인지 확인
		Integer maxOrder;

		// 배송순번 부여
		int deliveryOrder = setDeliveryOrder(deliveryType);

		DeliveryManager deliveryManager =
				DeliveryManager.create(id, hubId, slackId, deliveryType, deliveryOrder);

		deliveryManagerRepository.save(deliveryManager);

		log.info("[Service] 배달 담당자 생성 - DB 저장 성공");

		DeliveryManagerResponseDto responseDto = DeliveryManagerResponseDto.from(deliveryManager);

		return responseDto;
	}

	public int setDeliveryOrder(DeliveryType deliveryType) {
		Integer maxOrder;
		if (deliveryType == DeliveryType.HUB) {
			maxOrder = deliveryManagerRepository.findMaxDeliveryOrderByDeliveryType(DeliveryType.HUB);
			int nextOrder = maxOrder == null ? 1 : maxOrder + 1;
			log.info("현재 마지막 순번 : {} , 생성된 배달 순번 : {}", maxOrder, nextOrder);
			return nextOrder;
		} else {
			maxOrder = deliveryManagerRepository.findMaxDeliveryOrderByDeliveryType(DeliveryType.COMPANY);
			int nextOrder = maxOrder == null ? 1 : maxOrder + 1;
			log.info("현재 마지막 순번 : {} , 생성된 배달 순번 : {}", maxOrder, nextOrder);

			return nextOrder;
		}
	}
}
