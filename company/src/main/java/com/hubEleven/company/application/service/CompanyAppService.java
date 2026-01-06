package com.hubEleven.company.application.service;

import com.commonLib.common.code.ErrorCode;
import com.commonLib.common.exception.GlobalException;
import com.commonLib.common.request.CommonPageRequest;
import com.commonLib.common.response.CommonPageResponse;
import com.commonLib.common.utils.PagingUtils;
import com.hubEleven.company.application.command.ChangeCompanyStatusCommand;
import com.hubEleven.company.application.command.CreateCompanyCommand;
import com.hubEleven.company.application.command.SearchCompanyCommand;
import com.hubEleven.company.application.command.UpdateCompanyCommand;
import com.hubEleven.company.application.dto.response.CompanyResult;
import com.hubEleven.company.exception.CompanyErrorCode;
import com.hubEleven.company.domain.model.Company;
import com.hubEleven.company.domain.repository.CompanyRepository;
import com.hubEleven.company.domain.repository.CompanySearchCondition;
import com.hubEleven.company.infrastructure.client.HubClient;
import com.hubEleven.company.infrastructure.security.AuthUser;
import com.hubEleven.company.infrastructure.security.AuthUserContext;
import com.hubEleven.company.infrastructure.security.Role;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyAppService {

	private final CompanyRepository companyRepository;
	private final HubClient hubClient;

	private AuthUser currentUser() {
		return AuthUserContext.get();
	}

	private void assertHubExists(UUID hubId) {
		try {
			hubClient.getHub(hubId);
		} catch (feign.FeignException.NotFound e) {
			throw new GlobalException(CompanyErrorCode.HUB_NOT_FOUND);
		} catch (feign.FeignException e) {
			throw new GlobalException(ErrorCode.SERVER_ERROR);
		}
	}

	private void assertCreateAccess(UUID hubId) {
		AuthUser user = currentUser();
		if (user == null) throw new GlobalException(CompanyErrorCode.UNAUTHORIZED);

		if (user.role() == Role.MASTER) return;
		if (user.role() == Role.HUB_MANAGER && hubId != null && hubId.equals(user.hubId())) return;

		throw new GlobalException(CompanyErrorCode.FORBIDDEN);
	}

	private void assertUpdateAccess(UUID hubId, UUID companyId) {
		AuthUser user = currentUser();
		if (user == null) throw new GlobalException(CompanyErrorCode.UNAUTHORIZED);

		if (user.role() == Role.MASTER) return;
		if (user.role() == Role.HUB_MANAGER && hubId != null && hubId.equals(user.hubId())) return;
		if (user.role() == Role.COMPANY_MANAGER
				&& companyId != null
				&& companyId.equals(user.companyId())) return;

		throw new GlobalException(CompanyErrorCode.FORBIDDEN);
	}

	private void assertDeleteAccess(UUID hubId) {
		AuthUser user = currentUser();
		if (user == null) throw new GlobalException(CompanyErrorCode.UNAUTHORIZED);

		if (user.role() == Role.MASTER) return;
		if (user.role() == Role.HUB_MANAGER && hubId != null && hubId.equals(user.hubId())) return;

		throw new GlobalException(CompanyErrorCode.FORBIDDEN);
	}

	@Transactional
	public CompanyResult createCompany(CreateCompanyCommand cmd) {
		assertCreateAccess(cmd.hubId());
		assertHubExists(cmd.hubId());

		if (companyRepository.existsByHubIdAndName(cmd.hubId(), cmd.name())) {
			throw new GlobalException(CompanyErrorCode.COMPANY_DUPLICATED);
		}

		Company company = Company.create(cmd.hubId(), cmd.name(), cmd.type(), cmd.slackId(), cmd.address());
		return CompanyResult.from(companyRepository.save(company));
	}

	@Transactional
	public CompanyResult updateCompany(UpdateCompanyCommand cmd) {
		var company = companyRepository.findById(cmd.companyId())
				.orElseThrow(() -> new GlobalException(CompanyErrorCode.COMPANY_NOT_FOUND));

		assertUpdateAccess(company.getHubId(), company.getCompanyId());
		assertHubExists(company.getHubId());

		if (cmd.name() != null && !cmd.name().isBlank()) {
			boolean changed = !cmd.name().equalsIgnoreCase(company.getName());
			if (changed && companyRepository.existsByHubIdAndName(company.getHubId(), cmd.name())) {
				throw new GlobalException(CompanyErrorCode.COMPANY_DUPLICATED);
			}
		}

		company.changeType(cmd.type());
		company.update(cmd.name(), cmd.address(), cmd.slackId());
		return CompanyResult.from(company);
	}

	@Transactional(readOnly = true)
	public CompanyResult getCompany(UUID companyId) {
		var company =
				companyRepository
						.findById(companyId)
						.orElseThrow(() -> new GlobalException(CompanyErrorCode.COMPANY_NOT_FOUND));
		return CompanyResult.from(company);
	}

	@Transactional(readOnly = true)
	public CommonPageResponse<CompanyResult> findCompanyList(CommonPageRequest pageReq) {
		var page =
				companyRepository.search(
						new CompanySearchCondition(null, null, null, null), pageReq.toPageable());
		return PagingUtils.convert(page, CompanyResult::from);
	}

	@Transactional(readOnly = true)
	public CommonPageResponse<CompanyResult> searchCompany(SearchCompanyCommand cmd, CommonPageRequest pageReq) {
		var cond = new CompanySearchCondition(cmd.hubId(), cmd.name(), cmd.type(), cmd.status());
		var page = companyRepository.search(cond, pageReq.toPageable());
		return PagingUtils.convert(page, CompanyResult::from);
	}

	@Transactional
	public CompanyResult changeStatus(ChangeCompanyStatusCommand cmd) {
		var company = companyRepository.findById(cmd.companyId())
				.orElseThrow(() -> new GlobalException(CompanyErrorCode.COMPANY_NOT_FOUND));

		assertUpdateAccess(company.getHubId(), company.getCompanyId());

		company.changeStatus(cmd.status());
		return CompanyResult.from(company);
	}


	@Transactional
	public void deleteCompany(UUID companyId) {
		var company =
				companyRepository
						.findById(companyId)
						.orElseThrow(() -> new GlobalException(CompanyErrorCode.COMPANY_NOT_FOUND));

		assertDeleteAccess(company.getHubId());

		company.delete(currentUser().userId());
		companyRepository.save(company);
	}
}
