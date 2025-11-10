package com.hubEleven.order.application.dto;

import com.hubEleven.order.domain.model.Order;
import java.util.UUID;

public record OrderResult(
        UUID oderId,
        UUID requestorCompanyId,
        UUID recipientCompanyId,
        UUID productId,
        String productName,
        UUID deliveryId,
        Long quantity,
        String note) {

    public static OrderResult from(Order order, String productName) {
        return new OrderResult(
                order.getOrderId(),
                order.getRequestorCompanyId(),
                order.getRecipientCompanyId(),
                order.getProductId(),
                productName,
                order.getDeliveryId(),
                order.getQuantity(),
                order.getNote());
    }
}
