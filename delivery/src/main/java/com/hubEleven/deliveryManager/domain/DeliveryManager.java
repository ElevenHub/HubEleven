package com.hubEleven.deliveryManager.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
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
			Long id, UUID hubId, String slackId, DeliveryType deliveryType, int deliveryOrder) {
		return new DeliveryManager(id, hubId, slackId, deliveryType, deliveryOrder);
	}
}
