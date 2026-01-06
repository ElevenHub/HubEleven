package com.hubEleven.company.application.service;

import com.commonLib.common.code.ErrorCode;
import com.commonLib.common.exception.GlobalException;
import com.commonLib.common.request.CommonPageRequest;
import com.commonLib.common.response.CommonPageResponse;
import com.commonLib.common.utils.PagingUtils;
import com.hubEleven.company.application.dto.response.CompanyResult;
import com.hubEleven.company.exception.CompanyErrorCode;
import com.hubEleven.company.domain.model.Company;
import com.hubEleven.company.domain.model.CompanyStatus;
import com.hubEleven.company.domain.model.CompanyType;
import com.hubEleven.company.domain.repository.CompanyRepository;
import com.hubEleven.company.domain.repository.CompanySearchCondition;
import com.hubEleven.company.infrastructure.client.HubClient;
import com.hubEleven.company.infrastructure.security.AuthUser;
import com.hubEleven.company.infrastructure.security.AuthUserContext;
import com.hubEleven.company.infrastructure.security.Role;
import com.hubEleven.company.presentation.dto.request.CompanyRequests;
import java.util.Optional;
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
	public CompanyResult createCompany(CompanyRequests.Create req) {
		assertCreateAccess(req.hubId());
		assertHubExists(req.hubId());

		if (companyRepository.existsByHubIdAndName(req.hubId(), req.name())) {
			throw new GlobalException(CompanyErrorCode.COMPANY_DUPLICATED);
		}

		Company company =
				Company.create(req.hubId(), req.name(), req.type(), req.slackId(), req.address());
		return CompanyResult.from(companyRepository.save(company));
	}

	@Transactional
	public CompanyResult updateCompany(UUID companyId, CompanyRequests.Update req) {
		var company =
				companyRepository
						.findById(companyId)
						.orElseThrow(() -> new GlobalException(CompanyErrorCode.COMPANY_NOT_FOUND));

		assertUpdateAccess(company.getHubId(), company.getCompanyId());
		assertHubExists(company.getHubId());

		if (req.name() != null && !req.name().isBlank()) {
			boolean changed = !req.name().equalsIgnoreCase(company.getName());
			if (changed && companyRepository.existsByHubIdAndName(company.getHubId(), req.name())) {
				throw new GlobalException(CompanyErrorCode.COMPANY_DUPLICATED);
			}
		}

		company.changeType(req.type());
		company.update(req.name(), req.address(), req.slackId());
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
	public CommonPageResponse<CompanyResult> searchCompany(
			Optional<UUID> hubId,
			Optional<String> name,
			Optional<CompanyType> type,
			Optional<CompanyStatus> status,
			CommonPageRequest pageReq) {

		var cond =
				new CompanySearchCondition(
						hubId.orElse(null), name.orElse(null), type.orElse(null), status.orElse(null));
		var page = companyRepository.search(cond, pageReq.toPageable());
		return PagingUtils.convert(page, CompanyResult::from);
	}

	@Transactional
	public CompanyResult changeStatus(UUID companyId, String rawStatus) {
		var company =
				companyRepository
						.findById(companyId)
						.orElseThrow(() -> new GlobalException(CompanyErrorCode.COMPANY_NOT_FOUND));

		assertUpdateAccess(company.getHubId(), company.getCompanyId());

		CompanyStatus newStatus;
		try {
			newStatus = CompanyStatus.valueOf(rawStatus.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new GlobalException(ErrorCode.SERVER_ERROR);
		}

		company.changeStatus(newStatus);
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
