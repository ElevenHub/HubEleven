package com.hubEleven.product.application.service;

import static com.hubEleven.product.domain.exception.ProductErrorCode.COMPANY_NOT_FOUND;
import static com.hubEleven.product.domain.exception.ProductErrorCode.HUB_NOT_FOUND;
import static com.hubEleven.product.domain.exception.ProductErrorCode.PRODUCT_DUPLICATED;

import com.hubEleven.common.exception.GlobalException;
import com.hubEleven.product.application.dto.ProductResult;
import com.hubEleven.product.domain.model.Product;
import com.hubEleven.product.domain.repository.ProductRepository;
import com.hubEleven.product.infrastructure.client.CompanyFeignClient;
import com.hubEleven.product.infrastructure.client.CompanyFeignClient.CompanyResponse;
import com.hubEleven.product.infrastructure.client.HubFeignClient;
import com.hubEleven.product.infrastructure.client.HubFeignClient.HubResponse;
import com.hubEleven.product.presentation.dto.request.ProductRequests;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CompanyFeignClient companyFeignClient;
    private final HubFeignClient hubFeignClient;

    @Override
    @Transactional
    public ProductResult create(ProductRequests.Create request) {

        // 회사 존재 여부 확인
        try {
            CompanyResponse company = companyFeignClient.getCompany(request.companyId());
            if (company == null) {
                throw new GlobalException(COMPANY_NOT_FOUND);
            }
        } catch (FeignException.NotFound e) {
            throw new GlobalException(COMPANY_NOT_FOUND);
        }

        // 허브 존재 여부 확인
        try {
            HubResponse hub = hubFeignClient.getHub(request.hubId());
            if (hub == null) {
                throw new GlobalException(HUB_NOT_FOUND);
            }
        } catch (FeignException.NotFound e) {
            throw new GlobalException(HUB_NOT_FOUND);
        }

        // 중복 제품명 확인
        boolean isDuplicated = productRepository.existsByCompanyIdAndNameAndHubId(
                request.companyId(),
                request.name(),
                request.hubId()
        );

        if (isDuplicated) {
            throw new GlobalException(PRODUCT_DUPLICATED);
        }

        // 제품 생성 및 저장
        Product product = Product.create(
                request.name(),
                request.companyId(),
                request.hubId()
        );

        Product savedProduct = productRepository.save(product);

        return ProductResult.from(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResult> searchProducts(String keyword, Pageable pageable) {
        Page<Product> products = productRepository.searchProducts(keyword, pageable);
        return products.map(ProductResult::from);
    }
}