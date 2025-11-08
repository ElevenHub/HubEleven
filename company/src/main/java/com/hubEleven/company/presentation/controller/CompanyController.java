package com.hubEleven.company.presentation.controller;

import com.hubEleven.common.request.CommonPageRequest;
import com.hubEleven.common.response.ApiResponse;
import com.hubEleven.common.response.ApiResponseEntity;
import com.hubEleven.common.response.CommonPageResponse;
import com.hubEleven.company.application.dto.CompanyDTO;
import com.hubEleven.company.application.service.CompanyAppService;
import com.hubEleven.company.domain.model.CompanyStatus;
import com.hubEleven.company.domain.model.CompanyType;
import com.hubEleven.company.presentation.request.CompanyRequests;
import com.hubEleven.company.presentation.response.CompanyResponse;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO @AuthenticationPrincipal 권한로직
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/companies")
public class CompanyController {

	private final CompanyAppService companyAppService;

	@PostMapping
	public ResponseEntity<ApiResponse<CompanyResponse>> create(
			@Valid @RequestBody CompanyRequests.Create req) {
		CompanyDTO dto = companyAppService.createCompany(req);
		return ApiResponseEntity.success(CompanyResponse.from(dto));
	}

	@PatchMapping("/{companyId}")
	public ResponseEntity<ApiResponse<CompanyResponse>> updateCompany(
			@PathVariable UUID companyId, @Valid @RequestBody CompanyRequests.Update req) {
		CompanyDTO dto = companyAppService.updateCompany(companyId, req);
		return ApiResponseEntity.success(CompanyResponse.from(dto));
	}

	@GetMapping("/{companyId}")
	public ResponseEntity<ApiResponse<CompanyResponse>> getCompany(@PathVariable UUID companyId) {
		CompanyDTO dto = companyAppService.getCompany(companyId);
		return ApiResponseEntity.success(CompanyResponse.from(dto));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<CommonPageResponse<CompanyResponse>>> getCompanyList(
			@Valid CommonPageRequest pageReq) {
		var page = companyAppService.findCompanyList(pageReq);
		var mapped =
				new CommonPageResponse<>(
						page.content().stream().map(CompanyResponse::from).toList(),
						page.page(),
						page.size(),
						page.totalElements(),
						page.totalPages(),
						page.first(),
						page.last());
		return ApiResponseEntity.success(mapped);
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<CommonPageResponse<CompanyResponse>>> search(
			@Valid CommonPageRequest pageReq,
			@RequestParam(required = false) UUID hubId,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) CompanyType type,
			@RequestParam(required = false) CompanyStatus status) {
		var page =
				companyAppService.searchCompany(
						Optional.ofNullable(hubId),
						Optional.ofNullable(name),
						Optional.ofNullable(type),
						Optional.ofNullable(status),
						pageReq);

		var mapped =
				new CommonPageResponse<>(
						page.content().stream().map(CompanyResponse::from).toList(),
						page.page(),
						page.size(),
						page.totalElements(),
						page.totalPages(),
						page.first(),
						page.last());
		return ApiResponseEntity.success(mapped);
	}

	@PatchMapping("/{companyId}/status")
	public ResponseEntity<ApiResponse<CompanyResponse>> updateCompanyStatus(
			@PathVariable UUID companyId, @Valid @RequestBody CompanyRequests.StatusChange req) {
		CompanyDTO dto = companyAppService.changeStatus(companyId, req.status());
		return ApiResponseEntity.success(CompanyResponse.from(dto));
	}

	@DeleteMapping("{companyId}")
	public ResponseEntity<ApiResponse<Object>> deleteCompany(
			@PathVariable("companyId") UUID companyId) {
		companyAppService.deleteCompany(companyId, null);

		return ApiResponseEntity.success(null);
	}
}
