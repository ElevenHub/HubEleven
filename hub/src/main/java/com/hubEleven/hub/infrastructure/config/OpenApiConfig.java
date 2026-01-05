package com.hubEleven.hub.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(
						new Info()
								.title("Hub Service API")
								.version("1.0")
								.description("Hub Service API with JWT Authentication"))
				.addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
				.components(
						new Components()
								.addSecuritySchemes(
										"Bearer Authentication",
										new SecurityScheme()
												.name("Bearer Authentication")
												.type(SecurityScheme.Type.HTTP)
												.scheme("bearer")
												.bearerFormat("JWT")
												.description("JWT 토큰을 입력하세요 (Bearer 제외)")));
	}
}
