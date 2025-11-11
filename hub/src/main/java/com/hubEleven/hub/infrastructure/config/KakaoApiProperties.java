package com.hubEleven.hub.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kakao.api")
public class KakaoApiProperties {
	private String key;
	private String baseUrl;
}
