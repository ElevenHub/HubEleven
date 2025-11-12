package com.hubEleven.delivery.deliveryManager;

import com.hubEleven.deliveryManager.application.DeliveryManagerService;
import com.hubEleven.deliveryManager.infrastructure.DeliveryManagerJpaRepository;
import com.hubEleven.deliveryManager.presentation.dto.request.DeliveryManagerCreateRequestDto;
import com.hubEleven.deliveryManager.presentation.dto.response.DeliveryManagerResponseDto;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Rollback
public class DeliveryManagerCreateTest {

	@Autowired private DeliveryManagerService deliveryManagerService;

	@Autowired private DeliveryManagerJpaRepository deliveryManagerJpaRepository;

	private final int THREAD_COUNT = 3;

	@BeforeEach
	void cleanDatabase() {
		deliveryManagerJpaRepository.deleteAll(); // DB 초기화
		deliveryManagerJpaRepository.flush(); // JPA 캐시 반영
		System.out.println("테스트 전 DB 초기화 완료");
	}

	@Test
	void testConcurrentDeliveryOrderCreation() throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
		Set<Integer> deliveryOrders = ConcurrentHashMap.newKeySet(); // thread-safe

		for (int i = 0; i < THREAD_COUNT; i++) {
			final long managerId = 1000 + i; // 각 쓰레드별 고유 ID
			executor.submit(
					() -> {
						try {
							DeliveryManagerCreateRequestDto dto = new DeliveryManagerCreateRequestDto(managerId);
							DeliveryManagerResponseDto response =
									deliveryManagerService.createDeliveryManager(dto);

							// 순번 중복 체크
							if (!deliveryOrders.add(response.deliveryOrder())) {
								System.err.println("[중복] 배송순번 발생: " + response.deliveryOrder());
							} else {
								System.out.println("[생성] 배송순번: " + response.deliveryOrder());
							}
						} catch (Exception e) {
							System.err.println("[실패] " + e.getClass().getSimpleName() + " - " + e.getMessage());
						}
					});
		}

		executor.shutdown();
		executor.awaitTermination(15, TimeUnit.SECONDS); // 모든 쓰레드 종료 대기

		System.out.println("테스트 종료, 생성된 순번 수: " + deliveryOrders.size());

		// 검증: 순번 개수가 쓰레드 수와 동일해야 중복 없음
		if (deliveryOrders.size() != THREAD_COUNT) {
			throw new AssertionError("순번 중복 발생! 생성된 순번 수: " + deliveryOrders.size());
		}
	}
}
