package com.hubEleven.hub.domain.model;

import com.hubEleven.hub.domain.vo.Location;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	public static Hub create(
			String name,
			String address,
			Double latitude,
			Double longitude,
			String regionCode,
			Long createdBy) {
		return Hub.builder()
				.name(name)
				.location(Location.of(address, latitude, longitude))
				.regionCode(regionCode)
				.createdAt(LocalDateTime.now())
				.createdBy(createdBy)
				.build();
	}

	public void update(
			String name,
			String address,
			Double latitude,
			Double longitude,
			String regionCode,
			Long updatedBy) {

		if (name != null) {
			this.name = name;
		}

		if (address != null || latitude != null || longitude != null) {
			String newAddress = address != null ? address : this.location.getAddress();
			Double newLatitude = latitude != null ? latitude : this.location.getLatitude();
			Double newLongitude = longitude != null ? longitude : this.location.getLongitude();

			this.location = Location.of(newAddress, newLatitude, newLongitude);
		}

		if (regionCode != null) {
			this.regionCode = regionCode;
		}

		this.updatedAt = LocalDateTime.now();
		this.updatedBy = updatedBy;
	}

	public void softDelete(Long deletedBy) {
		this.deletedAt = LocalDateTime.now();
		this.deletedBy = deletedBy;
	}

	public boolean isDeleted() {
		return deletedAt != null;
	}
}
