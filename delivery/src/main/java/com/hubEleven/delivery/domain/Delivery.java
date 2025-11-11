package com.hubEleven.delivery.domain;

import com.commonLib.common.annotation.SoftDeletable;
import com.commonLib.common.model.BaseEntity;
import com.hubEleven.deliveryRoute.domain.DeliveryRoute;
import jakarta.persistence.*;
import java.util.ArrayList;
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
	@Column(name = "delivery_id", columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(name = "order_id", nullable = false, columnDefinition = "BINARY(16)")
	private UUID orderId;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private DeliveryStatus status;

	@Column(name = "from_hub_id", nullable = false, columnDefinition = "BINARY(16)")
	private UUID fromHubId;

	@Column(name = "to_hub_id", nullable = false, columnDefinition = "BINARY(16)")
	private UUID toHubId;

	@Column(name = "recipient_name", nullable = false)
	private String recipientName;

	@Column(name = "recipient_slack_id", nullable = false)
	private String recipientSlackId;

	@Column(name = "delivery_manager_id", nullable = false)
	private Long deliveryManagerId;

	// new Delivery()를 외부에서 못하게
	protected Delivery() {}

	@OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<DeliveryRoute> deliveryRoutes = new ArrayList<>();

	// 배송 경로 추가 (연관 관계 주인 설정)
	public void addRoute(DeliveryRoute deliveryRoute) {
		this.deliveryRoutes.add(deliveryRoute);
		deliveryRoute.setDelivery(this);
	}

	// 생성
	public static Delivery create(
			UUID orderId,
			DeliveryStatus status,
			UUID fromHubId,
			UUID toHubId,
			String recipientName,
			String recipientSlackId,
			Long deliveryManagerId) {
		Delivery delivery = new Delivery();
		delivery.id = UUID.randomUUID();
		delivery.orderId = orderId;
		delivery.status = status;
		delivery.fromHubId = fromHubId;
		delivery.toHubId = toHubId;
		delivery.recipientName = recipientName;
		delivery.recipientSlackId = recipientSlackId;
		delivery.deliveryManagerId = deliveryManagerId;
		return delivery;
	}

	// 수정
	public void update(
			DeliveryStatus status,
			UUID fromHubId,
			UUID toHubId,
			String recipientName,
			String recipientSlackId,
			Long deliveryManagerId) {
		if (status != null) this.status = status;
		if (fromHubId != null) this.fromHubId = fromHubId;
		if (toHubId != null) this.toHubId = toHubId;
		if (recipientName != null) this.recipientName = recipientName;
		if (recipientSlackId != null) this.recipientSlackId = recipientSlackId;
		if (deliveryManagerId != null) this.deliveryManagerId = deliveryManagerId;
	}
}
