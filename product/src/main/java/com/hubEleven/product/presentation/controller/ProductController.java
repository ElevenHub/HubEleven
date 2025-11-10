package com.hubEleven.product.presentation.controller;

import com.hubEleven.common.request.CommonPageRequest;
import com.hubEleven.common.response.ApiResponse;
import com.hubEleven.common.response.ApiResponseEntity;
import com.hubEleven.common.response.CommonPageResponse;
import com.hubEleven.common.utils.PagingUtils;
import com.hubEleven.product.application.dto.ProductResult;
import com.hubEleven.product.application.service.ProductService;
import com.hubEleven.product.presentation.dto.request.ProductRequests;
import com.hubEleven.product.presentation.dto.response.ProductResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO @AuthenticationPrincipal 권한로직
@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@PostMapping
	public ResponseEntity<ApiResponse<ProductResponse>> create(
			@Valid @RequestBody ProductRequests.Create request) {

		ProductResult result = productService.create(request);

		return ApiResponseEntity.success(ProductResponse.from(result));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<CommonPageResponse<ProductResponse>>> getProducts(
			CommonPageRequest request) {

		// Service 계층에서 Product 도메인 엔티티를 ProductResult DTO로 변환하여 조회
		Page<ProductResult> products =
				productService.searchProducts(
						request.keyword(), // 검색 키워드
						request.toPageable()); // 페이징 정보

		// Application 계층의 ProductResult를 Presentation 계층의 ProductResponse로 변환
		CommonPageResponse<ProductResponse> response =
				PagingUtils.convert(products, ProductResponse::from);

		return ApiResponseEntity.success(response);
	}

	@GetMapping("/{productId}")
	public ResponseEntity<ApiResponse<ProductResponse>> getProductDetail(
			@PathVariable UUID productId) {

		ProductResult result = productService.getProduct(productId);

		return ApiResponseEntity.success(ProductResponse.from(result));
	}

	@PatchMapping("/{productId}")
	public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
			@PathVariable UUID productId, @Valid @RequestBody ProductRequests.Update request) {

		ProductResult result = productService.updateProduct(productId, request);

		return ApiResponseEntity.success(ProductResponse.from(result));
	}

	@DeleteMapping("/{productId}")
	public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable UUID productId, Long userId) {

		productService.deleteProduct(productId, userId);

		return ApiResponseEntity.success(null);
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<CommonPageResponse<ProductResponse>>> searchProducts(
			CommonPageRequest request) {

		Page<ProductResult> products =
				productService.searchProducts(request.keyword(), request.toPageable());

		CommonPageResponse<ProductResponse> response =
				PagingUtils.convert(products, ProductResponse::from);

		return ApiResponseEntity.success(response);
	}
}
