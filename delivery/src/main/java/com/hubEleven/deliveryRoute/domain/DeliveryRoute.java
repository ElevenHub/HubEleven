package com.hubEleven.deliveryRoute.domain;

import com.commonLib.common.annotation.SoftDeletable;
import com.commonLib.common.model.BaseEntity;
import com.hubEleven.delivery.domain.Delivery;
import com.hubEleven.delivery.domain.DeliveryStatus;
import com.hubEleven.delivery.infrastructure.dto.HubRouteSegmentResponseDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SoftDeletable
@Table(name = "p_delivery_route")
public class DeliveryRoute extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "delivery_route_id", nullable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delivery_id", columnDefinition = "BINARY(16)")
	private Delivery delivery;

	@Column(name = "seq", nullable = false)
	private Integer seq;

	@Column(name = "from_hub_id", nullable = false, columnDefinition = "BINARY(16)")
	private UUID fromHubId;

	@Column(name = "to_hub_id", nullable = false, columnDefinition = "BINARY(16)")
	private UUID toHubId;

	@Column(name = "delivery_manager_id", nullable = false)
	private Long deliveryManagerId;

	@Column(name = "status", nullable = false)
	private DeliveryStatus status;

	@Column(name = "expected_distance", nullable = false)
	private Double expectedDistance;

	@Column(name = "expected_duration", nullable = false)
	private Integer expectedDuration;

	@Column(name = "actual_distance")
	private Double actualDistance;

	@Column(name = "actual_duration")
	private Integer actualDuration;

	// 배송 경로 생성
	public static DeliveryRoute create(
            HubRouteSegmentResponseDto hubRouteSegmentResponseDto, int seq, Long deliveryManagerId) {
		DeliveryRoute deliveryRoute = new DeliveryRoute();
		deliveryRoute.id = UUID.randomUUID();
		deliveryRoute.seq = seq;
		deliveryRoute.fromHubId = hubRouteSegmentResponseDto.fromHubId();
		deliveryRoute.toHubId = hubRouteSegmentResponseDto.toHubId();
		deliveryRoute.deliveryManagerId = deliveryManagerId;
		deliveryRoute.status = DeliveryStatus.HUB_WAITHING;
		deliveryRoute.expectedDistance = hubRouteSegmentResponseDto.distance();
		deliveryRoute.expectedDuration = hubRouteSegmentResponseDto.duration();
		return deliveryRoute;
	}

	// 배송 경로 수정
	public void update(
			Integer seq,
			UUID toHubId,
			Long deliveryManagerId,
			DeliveryStatus status,
			Double expectedDistance,
			Integer expectedDuration,
			Double actualDistance,
			Integer actualDuration) {
		if (seq != null) this.seq = seq;
		if (toHubId != null) this.toHubId = toHubId;
		if (deliveryManagerId != null) this.deliveryManagerId = deliveryManagerId;
		if (status != null) this.status = status;
		if (expectedDistance != null) this.expectedDistance = expectedDistance;
		if (expectedDuration != null) this.expectedDuration = expectedDuration;
		if (actualDistance != null) this.actualDistance = actualDistance;
		if (actualDuration != null) this.actualDuration = actualDuration;
	}
}
