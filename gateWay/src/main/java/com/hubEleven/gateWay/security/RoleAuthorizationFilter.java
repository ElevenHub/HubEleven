package com.hubEleven.gateWay.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RoleAuthorizationFilter implements GlobalFilter, Ordered {

	// 경로별 필요한 권한 정의 (HTTP 메서드별로 세분화)
	private static final Map<String, PathPermission> PATH_PERMISSIONS = new HashMap<>();

	static {
		// === User Service ===
		// 유저 조회 - 모든 인증된 사용자
		PATH_PERMISSIONS.put(
				"GET:/v1/user/",
				new PathPermission(
						List.of("MASTER", "HUB_MANAGER", "DELIVERY_MANAGER", "COMPANY_MANAGER")));
		// 유저 생성/수정/삭제 - MASTER만
		PATH_PERMISSIONS.put("POST:/v1/user", new PathPermission(List.of("MASTER")));
		PATH_PERMISSIONS.put("PUT:/v1/user", new PathPermission(List.of("MASTER")));
		PATH_PERMISSIONS.put("PATCH:/v1/user", new PathPermission(List.of("MASTER")));
		PATH_PERMISSIONS.put("DELETE:/v1/user", new PathPermission(List.of("MASTER")));

	}

	static class PathPermission {
		final List<String> allowedRoles;

		PathPermission(List<String> allowedRoles) {
			this.allowedRoles = allowedRoles;
		}
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String path = request.getURI().getPath();
		HttpMethod method = request.getMethod();
		String role = request.getHeaders().getFirst("X-User-Role");

		// X-User-Role 헤더가 없으면 (공개 경로이거나 인증되지 않은 사용자) 그냥 통과
		// JwtAuthenticationFilter에서 이미 인증 체크 완료
		if (role == null) {
			return chain.filter(exchange);
		}

		// HTTP 메서드 + 경로로 권한 체크
		String permissionKey = findPermissionKey(method.name(), path);

		if (permissionKey != null) {
			PathPermission permission = PATH_PERMISSIONS.get(permissionKey);
			if (permission != null && !permission.allowedRoles.contains(role)) {
				return onError(
						exchange,
						String.format("접근 권한이 없습니다. 필요한 권한: %s, 현재 권한: %s", permission.allowedRoles, role),
						HttpStatus.FORBIDDEN);
			}
		}

		return chain.filter(exchange);
	}

	/** HTTP 메서드와 경로를 매칭하여 권한 키를 찾습니다. 예: GET /v1/user/123 -> "GET:/v1/user/" */
	private String findPermissionKey(String method, String path) {
		// 정확히 일치하는 경로 먼저 찾기
		String exactKey = method + ":" + path;
		if (PATH_PERMISSIONS.containsKey(exactKey)) {
			return exactKey;
		}

		// 접두사 매칭 (가장 긴 것부터)
		return PATH_PERMISSIONS.keySet().stream()
				.filter(key -> key.startsWith(method + ":"))
				.filter(key -> path.startsWith(key.substring(method.length() + 1)))
				.max((k1, k2) -> Integer.compare(k1.length(), k2.length()))
				.orElse(null);
	}

	private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(status);
		response.getHeaders().add("Content-Type", "application/json");
		String body = String.format("{\"error\":\"%s\",\"message\":\"%s\"}", status.name(), message);
		return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
	}

	@Override
	public int getOrder() {
		return -99; // JwtAuthenticationFilter 다음에 실행
	}
}
