package com.hubEleven.hub.domain.model;

import com.hubEleven.hub.domain.event.HubCreatedEvent;
import com.hubEleven.hub.domain.event.HubDeletedEvent;
import com.hubEleven.hub.domain.event.HubLocationChangedEvent;
import com.hubEleven.hub.domain.vo.Location;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "p_hub")
public class Hub {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID hubId;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Embedded private Location location; // address, latitude, longitude

	@Column(name = "region_code", nullable = false, length = 50)
	private String regionCode;

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

	@Transient @Builder.Default
	private final List<Object> domainEvents = new ArrayList<>(); // 트랜잭션에서 발생하는 이벤트 추가할 리스트

	public static Hub create(
			String name,
			String address,
			Double latitude,
			Double longitude,
			String regionCode,
			Long createdBy) {

		Hub hub =
				Hub.builder()
						.name(name)
						.location(Location.of(address, latitude, longitude))
						.regionCode(regionCode)
						.createdAt(LocalDateTime.now())
						.createdBy(createdBy)
						.build();
		hub.registerEvent(new HubCreatedEvent(hub.hubId));

		return hub;
	}

	public static Hub createNoEvent(
			String name,
			String address,
			Double latitude,
			Double longitude,
			String regionCode,
			Long createdBy) {

		Hub hub =
				Hub.builder()
						.name(name)
						.location(Location.of(address, latitude, longitude))
						.regionCode(regionCode)
						.createdAt(LocalDateTime.now())
						.createdBy(createdBy)
						.build();

		return hub;
	}

	public void update(
			String name,
			String address,
			Double latitude,
			Double longitude,
			String regionCode,
			Long updatedBy) {

		boolean locationChanged = false;

		if (name != null) {
			this.name = name;
		}

		if (address != null || latitude != null || longitude != null) {
			String newAddress = address != null ? address : this.location.getAddress();
			Double newLatitude = latitude != null ? latitude : this.location.getLatitude();
			Double newLongitude = longitude != null ? longitude : this.location.getLongitude();

			Location newLocation = Location.of(newAddress, newLatitude, newLongitude);

			if (!this.location.equals(newLocation)) {
				this.location = newLocation;
				locationChanged = true;
			}
		}

		if (regionCode != null) {
			this.regionCode = regionCode;
		}

		this.updatedAt = LocalDateTime.now();
		this.updatedBy = updatedBy;

		if (locationChanged) {
			registerEvent(new HubLocationChangedEvent(this.hubId, this.location));
		}
	}

	public void softDelete(Long deletedBy) {
		this.deletedAt = LocalDateTime.now();
		this.deletedBy = deletedBy;

		registerEvent(new HubDeletedEvent(this.hubId));
	}

	public boolean isDeleted() {
		return deletedAt != null;
	}

	protected void registerEvent(Object event) {
		this.domainEvents.add(event);
	}

	@DomainEvents
	public List<Object> getDomainEvents() {
		return Collections.unmodifiableList(domainEvents);
	}

	@AfterDomainEventPublication
	public void clearDomainEvents() {
		this.domainEvents.clear();
	}
}
