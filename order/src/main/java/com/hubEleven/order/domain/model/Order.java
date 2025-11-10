package com.hubEleven.order.domain.model;

import com.hubEleven.common.annotation.SoftDeletable;
import com.hubEleven.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDeletable // soft delete 커스텀 어노테이션
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "requestor_company_id", nullable = false)
    private UUID requestorCompanyId;

    @Column(name = "recipient_company_id", nullable = false)
    private UUID recipientCompanyId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "delivery_id", nullable = false)
    private UUID deliveryId;

    @Min(0)
    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "note", length = 500)
    private String note;

    @Builder(access = AccessLevel.PRIVATE)
    private Order(UUID requestorCompanyId, UUID recipientCompanyId, UUID productId,
                  UUID deliveryId, Long quantity, String note) {
        this.requestorCompanyId = requestorCompanyId;
        this.recipientCompanyId = recipientCompanyId;
        this.productId = productId;
        this.deliveryId = deliveryId;
        this.quantity = quantity;
        this.note = note;
    }

    public static Order create(UUID requestorCompanyId, UUID recipientCompanyId, UUID productId,
                               UUID deliveryId, Long quantity, String note) {
        return Order.builder()
                .requestorCompanyId(requestorCompanyId)
                .recipientCompanyId(recipientCompanyId)
                .productId(productId)
                .deliveryId(deliveryId)
                .quantity(quantity)
                .note(note)
                .build();
    }
}
