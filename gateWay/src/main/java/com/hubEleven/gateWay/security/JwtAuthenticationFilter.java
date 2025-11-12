package com.hubEleven.gateWay.security;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;


@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

	private final JwtValidator jwtValidator;

	// 인증이 필요 없는 공개 경로
	private static final List<String> PUBLIC_PATHS =
			Arrays.asList(
					"/v1/user/login",
					"/v1/user/signup",
					"/swagger-ui",
					"/v3/api-docs",
					"/springdoc",
					"/webjars",
					"/swagger-resources");

	public JwtAuthenticationFilter(JwtValidator jwtValidator) {
		this.jwtValidator = jwtValidator;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println(">>> JwtAuthenticationFilter 실행됨. 경로: " + exchange.getRequest().getURI().getPath());

		ServerHttpRequest request = exchange.getRequest();
		String path = request.getURI().getPath();

        // 공개 경로는 인증 없이 통과
		if (isPublicPath(path)) {
			return chain.filter(exchange);
		}

		// Authorization 헤더 확인
		String authHeader = request.getHeaders().getFirst("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
		}

		String token = authHeader.substring(7);

		// JWT 토큰 검증
		if (!jwtValidator.isTokenValid(token)) {
			return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
		}

		try {
			// JWT에서 사용자 정보 추출하여 헤더에 추가
			String username = jwtValidator.extractUsername(token);
			String role = jwtValidator.extractRole(token);
			Long userId = jwtValidator.extractUserId(token);

			// 다운스트림 서비스에서 사용할 수 있도록 헤더에 사용자 정보 추가
			ServerHttpRequest modifiedRequest =
					request
							.mutate()
							.header("X-User-Id", String.valueOf(userId))
							.header("X-Username", username)
							.header("X-User-Role", role)
							.build();

			return chain.filter(exchange.mutate().request(modifiedRequest).build());
		} catch (Exception e) {
			return onError(exchange, "JWT processing error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
	}

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
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
		return -100; // 다른 필터들보다 먼저 실행
	}
}
