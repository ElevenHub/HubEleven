package com.hubEleven.company.infrastructure.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import java.util.Map;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		ObjectMapper objectMapper =
				new ObjectMapper()
						.registerModule(new JavaTimeModule())
						.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		PolymorphicTypeValidator ptv =
				BasicPolymorphicTypeValidator.builder()
						.allowIfSubType("com.hubEleven")
						.allowIfSubType("com.commonLib")
						.allowIfSubType("java.util")
						.allowIfSubType("java.time")
						.build();

		objectMapper.activateDefaultTyping(
				ptv, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

		GenericJackson2JsonRedisSerializer valueSerializer =
				new GenericJackson2JsonRedisSerializer(objectMapper);

		RedisCacheConfiguration defaultConfig =
				RedisCacheConfiguration.defaultCacheConfig()
						.disableCachingNullValues()
						.prefixCacheNameWith("company::")
						.serializeKeysWith(
								RedisSerializationContext.SerializationPair.fromSerializer(
										new StringRedisSerializer()))
						.serializeValuesWith(
								RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer));

		Map<String, RedisCacheConfiguration> perCacheConfig =
				Map.of("companies", defaultConfig.entryTtl(Duration.ofHours(1)));

		return RedisCacheManager.builder(connectionFactory)
				.cacheDefaults(defaultConfig)
				.withInitialCacheConfigurations(perCacheConfig)
				.transactionAware()
				.build();
	}
}
