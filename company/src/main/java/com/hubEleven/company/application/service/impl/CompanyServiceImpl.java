package com.hubEleven.company.application.service.impl;

import com.commonLib.common.exception.GlobalException;
import com.commonLib.common.request.CommonPageRequest;
import com.commonLib.common.response.CommonPageResponse;
import com.commonLib.common.utils.PagingUtils;
import com.hubEleven.company.application.command.ChangeCompanyStatusCommand;
import com.hubEleven.company.application.command.CreateCompanyCommand;
import com.hubEleven.company.application.command.SearchCompanyCommand;
import com.hubEleven.company.application.command.UpdateCompanyCommand;
import com.hubEleven.company.application.dto.response.CompanyResult;
import com.hubEleven.company.application.service.CompanyService;
import com.hubEleven.company.application.validator.CompanyValidator;
import com.hubEleven.company.domain.model.Company;
import com.hubEleven.company.domain.repository.CompanyRepository;
import com.hubEleven.company.exception.CompanyErrorCode;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

	private final CompanyRepository companyRepository;
	private final CompanyValidator companyValidator;

	@Transactional
	@Override
	public CompanyResult createCompany(CreateCompanyCommand cmd, Long userId, String userRole) {
		companyValidator.assertCreateAccess(cmd.hubId(), userId, userRole);
		companyValidator.assertHubExists(cmd.hubId());
		companyValidator.assertCompanyNotDuplicated(cmd.hubId(), cmd.name());

		Company company =
				Company.create(cmd.hubId(), cmd.name(), cmd.type(), cmd.slackId(), cmd.address());
		Company saved = companyRepository.save(company);

		log.info(
				"업체 생성 성공 companyId={} hubId={} name={}", saved.getCompanyId(), cmd.hubId(), cmd.name());
		return CompanyResult.from(saved);
	}

	@Transactional
	@Override
	public CompanyResult updateCompany(UpdateCompanyCommand cmd, Long userId, String userRole) {
		Company company =
				companyRepository
						.findById(cmd.companyId())
						.orElseThrow(() -> new GlobalException(CompanyErrorCode.COMPANY_NOT_FOUND));

		companyValidator.assertUpdateAccess(
				company.getHubId(), company.getCompanyId(), userId, userRole);
		companyValidator.assertHubExists(company.getHubId());
		companyValidator.assertCompanyNameNotDuplicated(
				company.getHubId(), cmd.name(), company.getName());

		company.changeType(cmd.type());
		company.update(cmd.name(), cmd.address(), cmd.slackId());

		log.info(
				"업체 수정 성공 companyId={} hubId={} name={}",
				company.getCompanyId(),
				company.getHubId(),
				cmd.name());
		return CompanyResult.from(company);
	}

	@Transactional(readOnly = true)
	@Override
	public CompanyResult getCompany(UUID companyId) {
		log.debug("업체 조회 companyId={}", companyId);

		Company company =
				companyRepository
						.findById(companyId)
						.orElseThrow(() -> new GlobalException(CompanyErrorCode.COMPANY_NOT_FOUND));
		return CompanyResult.from(company);
	}

	@Transactional(readOnly = true)
	@Override
	public CommonPageResponse<CompanyResult> searchCompany(
			SearchCompanyCommand cmd, CommonPageRequest pageReq) {
		log.debug(
				"업체 검색 hubId={} name={} type={} status={} page={} size={}",
				cmd.hubId(),
				cmd.name(),
				cmd.type(),
				cmd.status(),
				pageReq.page(),
				pageReq.size());

		var page = companyRepository.search(cmd, pageReq.toPageable());

		log.debug(
				"업체 검색 결과 totalElements={} totalPages={}", page.getTotalElements(), page.getTotalPages());
		return PagingUtils.convert(page, CompanyResult::from);
	}

	@Transactional
	@Override
	public CompanyResult changeStatus(ChangeCompanyStatusCommand cmd, Long userId, String userRole) {
		Company company =
				companyRepository
						.findById(cmd.companyId())
						.orElseThrow(() -> new GlobalException(CompanyErrorCode.COMPANY_NOT_FOUND));

		companyValidator.assertUpdateAccess(
				company.getHubId(), company.getCompanyId(), userId, userRole);

		company.changeStatus(cmd.status());

		log.info(
				"업체 상태 변경 성공 companyId={} hubId={} status={}",
				company.getCompanyId(),
				company.getHubId(),
				cmd.status());
		return CompanyResult.from(company);
	}

	@Transactional
	@Override
	public void deleteCompany(UUID companyId, Long userId, String userRole) {
		Company company =
				companyRepository
						.findById(companyId)
						.orElseThrow(() -> new GlobalException(CompanyErrorCode.COMPANY_NOT_FOUND));

		companyValidator.assertDeleteAccess(company.getHubId(), userId, userRole);

		company.delete(userId);
		companyRepository.save(company);

		log.info("업체 삭제 성공 companyId={} hubId={}", company.getCompanyId(), company.getHubId());
	}
}
