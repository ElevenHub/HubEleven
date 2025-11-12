package com.hubEleven.hub.application.service;

import com.commonLib.common.exception.GlobalException;
import com.hubEleven.hub.application.dto.RouteResult;
import com.hubEleven.hub.application.util.RouteCalculationResult;
import com.hubEleven.hub.application.util.RouteCalculator;
import com.hubEleven.hub.common.exception.HubErrorCode;
import com.hubEleven.hub.domain.model.Hub;
import com.hubEleven.hub.domain.model.HubRoute;
import com.hubEleven.hub.domain.model.HubRouteSegment;
import com.hubEleven.hub.domain.repository.HubRepository;
import com.hubEleven.hub.domain.repository.HubRouteRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubRouteServiceImpl implements HubRouteService {

	private final HubRepository hubRepository;
	private final HubRouteRepository hubRouteRepository;
	private final RouteCalculator routeCalculator;

	@Override
	@Transactional
	public void generateAllRoutes() {
		log.info("모든 허브 경로 생성 시작");

		List<Hub> allHubs = hubRepository.findAllNotDeleted();

		if (allHubs.size() < 2) {
			log.warn("경로 생성 가능한 허브가 부족합니다. 현재 허브 수: {}", allHubs.size());
			return;
		}

		int generatedCount = 0;

		// 모든 허브 쌍에 대해 경로 생성
		for (Hub fromHub : allHubs) {
			for (Hub toHub : allHubs) {
				if (fromHub.getHubId().equals(toHub.getHubId())) {
					continue;
				}

				try {
					// 기존 경로가 있으면 건너뛰기
					if (hubRouteRepository
							.findByFromHubIdAndToHubId(fromHub.getHubId(), toHub.getHubId())
							.isPresent()) {
						log.debug("이미 존재하는 경로: {} -> {}", fromHub.getName(), toHub.getName());
						continue;
					}

					// 경로 계산
					RouteCalculationResult calculationResult =
							routeCalculator.calculate(fromHub.getHubId(), toHub.getHubId(), allHubs);

					// Segment 생성
					List<HubRouteSegment> segments = createSegments(calculationResult);

					// 총 소요시간 계산
					Integer totalDuration =
							routeCalculator.calculateTotalDuration(calculationResult.segmentDistances());

					// HubRoute 생성 및 저장
					Long createdBy = 1L; // 임시값
					HubRoute hubRoute =
							HubRoute.create(
									fromHub.getHubId(),
									toHub.getHubId(),
									calculationResult.totalDistance(),
									totalDuration,
									segments,
									createdBy);

					hubRouteRepository.save(hubRoute);
					generatedCount++;

					log.info(
							"경로 생성 완료: {} -> {} (거리: {}km, 소요시간: {}분)",
							fromHub.getName(),
							toHub.getName(),
							String.format("%.2f", calculationResult.totalDistance()),
							totalDuration);

				} catch (GlobalException e) {
					log.warn("경로 생성 실패: {} -> {} ({})", fromHub.getName(), toHub.getName(), e.getMessage());
				}
			}
		}

		log.info("경로 생성 완료. 총 {}개 경로 생성됨", generatedCount);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RouteResult> getAllRoutes() {
		List<HubRoute> routes = hubRouteRepository.findAllNotDeleted();
		return routes.stream().map(RouteResult::from).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public RouteResult findRoute(UUID departureHubId, UUID arrivalHubId) {
		HubRoute hubRoute =
				hubRouteRepository
						.findByFromHubIdAndToHubId(departureHubId, arrivalHubId)
						.orElseThrow(() -> new GlobalException(HubErrorCode.ROUTE_NOT_FOUND));

		return RouteResult.from(hubRoute);
	}

	private List<HubRouteSegment> createSegments(RouteCalculationResult calculationResult) {
		List<HubRouteSegment> segments = new ArrayList<>();
		List<UUID> path = calculationResult.path();
		List<Double> distances = calculationResult.segmentDistances();

		Long createdBy = 1L; // 임시값

		for (int i = 0; i < distances.size(); i++) {
			UUID fromHubId = path.get(i);
			UUID toHubId = path.get(i + 1);
			Double distance = distances.get(i);
			Integer duration = routeCalculator.calculateDuration(distance);

			HubRouteSegment segment =
					HubRouteSegment.create(fromHubId, toHubId, i + 1, distance, duration, createdBy);

			segments.add(segment);
		}

		return segments;
	}
}
