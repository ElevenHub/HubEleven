package com.hubEleven.hub.presentation;

import com.hubEleven.common.response.ApiResponse;
import com.hubEleven.common.response.ApiResponseEntity;
import com.hubEleven.hub.application.dto.RouteResult;
import com.hubEleven.hub.application.service.RouteService;
import com.hubEleven.hub.presentation.dto.response.HubRouteResponseDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/hub-routes")
@RequiredArgsConstructor
public class HubRouteController {

	private final RouteService routeService;

	@PostMapping("/generate")
	public ResponseEntity<ApiResponse<String>> generateAllRoutes() {
		routeService.generateAllRoutes();
		return ApiResponseEntity.success("모든 경로 생성이 완료되었습니다.");
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<HubRouteResponseDto>>> getAllRoutes() {
		List<RouteResult> results = routeService.getAllRoutes();
		List<HubRouteResponseDto> response = HubRouteResponseDto.fromList(results);
		return ApiResponseEntity.success(response);
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<HubRouteResponseDto>> searchRoute(
			@RequestParam UUID fromHubId, @RequestParam UUID toHubId) {
		RouteResult result = routeService.findRoute(fromHubId, toHubId);
		HubRouteResponseDto response = HubRouteResponseDto.from(result);
		return ApiResponseEntity.success(response);
	}
}
