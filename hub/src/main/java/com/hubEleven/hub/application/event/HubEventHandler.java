package com.hubEleven.hub.application.event;

import com.hubEleven.hub.application.service.HubRouteServiceImpl;
import com.hubEleven.hub.domain.event.HubCreatedEvent;
import com.hubEleven.hub.domain.event.HubDeletedEvent;
import com.hubEleven.hub.domain.event.HubLocationChangedEvent;
import com.hubEleven.hub.domain.repository.HubRouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventHandler {

	private final HubRouteRepository hubRouteRepository;
	private final HubRouteServiceImpl hubRouteService;

	// hub 생성 이벤트
	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleHubCreated(HubCreatedEvent event) {
		log.info("Hub 생성 이벤트 수신: hubId={}", event.getHubId());

		try {
			// 기존 Route 전체 soft delete
			hubRouteRepository.softDeleteAll(event.getUserId());

			// 전체 경로 재계산
			hubRouteService.generateAllRoutes(event.getUserId());

		} catch (Exception e) {
			log.error("Hub 생성 이벤트 처리 중 오류 발생: hubId={}", event.getHubId(), e);
		}
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleHubLocationChanged(HubLocationChangedEvent event) {
		log.info(
				"Hub 위치 변경 이벤트 수신: hubId={}, newLocation={}", event.getHubId(), event.getNewLocation());

		try {
			// 기존 Route 전체 soft delete
			hubRouteRepository.softDeleteAll(event.getUserId());

			// 전체 경로 재계산
			hubRouteService.generateAllRoutes(event.getUserId());

		} catch (Exception e) {
			log.error("Hub 위치 변경 이벤트 처리 중 오류 발생: hubId={}", event.getHubId(), e);
		}
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleHubDeleted(HubDeletedEvent event) {
		log.info("Hub 삭제 이벤트 수신: hubId={}", event.getHubId());

		try {
			hubRouteRepository.softDeleteAll(event.getUserId());

			hubRouteService.generateAllRoutes(event.getUserId());

		} catch (Exception e) {
			log.error("Hub 삭제 이벤트 처리 중 오류 발생: hubId={}", event.getHubId(), e);
		}
	}
}
