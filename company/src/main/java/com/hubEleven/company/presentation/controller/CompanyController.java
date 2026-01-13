package com.hubEleven.company.presentation.controller;

import com.commonLib.common.request.CommonPageRequest;
import com.commonLib.common.response.ApiResponse;
import com.commonLib.common.response.ApiResponseEntity;
import com.commonLib.common.response.CommonPageResponse;
import com.hubEleven.company.application.command.ChangeCompanyStatusCommand;
import com.hubEleven.company.application.command.CreateCompanyCommand;
import com.hubEleven.company.application.command.SearchCompanyCommand;
import com.hubEleven.company.application.command.UpdateCompanyCommand;
import com.hubEleven.company.application.dto.response.CompanyResult;
import com.hubEleven.company.application.service.CompanyService;
import com.hubEleven.company.domain.vo.CompanyStatus;
import com.hubEleven.company.domain.vo.CompanyType;
import com.hubEleven.company.presentation.dto.request.ChangeCompanyStatusRequest;
import com.hubEleven.company.presentation.dto.request.CreateCompanyRequest;
import com.hubEleven.company.presentation.dto.request.UpdateCompanyRequest;
import com.hubEleven.company.presentation.dto.response.CompanyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Company", description = "업체 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/companies")
public class CompanyController {

	private final CompanyService companyService;

	@Operation(summary = "업체 생성 API", description = "새로운 업체를 생성한다.")
	@PostMapping
	public ResponseEntity<ApiResponse<CompanyResponse>> create(
			@Valid @RequestBody CreateCompanyRequest req,
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Role") String userRole) {

		log.info("업체 생성 요청 - hubId:{}, name:{}", req.hubId(), req.name());

		CompanyResult result =
				companyService.createCompany(
						new CreateCompanyCommand(
								req.hubId(), req.name(), req.type(), req.slackId(), req.address()),
						userId,
						userRole);

		log.info("업체 생성 성공 - companyId:{}", result.companyId());
		return ApiResponseEntity.success(CompanyResponse.from(result));
	}

	@Operation(summary = "업체 수정 API", description = "업체의 기본 정보를 수정한다.")
	@PatchMapping("/{companyId}")
	public ResponseEntity<ApiResponse<CompanyResponse>> updateCompany(
			@PathVariable UUID companyId,
			@Valid @RequestBody UpdateCompanyRequest req,
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Role") String userRole) {

		log.info("업체 수정 요청 - companyId:{}, name:{}", companyId, req.name());

		CompanyResult result =
				companyService.updateCompany(
						new UpdateCompanyCommand(
								companyId, req.name(), req.type(), req.slackId(), req.address()),
						userId,
						userRole);

		log.info("업체 수정 성공 - companyId:{}, name:{}", companyId, req.name());
		return ApiResponseEntity.success(CompanyResponse.from(result));
	}

	@Operation(summary = "업체 단건 조회 API", description = "업체 ID로 업체를 조회한다.")
	@GetMapping("/{companyId}")
	public ResponseEntity<ApiResponse<CompanyResponse>> getCompany(@PathVariable UUID companyId) {
		log.debug("업체 단건 조회 요청 - companyId:{}", companyId);

		CompanyResult dto = companyService.getCompany(companyId);

		log.debug("업체 단건 조회 성공 - companyId:{}", companyId);
		return ApiResponseEntity.success(CompanyResponse.from(dto));
	}

	@Operation(summary = "업체 목록 조회/검색 API", description = "업체 목록을 조회하거나 검색 조건으로 필터링한다.")
	@GetMapping
	public ResponseEntity<ApiResponse<CommonPageResponse<CompanyResponse>>> getCompanyList(
			@Valid CommonPageRequest pageReq,
			@RequestParam(required = false) UUID hubId,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) CompanyType type,
			@RequestParam(required = false) CompanyStatus status) {

		log.debug("업체 목록/검색 요청 - hubId:{}, name:{}, type:{}, status:{}, page: {}, size: {}",
				hubId, name, type, status, pageReq.page(), pageReq.size());

		var page =
				companyService.searchCompany(new SearchCompanyCommand(hubId, name, type, status), pageReq);

		var mapped =
				new CommonPageResponse<>(
						page.content().stream().map(CompanyResponse::from).toList(),
						page.page(),
						page.size(),
						page.totalElements(),
						page.totalPages(),
						page.first(),
						page.last());

		log.debug("업체 목록/검색 성공 - totalElements: {}, totalPages: {}",
				page.totalElements(), page.totalPages());
		return ApiResponseEntity.success(mapped);
	}

	@Operation(summary = "업체 상태 변경 API", description = "업체의 상태를 변경한다.")
	@PatchMapping("/{companyId}/status")
	public ResponseEntity<ApiResponse<CompanyResponse>> updateCompanyStatus(
			@PathVariable UUID companyId,
			@Valid @RequestBody ChangeCompanyStatusRequest req,
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Role") String userRole) {

		log.info("업체 상태 변경 요청 - companyId:{} status:{}", companyId, req.status());

		CompanyResult result =
				companyService.changeStatus(
						new ChangeCompanyStatusCommand(companyId, req.status()), userId, userRole);

		log.info("업체 상태 변경 성공 - companyId:{} status:{}", companyId, req.status());
		return ApiResponseEntity.success(CompanyResponse.from(result));
	}

	@Operation(summary = "업체 삭제 API", description = "업체를 삭제한다.")
	@DeleteMapping("/{companyId}")
	public ResponseEntity<ApiResponse<Object>> deleteCompany(
			@PathVariable UUID companyId,
			@RequestHeader("X-User-Id") Long userId,
			@RequestHeader("X-User-Role") String userRole) {

		log.info("업체 삭제 요청 - companyId: {}", companyId);

		companyService.deleteCompany(companyId, userId, userRole);

		log.info("업체 삭제 성공 - companyId: {}", companyId);
		return ApiResponseEntity.success(null);
	}
}
