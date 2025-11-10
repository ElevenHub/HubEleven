package com.hubEleven.order.application.dto;

import com.hubEleven.order.domain.model.Order;
import java.util.UUID;

public record OrderResult(
        UUID oderId,
        UUID requestorCompanyId,
        UUID recipientCompanyId,
        UUID productId,
        UUID deliveryId,
        Long quantity,
        String note) {

    public static OrderResult from(Order order) {
        return new OrderResult(
                order.getOrderId(),
                order.getRequestorCompanyId(),
                order.getRecipientCompanyId(),
                order.getProductId(),
                order.getDeliveryId(),
                order.getQuantity(),
                order.getNote());
    }
}
