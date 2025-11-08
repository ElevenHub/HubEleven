package com.hubEleven.company.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain filter(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();
	}
}
