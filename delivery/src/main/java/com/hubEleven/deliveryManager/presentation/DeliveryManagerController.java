package com.hubEleven.deliveryManager.presentation;

import com.hubEleven.deliveryManager.application.DeliveryManagerService;
import com.hubEleven.deliveryManager.presentation.dto.request.DeliveryManagerCreateRequestDto;
import com.hubEleven.deliveryManager.presentation.dto.response.DeliveryManagerResponseDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/delivery-managers")
public class DeliveryManagerController {

	private final DeliveryManagerService deliveryManagerService;

	public DeliveryManagerController(DeliveryManagerService deliveryManagerService) {
		this.deliveryManagerService = deliveryManagerService;
	}

	@PostMapping
	public ResponseEntity<DeliveryManagerResponseDto> createDeliveryManager(
			@RequestBody @Valid DeliveryManagerCreateRequestDto createRequestDto) {
		// TODO: JWT토큰 파싱하여 권한 검증
		log.info("[Controller] 배달 담당자 생성 요청");
		DeliveryManagerResponseDto responseDto =
				deliveryManagerService.createDeliveryManager(createRequestDto);

		return ResponseEntity.ok(responseDto);
	}
}
