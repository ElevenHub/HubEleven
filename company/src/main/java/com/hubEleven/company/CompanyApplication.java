package com.hubEleven.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.hubEleven.company.infrastructure.client")
@SpringBootApplication
public class CompanyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompanyApplication.class, args);
	}
}
