package com.hubEleven.delivery.domain;

import com.commonLib.common.annotation.SoftDeletable;
import com.commonLib.common.model.BaseEntity;
import com.hubEleven.deliveryRoute.domain.DeliveryRoute;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@SoftDeletable
@Table(name = "p_delivery")
public class Delivery extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "delivery_id", nullable = false)
	private UUID id;

	@Column(name = "order_id", nullable = false)
	private UUID orderId;

	@Column(name = "status", nullable = false)
	private DeliveryStatus status;

	@Column(name = "from_hub_id", nullable = false)
	private UUID fromHubId;

	@Column(name = "to_hub_id", nullable = false)
	private UUID toHubId;

	@Column(name = "recipient_name", nullable = false)
	private String recipientName;

	@Column(name = "recipient_slack_id", nullable = false)
	private String recipientSlackId;

	@Column(name = "delivery_manager_id", nullable = false)
	private Long deliveryManagerId;

	@OneToMany(mappedBy = "delivery")
	private List<DeliveryRoute> deliveryRoutes;
}
