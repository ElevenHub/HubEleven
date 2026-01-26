package com.hubEleven.gateWay.security;

import static com.hubEleven.gateWay.exception.GateWayException.*;

import com.commonLib.common.exception.GlobalException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

	private final JwtValidator jwtValidator;

	// 인증이 필요 없는 공개 경로
	private static final List<String> PUBLIC_PATHS =
			Arrays.asList(
					"/v1/user/login",
					"/v1/user/signup",
					"/v1/ai",
					"/swagger-ui",
					"/v3/api-docs",
					"/springdoc",
					"/webjars",
					"/swagger-resources");

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String path = request.getURI().getPath();

		// 공개 경로는 인증 없이 통과
		if (isPublicPath(path)) {
			return chain.filter(exchange);
		}

		// Authorization 헤더 확인
		String authHeader = request.getHeaders().getFirst("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new GlobalException(INVALID_AUTHORIZATION_HEADER);
		}

		String token = authHeader.substring(7);

		// JWT 토큰 검증
		if (!jwtValidator.isTokenValid(token)) {
			throw new GlobalException(INVALID_JWT_TOKEN);
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
			throw new GlobalException(JWT_PROCESSING_ERROR);
		}
	}

	private boolean isPublicPath(String path) {
		return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
	}

	@Override
	public int getOrder() {
		return -100; // 다른 필터들보다 먼저 실행
	}
}
