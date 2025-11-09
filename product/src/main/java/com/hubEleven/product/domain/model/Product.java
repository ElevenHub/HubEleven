package com.hubEleven.product.domain.model;

import com.hubEleven.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

@Getter
@Entity
@Table(name = "p_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete // soft delete 커스텀 어노테이션
public class Product extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "product_id", nullable = false)
	private UUID productId;

	@Column(name = "name", nullable = false, length = 20)
	private String name;

	@Column(name = "company_id", nullable = false)
	private UUID companyId;

	@Column(name = "hub_id", nullable = false)
	private UUID hubId;

	@Builder(access = AccessLevel.PRIVATE)
	private Product(String name, UUID companyId, UUID hubId) {
		this.name = name;
		this.companyId = companyId;
		this.hubId = hubId;
	}

	public static Product create(String name, UUID companyId, UUID hubId) {
		return Product.builder().name(name).companyId(companyId).hubId(hubId).build();
	}

	// 제품명 수정 메서드
	public void update(String name) {
		if (name != null && !name.isBlank()) {
			this.name = name;
		}
	}
}
