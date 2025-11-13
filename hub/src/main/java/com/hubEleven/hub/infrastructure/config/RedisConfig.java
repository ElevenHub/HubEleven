package com.hubEleven.hub.infrastructure.config;

import java.time.Duration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
@ConditionalOnProperty(name = "cache.enabled", havingValue = "true")
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis")
public class RedisConfig {

	@Bean
	public RedisCacheConfiguration cacheConfiguration() {
		return RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofHours(1)) // TTL 1시간
				.disableCachingNullValues() // null 값 캐싱 방지
				.serializeKeysWith(
						RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(
						RedisSerializationContext.SerializationPair.fromSerializer(
								new GenericJackson2JsonRedisSerializer()));
	}
}
