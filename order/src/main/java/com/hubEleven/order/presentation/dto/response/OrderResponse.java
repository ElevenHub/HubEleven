package com.hubEleven.order.presentation.dto.response;

import com.hubEleven.order.application.dto.OrderResult;
import java.util.UUID;

public record OrderResponse(
        UUID oderId,
        UUID requestorCompanyId,
        UUID recipientCompanyId,
        UUID productId,
        UUID deliveryId,
        Long quantity,
        String note) {

    public static OrderResponse from(OrderResult orderResult) {
        return new OrderResponse(
                orderResult.oderId(),
                orderResult.requestorCompanyId(),
                orderResult.recipientCompanyId(),
                orderResult.productId(),
                orderResult.deliveryId(),
                orderResult.quantity(),
                orderResult.note());
    }
}
