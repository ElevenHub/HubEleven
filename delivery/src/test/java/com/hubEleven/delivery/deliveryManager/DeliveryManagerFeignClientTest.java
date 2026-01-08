package com.hubEleven.delivery.deliveryManager;

import org.springframework.boot.test.context.SpringBootTest;

// @ActiveProfiles("test") // 이 줄 추가
@SpringBootTest
public class DeliveryManagerFeignClientTest {

	//	@Autowired private UserFeignClient userFeignClient;

	//	@Test
	//	void testUserFeignClient() {
	//		// 실제 값으로 테스트
	//		Long userId = 2L;
	//		Long requestUserId = 1L; // 실제 존재하는 유저 ID
	//		String requestUserRole = "MASTER"; // 또는 "HUB_MANAGER" 등
	//
	//		// when
	//		ResponseEntity<ApiResponse<UserInfoResponse>> response =
	//				userFeignClient.getUser(userId, requestUserId, requestUserRole);
	//
	//		// then
	//		assertNotNull(response, "응답이 null이 아니어야 합니다");
	//		assertNotNull(response.getBody(), "응답 body가 null이 아니어야 합니다");
	//		assertTrue(response.getStatusCode().is2xxSuccessful(), "HTTP 상태 코드가 2xx여야 합니다");
	//
	//		ApiResponse<UserInfoResponse> apiResponse = response.getBody();
	//		assertNotNull(apiResponse.result(), "result가 null이 아니어야 합니다");
	//
	//		UserInfoResponse userDto = apiResponse.result();
	//		assertThat(userDto.userId()).isEqualTo(userId);
	//
	//		// 로그 출력
	//		System.out.println("=== 응답 정보 ===");
	//		System.out.println("HTTP Status: " + response.getStatusCode());
	//		System.out.println("User ID: " + userDto.userId());
	//		System.out.println("Username: " + userDto.username());
	//		System.out.println("Role: " + userDto.role());
	//	}
}
