package com.hubEleven.delivery.infrastructure.security;

import java.util.Collections;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class HeaderAuthenticationFilter implements WebFilter {
	// Gateway에서 설정한 사용자 ID 헤더 키
	private static final String USER_ID_HEADER = "X-User-Id";
	private static final String USER_ROLE_HEADER = "X-User-Role";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		String userId = exchange.getRequest().getHeaders().getFirst(USER_ID_HEADER);
		String userRole = exchange.getRequest().getHeaders().getFirst(USER_ROLE_HEADER);

		// 1. 헤더가 없거나 유효하지 않으면 다음 체인으로 넘김 (Spring Security가 401/403 처리)
		if (userId == null || userRole == null) {
			return chain.filter(exchange);
		}

		// 2. 인증 객체 생성 (Authentication Principal)
		// UserDetails 대신 userId를 주체(Principal)로 사용
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userRole);
		List<SimpleGrantedAuthority> authorities = Collections.singletonList(authority);

		// 인증 성공 토큰 생성 (principal: userId, credentials: null, authorities: role)
		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(userId, null, authorities);

		// 3. SecurityContext에 인증 정보 저장
		SecurityContext securityContext = new SecurityContextImpl(authentication);

		// 4. ServerWebExchange에 SecurityContext를 저장하여 다음 필터 체인(인가)에서 사용 가능하도록 함
		// filter 체인의 Mono<Void>를 실행하고, 컨텍스트 쓰기
		return chain
				.filter(exchange)
				// 4. filter 체인의 Mono<Void>를 실행하고, 컨텍스트 쓰기
				.contextWrite(
						context -> {
							// SecurityContext 객체를 Mono.just()로 감싸서 Context에 저장
							// 이 키(SecurityContext.class)에 저장된 값은 반드시 Mono<SecurityContext>여야 합니다.
							return context.put(
									SecurityContext.class,
									reactor.core.publisher.Mono.just(securityContext) // <--- Mono로 감싸서 저장
									);
						});
	}
}
