package com.hubEleven.company.infrastructure.configuration;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignConfig {

	@Bean
	public RequestInterceptor authHeaderForwardingInterceptor() {
		return template -> {
			var ctx = SecurityContextHolder.getContext();
			if (ctx != null && ctx.getAuthentication() != null) {
				var auth = ctx.getAuthentication();
				Object cred = auth.getCredentials();
				if (cred instanceof String token && !token.isBlank()) {
					template.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
				}
			}
		};
	}
}
