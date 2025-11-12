package com.hubEleven.gateWay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    // SecurityConfig.java 파일 (WebFlux 환경 가정)
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                // 1. CSRF 방어 비활성화
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                // Spring Security의 기본 인증/권한 부여 비활성화
                .authorizeExchange(exchange -> exchange.anyExchange().permitAll()) // 모든 요청 허용
                .build();
    }
}
