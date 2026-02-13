package com.hubEleven.user;

import com.commonLib.common.exception.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
		basePackages = {
			"com.hubEleven.user", // 현재 서비스 패키지
			"com.commonLib.common" // 공통 모듈 패키지
		},
		excludeFilters = {
			@ComponentScan.Filter(
					type = FilterType.ASSIGNABLE_TYPE,
					classes = GlobalExceptionHandler.class)
		})
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}
}
