package com.hubEleven.hub.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
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
@Table(name = "p_hub_route")
public class HubRoute {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID hubRouteId;

	@Column(name = "from_hub_id", nullable = false)
	private UUID fromHub;

	@Column(name = "to_hub_id", nullable = false)
	private UUID toHub;

	@Column(name = "distance", precision = 10, scale = 2, nullable = false)
	private BigDecimal distance;

	@Column(name = "duration", nullable = false)
	private Long duration;

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
}
