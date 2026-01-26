package com.hubEleven.gateWay.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> swaggerUrls(
			RouteDefinitionLocator routeLocator, SwaggerUiConfigProperties swaggerUiConfigProperties) {

		Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = new HashSet<>();
		List<RouteDefinition> definitions = routeLocator.getRouteDefinitions().collectList().block();

		if (definitions != null) {
			definitions.forEach(
					routeDefinition -> {
						String name = routeDefinition.getId();
						if (name.endsWith("-service")) {
							String url = "/springdoc/openapi3-" + name + ".json";
							AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl =
									new AbstractSwaggerUiConfigProperties.SwaggerUrl(name, url, null);
							urls.add(swaggerUrl);
						}
					});
		}

		// application.yml에 정의된 URL들도 추가
		if (swaggerUiConfigProperties.getUrls() != null) {
			urls.addAll(swaggerUiConfigProperties.getUrls());
		}

		return urls;
	}
}
