package com.hubEleven.stock.domain.model;

import static com.hubEleven.product.domain.exception.ProductErrorCode.COMPANY_NOT_FOUND;
import static com.hubEleven.product.domain.exception.ProductErrorCode.HUB_NOT_FOUND;
import static com.hubEleven.product.domain.exception.ProductErrorCode.PRODUCT_NOT_FOUND;
import static com.hubEleven.stock.domain.exception.StockErrorCode.INVALID_STOCK_QUANTITY;

import com.commonLib.common.exception.GlobalException;
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

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Builder(access = AccessLevel.PRIVATE)
    private Stock(UUID productId, UUID companyId, UUID hubId, int quantity) {
        this.productId = productId;
        this.companyId = companyId;
        this.hubId = hubId;
        this.quantity = quantity;
    }

    public static Stock create(
            UUID productId,
            UUID companyId,
            UUID hubId,
            int quantity
    ) {
        validateProductId(productId);
        validateCompanyId(companyId);
        validateHubId(hubId);

        return Stock.builder()
                .productId(productId)
                .companyId(companyId)
                .hubId(hubId)
                .quantity(quantity)
                .build();
    }

    private static void validateProductId(UUID productId) {
        if (productId == null) {
            throw new GlobalException(PRODUCT_NOT_FOUND);
        }
    }

    private static void validateCompanyId(UUID companyId) {
        if (companyId == null) {
            throw new GlobalException(COMPANY_NOT_FOUND);
        }
    }

    private static void validateHubId(UUID hubId) {
        if (hubId == null) {
            throw new GlobalException(HUB_NOT_FOUND);
        }
    }

    public void decreaseQuantity(int decreaseQuantity) {
        validateDecreaseQuantity(decreaseQuantity);

        if (decreaseQuantity > 0 && this.quantity >= decreaseQuantity) {
            this.quantity -= decreaseQuantity;
        }
    }

    public void restoreQuantity(int restoreQuantity) {
        validateRestoreQuantity(restoreQuantity);

        if (restoreQuantity > 0) {
            this.quantity += restoreQuantity;
        }
    }

    public void validateDecreaseQuantity(int decreaseQuantity) {
        if (decreaseQuantity < 0) {
            throw new GlobalException(INVALID_STOCK_QUANTITY);
        }
    }

    private void validateRestoreQuantity(int restoreQuantity) {
        if (restoreQuantity < 0) {
            throw new GlobalException(INVALID_STOCK_QUANTITY);
        }
    }

}
