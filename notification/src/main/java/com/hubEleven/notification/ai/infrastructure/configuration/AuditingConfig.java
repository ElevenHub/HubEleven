package com.hubEleven.notification.ai.infrastructure.configuration;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class AuditingConfig {

	@Bean
	public AuditorAware<Long> auditorAware() {
		return () -> Optional.of(System.currentTimeMillis());
	}
}
