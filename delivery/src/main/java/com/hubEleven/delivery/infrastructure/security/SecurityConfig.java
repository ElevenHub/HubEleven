package com.hubEleven.delivery.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		System.out.println("배송입니다");
		HeaderAuthenticationFilter headerAuthFilter = new HeaderAuthenticationFilter();

		return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
				// .oauth2ResourceServer(oauth2 -> oauth2.disable()) // 의존성 제거했으면 이 줄 불필요

				// 2. 커스텀 필터를 AuthenticationWebFilter 이전에 추가
				.addFilterAt(headerAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				.authorizeExchange(
						exchange -> {
							// PUBLIC_PATHS 설정은 Delivery Service에 필요하다면 추가
							exchange.anyExchange().authenticated(); // 나머지 모든 요청은 인증 필요
						})
				// 3. 인증되지 않은 요청에 대한 처리 (선택 사항: 응답 커스텀)
				// .exceptionHandling( /* ... */ )

				.build();
	}
}
