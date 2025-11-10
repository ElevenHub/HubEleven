package com.hubEleven.order.application.service;

import static com.hubEleven.order.domain.exception.OrderErrorCode.PRODUCT_NOT_FOUND;
import static com.hubEleven.order.domain.exception.OrderErrorCode.RECIPIENT_COMPANY_NOT_FOUND;
import static com.hubEleven.order.domain.exception.OrderErrorCode.REQUESTOR_COMPANY_NOT_FOUND;

import com.hubEleven.common.exception.GlobalException;
import com.hubEleven.order.application.dto.OrderResult;
import com.hubEleven.order.domain.model.Order;
import com.hubEleven.order.domain.repository.OrderRepository;
import com.hubEleven.order.infrastructure.client.CompanyFeignClient;
import com.hubEleven.order.infrastructure.client.CompanyFeignClient.CompanyResponse;
import com.hubEleven.order.infrastructure.client.ProductFeignClient;
import com.hubEleven.order.infrastructure.client.ProductFeignClient.ProductResponse;
import com.hubEleven.order.presentation.dto.request.OrderRequests;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductFeignClient productFeignClient;
    private final CompanyFeignClient companyFeignClient;

    @Override
    @Transactional
    public OrderResult create(OrderRequests.Create request) {

        // 요청 업체 존재 여부 확인
        try {
            CompanyResponse company = companyFeignClient.getCompany(request.requestorCompanyId());
            if (company == null) {
                throw new GlobalException(REQUESTOR_COMPANY_NOT_FOUND);
            }
        } catch (FeignException.NotFound e) {
            throw new GlobalException(REQUESTOR_COMPANY_NOT_FOUND);
        }

        // 수령 업체 존재 여부 확인
        try {
            CompanyResponse company = companyFeignClient.getCompany(request.recipientCompanyId());
            if (company == null) {
                throw new GlobalException(RECIPIENT_COMPANY_NOT_FOUND);
            }
        } catch (FeignException.NotFound e) {
            throw new GlobalException(RECIPIENT_COMPANY_NOT_FOUND);
        }

        // 상품 존재 여부 확인
        try {
            ProductResponse product = productFeignClient.getProduct(request.productId());
            if (product == null) {
                throw new GlobalException(PRODUCT_NOT_FOUND);
            }
        } catch (FeignException.NotFound e) {
            throw new GlobalException(PRODUCT_NOT_FOUND);
        }

        // 주문 생성 및 저장
        Order order = Order.create(
                request.requestorCompanyId(),
                request.recipientCompanyId(),
                request.productId(),
                request.deliveryId(),
                request.quantity(),
                request.note());

        Order savedOrder = orderRepository.save(order);

        return OrderResult.from(savedOrder);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResult> searchOrders(String keyword, Pageable pageable) {

        Page<Order> orders = orderRepository.searchOrders(keyword, pageable);

        return orders.map(OrderResult::from);
    }
}