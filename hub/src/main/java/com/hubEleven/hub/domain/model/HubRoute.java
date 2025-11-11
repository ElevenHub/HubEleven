package com.hubEleven.hub.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "p_hub_route")
public class HubRoute {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID hubRouteId;

	@Column(name = "from_hub_id", nullable = false)
	private UUID fromHubId;

	@Column(name = "to_hub_id", nullable = false)
	private UUID toHubId;

	@Column(name = "total_distance", nullable = false)
	private Double totalDistance;

	@Column(name = "total_duration", nullable = false)
	private Integer totalDuration;

	@OneToMany(mappedBy = "hubRoute", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("sequence ASC")
	@Builder.Default
	private List<HubRouteSegment> segments = new ArrayList<>();

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "created_by", nullable = false)
	private Long createdBy;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "updated_by")
	private Long updatedBy;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Column(name = "deleted_by")
	private Long deletedBy;

	public static HubRoute create(
			UUID fromHubId,
			UUID toHubId,
			Double totalDistance,
			Integer totalDuration,
			List<HubRouteSegment> segments,
			Long createdBy) {

		HubRoute hubRoute =
				HubRoute.builder()
						.fromHubId(fromHubId)
						.toHubId(toHubId)
						.totalDistance(totalDistance)
						.totalDuration(totalDuration)
						.createdAt(LocalDateTime.now())
						.createdBy(createdBy)
						.build();

		for (HubRouteSegment segment : segments) {
			hubRoute.addSegment(segment);
		}

		return hubRoute;
	}

	public void addSegment(HubRouteSegment segment) {
		this.segments.add(segment);
		segment.setHubRoute(this);
	}

	public void softDelete(Long deletedBy) {
		this.deletedAt = LocalDateTime.now();
		this.deletedBy = deletedBy;
	}

	public boolean isDeleted() {
		return deletedAt != null;
	}
}
