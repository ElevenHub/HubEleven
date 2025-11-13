package com.hubEleven.delivery.infrastructure.service;

import com.commonLib.common.exception.GlobalException;
import com.commonLib.common.response.ApiResponse;
import com.hubEleven.delivery.domain.DeliveryErrorCode;
import com.hubEleven.delivery.infrastructure.client.OrderFeignClient;
import com.hubEleven.delivery.infrastructure.dto.OrderFeignResponseDto;
import feign.FeignException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderFeignService {
	private final OrderFeignClient orderFeignClient;

	public ApiResponse<OrderFeignResponseDto> getOrderInfo(UUID orderId) {
		try {
			return orderFeignClient.getOrder(orderId);
		} catch (FeignException.NotFound e) {
			// 주문 없음
			throw new GlobalException(DeliveryErrorCode.ORDER_NOT_FOUND);
		} catch (FeignException e) {
			// 기타 Feign 문제
			throw new GlobalException(DeliveryErrorCode.FEIGN_ERROR);
		}
	}
}
