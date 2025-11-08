package com.hubEleven.company.domain.model;

import com.hubEleven.common.annotation.SoftDeletable;
import com.hubEleven.common.model.BaseEntity;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
		name = "p_company",
		uniqueConstraints = {
			@UniqueConstraint(
					name = "uq_company",
					columnNames = {"hub_id", "company_name"})
		},
		indexes = {
			@Index(name = "idx_company_hub", columnList = "hub_id"),
			@Index(name = "idx_company_created_at", columnList = "created_at")
		})
@Getter
@NoArgsConstructor
@SoftDeletable
public class Company extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "company_id")
	private UUID companyId;

	@Column(name = "hub_id", nullable = false)
	private UUID hubId;

	@Column(name = "company_name", nullable = false, length = 150)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "company_type", nullable = false)
	private CompanyType companyType;

	@Enumerated(EnumType.STRING)
	@Column(name = "company_status", nullable = false)
	private CompanyStatus status = CompanyStatus.ACTIVE;

	@Column(name = "slack_id", length = 100)
	private String slackId;

	@Column(name = "company_address", nullable = false, length = 300)
	private String address;

	public static Company create(
			UUID hubId, String name, CompanyType type, String slackId, String address) {
		Company c = new Company();
		c.hubId = hubId;
		c.name = name;
		c.companyType = type;
		c.status = CompanyStatus.ACTIVE;
		c.slackId = slackId;
		c.address = address;
		return c;
	}

	public void update(String name, String address, String slackId) {
		if (name != null && !name.isBlank()) {
			this.name = name;
		}
		if (address != null && !address.isBlank()) {
			this.address = address;
		}
		if (slackId != null && !slackId.isBlank()) {
			this.slackId = slackId;
		}
	}

	public void changeType(CompanyType type) {
		if (type != null) {
			this.companyType = type;
		}
	}

	public void changeStatus(CompanyStatus status) {
		if (status != null) {
			this.status = status;
		}
	}
}
