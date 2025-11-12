package com.hubEleven.deliveryRoute.domain;

import com.commonLib.common.annotation.SoftDeletable;
import com.commonLib.common.model.BaseEntity;
import com.hubEleven.delivery.domain.Delivery;
import com.hubEleven.delivery.domain.DeliveryStatus;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

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
	private UUID deliveryRouteId;

	@ManyToOne
	@JoinColumn(name = "delivery_id", nullable = false)
	private Delivery delivery;

	@Column(name = "seq", nullable = false)
	private Integer seq;

	@Column(name = "from_hub_id", nullable = false)
	private UUID fromHubId;

	@Column(name = "to_hub_id", nullable = false)
	private UUID toHubId;

	@Column(name = "delivery_manager_id", nullable = false)
	private Long deliveryManagerId;

	@Column(name = "status", nullable = false)
	private DeliveryStatus status;

	@Column(name = "expected_distance", nullable = false)
	private Double expectedDistance;

	@Column(name = "expected_duration", nullable = false)
	private Long expectedDuration;

	@Column(name = "actual_distance")
	private Double actualDistance;

	@Column(name = "actual_duration")
	private Long actualDuration;
}
