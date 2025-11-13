package com.hubEleven.hub.infrastructure.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@ConditionalOnProperty(name = "cache.enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "simple", matchIfMissing = true)
public class CacheConfig {
	// Spring의 Cache 이용
}
