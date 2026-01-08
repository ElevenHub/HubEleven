package com.hubEleven.company.application.service.impl;

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
import com.commonLib.common.exception.GlobalException;
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
		return CompanyResult.from(companyRepository.save(company));
	}

	@Transactional
	@Override
	public CompanyResult updateCompany(UpdateCompanyCommand cmd, Long userId, String userRole) {
		var company =
				companyRepository
						.findById(cmd.companyId())
						.orElseThrow(() -> new GlobalException(CompanyErrorCode.COMPANY_NOT_FOUND));

		companyValidator.assertUpdateAccess(company.getHubId(), company.getCompanyId(), userId, userRole);
		companyValidator.assertHubExists(company.getHubId());
		companyValidator.assertCompanyNameNotDuplicated(company.getHubId(), cmd.name(), company.getName());

		company.changeType(cmd.type());
		company.update(cmd.name(), cmd.address(), cmd.slackId());
		return CompanyResult.from(company);
	}

	@Transactional(readOnly = true)
	@Override
	public CompanyResult getCompany(UUID companyId) {
		var company =
				companyRepository
						.findById(companyId)
						.orElseThrow(() -> new GlobalException(CompanyErrorCode.COMPANY_NOT_FOUND));
		return CompanyResult.from(company);
	}

	@Transactional(readOnly = true)
	@Override
	public CommonPageResponse<CompanyResult> searchCompany(
			SearchCompanyCommand cmd, CommonPageRequest pageReq) {
		var page = companyRepository.search(cmd, pageReq.toPageable());
		return PagingUtils.convert(page, CompanyResult::from);
	}

	@Transactional
	@Override
	public CompanyResult changeStatus(ChangeCompanyStatusCommand cmd, Long userId, String userRole) {
		var company =
				companyRepository
						.findById(cmd.companyId())
						.orElseThrow(() -> new GlobalException(CompanyErrorCode.COMPANY_NOT_FOUND));

		companyValidator.assertUpdateAccess(company.getHubId(), company.getCompanyId(), userId, userRole);

		company.changeStatus(cmd.status());
		return CompanyResult.from(company);
	}

	@Transactional
	@Override
	public void deleteCompany(UUID companyId, Long userId, String userRole) {
		var company =
				companyRepository
						.findById(companyId)
						.orElseThrow(() -> new GlobalException(CompanyErrorCode.COMPANY_NOT_FOUND));

		companyValidator.assertDeleteAccess(company.getHubId(), userId, userRole);

		company.delete(userId);
		companyRepository.save(company);
	}
}
