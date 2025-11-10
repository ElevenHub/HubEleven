package com.hubEleven.deliveryManager.presentation;

import com.hubEleven.deliveryManager.application.DeliveryManagerService;
import com.hubEleven.deliveryManager.presentation.dto.request.DeliveryManagerAssignRequestDto;
import com.hubEleven.deliveryManager.presentation.dto.request.DeliveryManagerCreateRequestDto;
import com.hubEleven.deliveryManager.presentation.dto.response.DeliveryManagerAssignResponseDto;
import com.hubEleven.deliveryManager.presentation.dto.response.DeliveryManagerResponseDto;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
		log.info("[DeliveryManager Controller] 배달 담당자 생성 요청");
		DeliveryManagerResponseDto responseDto =
				deliveryManagerService.createDeliveryManager(createRequestDto);

		return ResponseEntity.ok(responseDto);
	}

	@GetMapping
	public ResponseEntity<List<DeliveryManagerResponseDto>> getAllDeliveryManager() {
		log.info("[DeliveryManager Controller] 배달 담당자 목록 조회 요청");
		List<DeliveryManagerResponseDto> responseDtoList =
				deliveryManagerService.getAllDeliveryManager();
		return ResponseEntity.ok(responseDtoList);
	}

	@GetMapping("/{managerId}")
	public ResponseEntity<DeliveryManagerResponseDto> getDeliveryManager(
			@PathVariable Long managerId) {
		log.info("[DeliveryManager Controller] 배달 담당자 단일 조회 요청");
		DeliveryManagerResponseDto responseDto = deliveryManagerService.getDeliveryManager(managerId);
		return ResponseEntity.ok(responseDto);
	}

	@DeleteMapping("/{managerId}")
	public ResponseEntity<Void> deleteDeliveryManager(@PathVariable Long managerId) {
		deliveryManagerService.deleteDeliveryManager(managerId);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/assign")
	public ResponseEntity<DeliveryManagerAssignResponseDto> assignDeliveryManager(
			@RequestBody @Valid DeliveryManagerAssignRequestDto assignRequestDto) {
		DeliveryManagerAssignResponseDto assignResponseDtoList =
				deliveryManagerService.assignDeliveryManagers(assignRequestDto);

		return ResponseEntity.ok(assignResponseDtoList);
	}
}
