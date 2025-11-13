package com.hubEleven.delivery.infrastructure.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {
	@Bean
	public Retryer retryer() {
		// 0.1초 시작, 최대 1초 간견, 3회 재시도
		return new Retryer.Default(100, 1000, 3);
	}
}
