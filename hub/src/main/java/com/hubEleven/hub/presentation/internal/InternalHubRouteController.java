package com.hubEleven.hub.presentation.internal;

import com.commonLib.common.response.ApiResponse;
import com.hubEleven.hub.application.dto.RouteResult;
import com.hubEleven.hub.application.service.HubRouteService;
import com.hubEleven.hub.presentation.dto.response.HubRouteResponseDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/internal/hub-routes")
@RequiredArgsConstructor
public class InternalHubRouteController {

	private final HubRouteService hubRouteService;

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
