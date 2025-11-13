package com.hubEleven.delivery.presentation;

import com.commonLib.common.code.SuccessCode;
import com.commonLib.common.response.ApiResponse;
import com.commonLib.common.response.ApiResponseEntity;
import com.commonLib.common.response.CommonPageResponse;
import com.hubEleven.delivery.application.dto.DeliveryDetailResponseDto;
import com.hubEleven.delivery.application.dto.DeliveryRequestDto;
import com.hubEleven.delivery.application.dto.DeliveryResponseDto;
import com.hubEleven.delivery.application.service.DeliveryService;
import com.hubEleven.delivery.domain.DeliveryStatus;
import com.hubEleven.deliveryRoute.application.dto.DeliveryRouteRequestDto;
import com.hubEleven.deliveryRoute.application.dto.DeliveryRouteResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "배송", description = "배송 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/delivery")
public class DeliveryController {

	private final DeliveryService deliveryService;

	// 배송 검색
	@Operation(summary = "배송 검색", description = "조건을 가지고 배송 내역 검색")
	@GetMapping("/search")
	public ResponseEntity<ApiResponse<CommonPageResponse<DeliveryResponseDto>>> searchDelivery(
			@RequestParam(required = false) UUID deliveryId,
			@RequestParam(required = false) UUID orderId,
			@RequestParam(required = false) DeliveryStatus status,
			@RequestParam(required = false) UUID fromHubId,
			@RequestParam(required = false) UUID toHubId,
			@RequestParam(required = false) String recipientName,
			@RequestParam(required = false) String recipientSlackId,
			@RequestParam(required = false) String deliveryManagerId,
			@RequestParam(value = "page", defaultValue = "0") int page, // 페이지 번호
			@RequestParam(value = "size", defaultValue = "10") int size, // 조회할 항목수
			@RequestParam(value = "sort", defaultValue = "createdAt") String sort // 정렬기준
			) {
		Page<DeliveryResponseDto> result =
				deliveryService.searchDelivery(
						deliveryId,
						orderId,
						status,
						fromHubId,
						toHubId,
						recipientName,
						recipientSlackId,
						deliveryManagerId,
						page,
						size,
						sort);
		CommonPageResponse<DeliveryResponseDto> response = CommonPageResponse.of(result);
		return ApiResponseEntity.success(response);
	}

	// 배송 전체 조회
	@Operation(summary = "배송 전체 조회", description = "배송 내역 전체를 조회 합니다.")
	@GetMapping
	public ResponseEntity<ApiResponse<CommonPageResponse<DeliveryResponseDto>>> getDeliveryList(
			@RequestParam(value = "page", defaultValue = "0") int page, // 페이지 번호
			@RequestParam(value = "size", defaultValue = "10") int size, // 조회할 항목수
			@RequestParam(value = "sort", defaultValue = "createdAt") String sort // 정렬기준
			) {
		Page<DeliveryResponseDto> result = deliveryService.getDeliveryList(page, size, sort);
		CommonPageResponse<DeliveryResponseDto> response = CommonPageResponse.of(result);
		return ApiResponseEntity.success(response);
	}

	// 배송 상세 조회
	@Operation(summary = "배송 상세 조회", description = "deliveryId에 해당하는 배송 내역 및 경로를 조회 합니다.")
	@GetMapping("/{deliveryId}")
	public ResponseEntity<ApiResponse<DeliveryDetailResponseDto>> getDelivery(
			@PathVariable UUID deliveryId) {
		DeliveryDetailResponseDto result = deliveryService.getDelivery(deliveryId);

		return ApiResponseEntity.create(SuccessCode.SUCCESS, "/delivery/" + deliveryId, result);
	}

	// 배송 생성
	@Operation(summary = "배송 생성", description = "배송 내역과 경로를 생성합니다.")
	@PreAuthorize("hasAnyAuthority('MASTER', 'HUB_MANAGER')")
	@PostMapping
	public ResponseEntity<ApiResponse<DeliveryResponseDto>> createDelivery(
			@RequestBody DeliveryRequestDto deliveryRequestDto) {
		UUID orderId = deliveryRequestDto.orderId();
		DeliveryResponseDto result = deliveryService.createDelivery(orderId);
		return ApiResponseEntity.create(SuccessCode.CREATED, "/delivery/" + orderId, result);
	}

	// 배송 수정
	@Operation(summary = "배송 수정", description = "배송 내역을 수정합니다.")
	@PreAuthorize("hasAnyAuthority('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER')")
	@PatchMapping("/{deliveryId}")
	public ResponseEntity<ApiResponse<DeliveryResponseDto>> updateDelivery(
			@PathVariable UUID deliveryId, @RequestBody DeliveryRequestDto deliveryRequestDto) {
		DeliveryResponseDto result = deliveryService.updateDelivery(deliveryId, deliveryRequestDto);
		return ApiResponseEntity.create(SuccessCode.UPDATED, "/delivery/" + deliveryId, result);
	}

	// 배송 삭제
	@DeleteMapping("/{deliveryId}")
	@Operation(summary = "배송 삭제", description = "배송 내역을 삭제합니다.")
	@PreAuthorize("hasAnyAuthority('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponse<Object>> deleteDelivery(
			@PathVariable UUID deliveryId, @RequestHeader Long userId) {
		deliveryService.deleteDelivery(deliveryId, userId);
		return ApiResponseEntity.create(SuccessCode.DELETED, "/delivery/" + deliveryId, null);
	}

	// 배송 경로 수정
	@PatchMapping("/{deliveryId}/route")
	@Operation(summary = "배송 경로 수정", description = "배송 경로를 수정합니다.")
	@PreAuthorize("hasAnyAuthority('MASTER', 'HUB_MANAGER', 'DELIVERY_MANAGER')")
	public ResponseEntity<ApiResponse<DeliveryRouteResponseDto>> updateDelivery(
			@PathVariable UUID deliveryId, @RequestBody DeliveryRouteRequestDto deliveryRouteRequestDto) {
		DeliveryRouteResponseDto result =
				deliveryService.updateDeliveryRoute(deliveryId, deliveryRouteRequestDto);
		return ApiResponseEntity.create(SuccessCode.UPDATED, "/delivery/{deliveryId}/route", result);
	}
}
