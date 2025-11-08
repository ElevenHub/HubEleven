package com.hubEleven.company.infrastructure.configuration;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing
public class CompanyJpaConfig {

	@Bean
	public AuditorAware<Long> auditorAware() {
		return () -> {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth == null || !auth.isAuthenticated()) return Optional.empty();
			// TODO: 프로젝트의 Principal/JWT에서 userId 추출
			return Optional.ofNullable(extractUserId(auth));
		};
	}

	private Long extractUserId(Authentication auth) {
		// TODO: ((CustomPrincipal) auth.getPrincipal()).getUserId()
		return null;
	}
}
