package com.hubEleven.hub.presentation.internal;

import com.commonLib.common.response.ApiResponse;
import com.commonLib.common.response.ApiResponseEntity;
import com.hubEleven.hub.application.dto.RouteResult;
import com.hubEleven.hub.application.service.RouteService;
import com.hubEleven.hub.presentation.dto.response.HubRouteResponseDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/internal/hub-routes")
@RequiredArgsConstructor
public class InternalHubRouteController {

	private final RouteService routeService;

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
