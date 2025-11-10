package com.hubEleven.order.application.service;

import com.hubEleven.order.application.dto.OrderResult;
import com.hubEleven.order.presentation.dto.request.OrderRequests;
import com.hubEleven.order.presentation.dto.request.OrderRequests.Create;
import jakarta.validation.Valid;

public interface OrderService {

    OrderResult create(OrderRequests.Create request);
}
