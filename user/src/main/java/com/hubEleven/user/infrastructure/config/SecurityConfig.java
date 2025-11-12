package com.hubEleven.user.infrastructure.config;

import com.hubEleven.user.infrastructure.security.jwt.CustomJwtAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
		org.springframework.web.cors.CorsConfiguration configuration =
				new org.springframework.web.cors.CorsConfiguration();
		configuration.addAllowedOriginPattern("*");
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("*");
		configuration.setAllowCredentials(true);

		org.springframework.web.cors.UrlBasedCorsConfigurationSource source =
				new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	// 공개 엔드포인트용 SecurityFilterChain (OAuth2 비활성화)
	@Bean
	@Order(1)
	SecurityFilterChain publicSecurityFilterChain(HttpSecurity http) throws Exception {
		return http.securityMatcher(
						"/v1/user/login",
						"/v1/user/signup",
						"/api-docs/**",
						"/v3/api-docs/**",
						"/swagger-ui/**",
						"/swagger-ui.html",
						"/swagger-resources/**",
						"/webjars/**",
						"/h2-console/**")
				.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
				.cors(cors -> cors.configure(http))
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(
						session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.build();
	}

	// 보호된 엔드포인트용 SecurityFilterChain (OAuth2 활성화)
	@Bean
	@Order(2)
	SecurityFilterChain protectedSecurityFilterChain(
			HttpSecurity http, CustomJwtAuthenticationConverter jwtAuthConverter, JwtDecoder jwtDecoder)
			throws Exception {
		return http.headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
				.cors(cors -> cors.configure(http))
				.httpBasic(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(
						session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
				.oauth2ResourceServer(
						oauth ->
								oauth.jwt(
										jwt -> jwt.decoder(jwtDecoder).jwtAuthenticationConverter(jwtAuthConverter)))
				.build();
	}
}
