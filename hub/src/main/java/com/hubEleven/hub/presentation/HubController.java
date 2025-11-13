package com.hubEleven.hub.presentation;

import com.commonLib.common.exception.GlobalException;
import com.commonLib.common.response.ApiResponse;
import com.commonLib.common.response.ApiResponseEntity;
import com.hubEleven.hub.application.command.CreateHubCommand;
import com.hubEleven.hub.application.command.DeleteHubCommand;
import com.hubEleven.hub.application.command.UpdateHubCommand;
import com.hubEleven.hub.application.dto.HubListResult;
import com.hubEleven.hub.application.dto.HubResult;
import com.hubEleven.hub.application.service.HubService;
import com.hubEleven.hub.common.exception.HubErrorCode;
import com.hubEleven.hub.presentation.dto.request.HubCreateRequestDto;
import com.hubEleven.hub.presentation.dto.request.HubUpdateRequestDto;
import com.hubEleven.hub.presentation.dto.response.HubDeleteResponseDto;
import com.hubEleven.hub.presentation.dto.response.HubListResponseDto;
import com.hubEleven.hub.presentation.dto.response.HubResponseDto;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/hubs")
@RequiredArgsConstructor
public class HubController {

	private final HubService hubService;

	@PostMapping
	public ResponseEntity<ApiResponse<HubResponseDto>> createHub(
			@Valid @RequestBody HubCreateRequestDto request,
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Role") String userRole,
			@RequestHeader("X-Username") String userNameString) {
		if (!"MASTER".equalsIgnoreCase(userRole)) {
			throw new GlobalException(HubErrorCode.MASTER_ONLY);
		}
		CreateHubCommand command = request.toCommand(userId);
		HubResult result = hubService.createHub(command);
		HubResponseDto response = HubResponseDto.from(result);
		return ApiResponseEntity.success(response);
	}

	@PatchMapping("/{hubId}")
	public ResponseEntity<ApiResponse<HubResponseDto>> updateHub(
			@PathVariable UUID hubId,
			@Valid @RequestBody HubUpdateRequestDto request,
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Role") String userRole,
			@RequestHeader("X-Username") String userName) {
		if (!"MASTER".equalsIgnoreCase(userRole)) {
			throw new GlobalException(HubErrorCode.MASTER_ONLY);
		}
		UpdateHubCommand command = request.toCommand(hubId, userId);
		HubResult result = hubService.updateHub(command);
		HubResponseDto response = HubResponseDto.from(result);
		return ApiResponseEntity.success(response);
	}

	@DeleteMapping("/{hubId}")
	public ResponseEntity<ApiResponse<HubDeleteResponseDto>> deleteHub(
			@PathVariable UUID hubId,
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Role") String userRole,
			@RequestHeader("X-Username") String userName) {
		if (!"MASTER".equalsIgnoreCase(userRole)) {
			throw new GlobalException(HubErrorCode.MASTER_ONLY);
		}
		hubService.deleteHub(new DeleteHubCommand(hubId, userId));
		HubDeleteResponseDto response = new HubDeleteResponseDto(hubId, true);
		return ApiResponseEntity.success(response);
	}

	@GetMapping("/{hubId}")
	public ResponseEntity<ApiResponse<HubResponseDto>> getHub(@PathVariable UUID hubId) {
		HubResult result = hubService.getHub(hubId);
		HubResponseDto response = HubResponseDto.from(result);
		return ApiResponseEntity.success(response);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<HubListResponseDto>> getHubs() {
		HubListResult result = hubService.getHubs();
		List<HubResponseDto> responses = HubResponseDto.fromList(result.hubs());
		HubListResponseDto response = new HubListResponseDto(responses);
		return ApiResponseEntity.success(response);
	}
}
