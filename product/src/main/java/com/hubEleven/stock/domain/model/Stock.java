package com.hubEleven.stock.domain.model;

import com.commonLib.common.model.BaseEntity;
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

@Getter
@Entity
@Table(name = "p_stock")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "stock_id", nullable = false)
	private UUID stockId;

	@Column(name = "product_id", nullable = false)
	private UUID productId;

	@Column(name = "hub_id", nullable = false)
	private UUID hubId;

	@Column(name = "company_id", nullable = false)
	private UUID companyId;

	@Column(name = "quantity", nullable = false)
	private Long quantity;

	@Builder(access = AccessLevel.PRIVATE)
	private Stock(Long quantity, UUID productId, UUID companyId, UUID hubId) {
		this.quantity = quantity;
		this.productId = productId;
		this.companyId = companyId;
		this.hubId = hubId;
	}

	public static Stock create(Long quantity, UUID productId, UUID companyId, UUID hubId) {
		return Stock.builder()
				.quantity(quantity)
				.productId(productId)
				.companyId(companyId)
				.hubId(hubId)
				.build();
	}

	public void decreaseQuantity(Long decreaseQuantity) {
		if (decreaseQuantity != null && decreaseQuantity > 0 && this.quantity >= decreaseQuantity) {
			this.quantity -= decreaseQuantity;
		}
	}

	public void restoreQuantity(Long restoreQuantity) {
		if (restoreQuantity != null && restoreQuantity > 0) {
			this.quantity += restoreQuantity;
		}
	}
}
