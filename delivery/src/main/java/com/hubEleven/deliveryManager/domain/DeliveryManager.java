package com.hubEleven.deliveryManager.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryManager {

	@Id private Long deliveryManagerId; // userId를 받아올 것임

	private UUID hubId;

	private String slackId;

	@Enumerated(EnumType.STRING)
	private DeliveryType deliveryType;

	@Column(updatable = false, nullable = false)
	private int deliveryOrder;

	private LocalDateTime lastDeliveryTime;

	private DeliveryManager(
			Long deliveryManagerId,
			UUID hubId,
			String slackId,
			DeliveryType deliveryType,
			int deliveryOrder) {
		this.deliveryManagerId = deliveryManagerId;
		this.hubId = hubId;
		this.slackId = slackId;
		this.deliveryType = deliveryType;
		this.deliveryOrder = deliveryOrder;
	}

	public static DeliveryManager create(
			Long deliveryManagerId,
			UUID hubId,
			String slackId,
			DeliveryType deliveryType,
			int deliveryOrder) {
		return new DeliveryManager(deliveryManagerId, hubId, slackId, deliveryType, deliveryOrder);
	}

	public void recordDeliveryTime() {
		this.lastDeliveryTime = LocalDateTime.now();
	}

	// 임시
	public void softDelete(Long deletedBy) {}
}
