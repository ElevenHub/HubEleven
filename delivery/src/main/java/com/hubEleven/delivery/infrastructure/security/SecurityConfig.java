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
		HeaderAuthenticationFilter headerAuthFilter = new HeaderAuthenticationFilter();

		return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
				// .oauth2ResourceServer(oauth2 -> oauth2.disable()) // 의존성 제거했으면 이 줄 불필요

				// CORS preflight(OPTIONS) 처리를 위해 CORS 활성화 (필요 시 CorsWebFilter/설정 추가)
				.cors(org.springframework.security.config.Customizer.withDefaults())

				// 2. 커스텀 필터를 AuthenticationWebFilter 이전에 추가
				.addFilterAt(headerAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
				.authorizeExchange(
						exchange -> {
							// 브라우저의 preflight(OPTIONS)는 Authorization 없이 오므로 먼저 허용
							exchange.pathMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll();

							// Swagger / OpenAPI 문서 및 UI는 인증 없이 접근 가능
							exchange
									.pathMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html")
									.permitAll();

							// PUBLIC_PATHS 설정은 Delivery Service에 필요하다면 추가
							exchange.anyExchange().authenticated(); // 나머지 모든 요청은 인증 필요
						})
				// 3. 인증되지 않은 요청에 대한 처리 (선택 사항: 응답 커스텀)
				// .exceptionHandling( /* ... */ )

				.build();
	}
}
