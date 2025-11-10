package com.hubEleven.hub.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "p_hub_route_segment")
public class HubRouteSegment {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID segmentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hub_route_id", nullable = false)
	private HubRoute hubRoute;

	@Column(name = "from_hub_id", nullable = false)
	private UUID fromHubId;

	@Column(name = "to_hub_id", nullable = false)
	private UUID toHubId;

	@Column(name = "sequence", nullable = false)
	private Integer sequence;

	@Column(name = "distance", nullable = false)
	private Double distance;

	@Column(name = "duration", nullable = false)
	private Integer duration;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "created_by", nullable = false)
	private Long createdBy;

	public static HubRouteSegment create(
			UUID fromHubId,
			UUID toHubId,
			Integer sequence,
			Double distance,
			Integer duration,
			Long createdBy) {

		return HubRouteSegment.builder()
				.fromHubId(fromHubId)
				.toHubId(toHubId)
				.sequence(sequence)
				.distance(distance)
				.duration(duration)
				.createdAt(LocalDateTime.now())
				.createdBy(createdBy)
				.build();
	}

	void setHubRoute(HubRoute hubRoute) {
		this.hubRoute = hubRoute;
	}
}
