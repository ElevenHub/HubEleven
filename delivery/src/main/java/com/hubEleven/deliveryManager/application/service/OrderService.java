package com.hubEleven.deliveryManager.application.service;

import com.commonLib.common.exception.GlobalException;
import com.hubEleven.deliveryManager.domain.exception.DeliveryManagerErrorCode;
import com.hubEleven.deliveryManager.infrastructure.client.OrderFeignClient;
import com.hubEleven.deliveryManager.infrastructure.dto.OrderResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {
	private final OrderFeignClient orderFeignClient;

	public OrderService(OrderFeignClient orderFeignClient) {
		this.orderFeignClient = orderFeignClient;
	}

	public OrderResponse getOrder(UUID orderId) {

		ResponseEntity<OrderResponse> response = orderFeignClient.getOrderDetail(orderId);

		if (response == null) {
			throw new GlobalException(DeliveryManagerErrorCode.ORDER_NOT_FOUND);
		}
		log.info("order 정보 받아옴");
		return response.getBody();
	}
}
