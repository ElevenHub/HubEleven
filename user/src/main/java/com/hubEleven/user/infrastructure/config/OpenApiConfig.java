package com.hubEleven.user.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(
						new Info()
								.title("User Service API")
								.version("1.0")
								.description("User Service API with JWT Authentication"))
				.addServersItem(new Server().url("http://localhost:8081").description("Local server"))
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
