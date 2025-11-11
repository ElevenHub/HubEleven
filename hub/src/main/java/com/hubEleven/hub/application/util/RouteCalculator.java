package com.hubEleven.hub.application.util;

import com.hubEleven.hub.domain.model.Hub;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RouteCalculator {

	private static final double MAX_DIRECT_DISTANCE_KM = 200.0;
	private static final double AVERAGE_SPEED_KM_PER_HOUR = 60.0;
	private static final int HUB_PROCESSING_TIME_MINUTES = 30;

	private final DistanceCalculator distanceCalculator;

	public RouteCalculationResult calculate(UUID departureId, UUID arrivalId, List<Hub> allHubs) {

		// 허브 맵 생성 (ID -> Hub)
		Map<UUID, Hub> hubMap = new HashMap<>();
		for (Hub hub : allHubs) {
			hubMap.put(hub.getHubId(), hub);
		}

		// 그래프 구성 (인접 리스트)
		Map<UUID, List<Edge>> graph = buildGraph(allHubs, hubMap);

		// Dijkstra
		Map<UUID, Double> distances = new HashMap<>();
		Map<UUID, UUID> previous = new HashMap<>();
		PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingDouble(node -> node.distance));

		// 초기화
		for (Hub hub : allHubs) {
			distances.put(hub.getHubId(), Double.MAX_VALUE);
		}
		distances.put(departureId, 0.0);
		pq.offer(new Node(departureId, 0.0));

		while (!pq.isEmpty()) {
			Node current = pq.poll();
			UUID currentId = current.hubId;

			if (currentId.equals(arrivalId)) {
				break;
			}

			if (current.distance > distances.get(currentId)) {
				continue;
			}

			List<Edge> neighbors = graph.getOrDefault(currentId, Collections.emptyList());
			for (Edge edge : neighbors) {
				double newDist = distances.get(currentId) + edge.distance;
				if (newDist < distances.get(edge.toHubId)) {
					distances.put(edge.toHubId, newDist);
					previous.put(edge.toHubId, currentId);
					pq.offer(new Node(edge.toHubId, newDist));
				}
			}
		}

		List<UUID> path = reconstructPath(previous, departureId, arrivalId);
		List<Double> segmentDistances = calculateSegmentDistances(path, hubMap);
		Double totalDistance = distances.get(arrivalId);

		return new RouteCalculationResult(path, segmentDistances, totalDistance);
	}

	// 각 구간별 소요 시간 계산 (분 단위)
	public Integer calculateDuration(Double distance) {
		return (int) Math.round((distance / AVERAGE_SPEED_KM_PER_HOUR) * 60);
	}

	// 전체 소요 시간 계산 (경유지 처리 시간 포함)
	public Integer calculateTotalDuration(List<Double> segmentDistances) {
		int travelTime = 0;
		for (Double distance : segmentDistances) {
			travelTime += calculateDuration(distance);
		}

		// 경유지 수 = 전체 구간 수 - 1
		int viaHubCount = Math.max(0, segmentDistances.size() - 1);
		int processingTime = viaHubCount * HUB_PROCESSING_TIME_MINUTES;

		return travelTime + processingTime;
	}

	// 그래프 구성 (200km 이하만 연결)
	private Map<UUID, List<Edge>> buildGraph(List<Hub> allHubs, Map<UUID, Hub> hubMap) {
		Map<UUID, List<Edge>> graph = new HashMap<>();

		for (Hub from : allHubs) {
			List<Edge> edges = new ArrayList<>();
			for (Hub to : allHubs) {
				if (from.getHubId().equals(to.getHubId())) {
					continue;
				}

				double distance =
						distanceCalculator.calculate(
								from.getLocation().getLatitude(),
								from.getLocation().getLongitude(),
								to.getLocation().getLatitude(),
								to.getLocation().getLongitude());

				if (distance <= MAX_DIRECT_DISTANCE_KM) {
					edges.add(new Edge(to.getHubId(), distance));
				}
			}
			graph.put(from.getHubId(), edges);
		}

		return graph;
	}

	// 경로 복원
	private List<UUID> reconstructPath(Map<UUID, UUID> previous, UUID departureId, UUID arrivalId) {
		List<UUID> path = new ArrayList<>();
		UUID current = arrivalId;

		while (current != null) {
			path.add(current);
			current = previous.get(current);
		}

		Collections.reverse(path);
		return path;
	}

	// 구간별 거리 계산
	private List<Double> calculateSegmentDistances(List<UUID> path, Map<UUID, Hub> hubMap) {
		List<Double> distances = new ArrayList<>();

		for (int i = 0; i < path.size() - 1; i++) {
			Hub from = hubMap.get(path.get(i));
			Hub to = hubMap.get(path.get(i + 1));

			double distance =
					distanceCalculator.calculate(
							from.getLocation().getLatitude(),
							from.getLocation().getLongitude(),
							to.getLocation().getLatitude(),
							to.getLocation().getLongitude());

			distances.add(distance);
		}

		return distances;
	}

	// Dijkstra용 노드
	private record Node(UUID hubId, Double distance) {}

	// 그래프 간선
	private record Edge(UUID toHubId, Double distance) {}
}
