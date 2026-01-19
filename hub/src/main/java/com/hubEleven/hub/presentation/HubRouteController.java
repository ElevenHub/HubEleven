package com.hubEleven.hub.presentation;

import com.commonLib.common.exception.GlobalException;
import com.commonLib.common.response.ApiResponse;
import com.hubEleven.hub.application.dto.RouteResult;
import com.hubEleven.hub.application.service.HubRouteService;
import com.hubEleven.hub.common.exception.HubErrorCode;
import com.hubEleven.hub.presentation.dto.response.HubRouteResponseDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/hub-routes")
@RequiredArgsConstructor
public class HubRouteController {

	private final HubRouteService hubRouteService;

	@PostMapping("/generate")
	public ResponseEntity<ApiResponse<String>> generateAllRoutes(
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Role") String userRole,
			@RequestHeader("X-Username") String userName) {
		if (!"MASTER".equalsIgnoreCase(userRole)) {
			throw new GlobalException(HubErrorCode.MASTER_ONLY);
		}
		hubRouteService.generateAllRoutes(userId);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("모든 경로 생성이 완료되었습니다."));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<HubRouteResponseDto>>> getAllRoutes() {
		List<RouteResult> results = hubRouteService.getAllRoutes();
		List<HubRouteResponseDto> response = HubRouteResponseDto.fromList(results);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<HubRouteResponseDto>> searchRoute(
			@RequestParam UUID fromHubId, @RequestParam UUID toHubId) {
		RouteResult result = hubRouteService.findRoute(fromHubId, toHubId);
		HubRouteResponseDto response = HubRouteResponseDto.from(result);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
	}
}
