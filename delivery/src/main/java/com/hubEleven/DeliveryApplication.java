package com.hubEleven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(
		basePackages = {
			"com.hubEleven.delivery.infrastructure.client",
			"com.hubEleven.deliveryManager.infrastructure.client"
		})
public class DeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryApplication.class, args);
	}
}
