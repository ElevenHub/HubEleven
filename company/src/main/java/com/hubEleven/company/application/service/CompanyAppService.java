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
import com.hubEleven.company.domain.model.Company;
import com.hubEleven.company.domain.repository.CompanyRepository;
import com.hubEleven.company.domain.repository.CompanySearchCondition;
import com.hubEleven.company.exception.CompanyErrorCode;
import com.hubEleven.company.infrastructure.client.HubClient;
import com.hubEleven.company.infrastructure.client.UserClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyAppService {

	private final CompanyRepository companyRepository;
	private final HubClient hubClient;
	private final UserClient userClient;

	private void assertHubExists(UUID hubId) {
		try {
			hubClient.getHub(hubId);
		} catch (feign.FeignException.NotFound e) {
			throw new GlobalException(CompanyErrorCode.HUB_NOT_FOUND);
		} catch (feign.FeignException e) {
			throw new GlobalException(ErrorCode.SERVER_ERROR);
		}
	}

	/**
	 * 사용자의 hubId를 조회합니다. user-service에서 사용자 정보를 가져온 후, companyId로 DB에서 직접 company를 조회하여 hubId를 가져옵니다.
	 */
	private UUID getUserHubId(Long userId, String userRole) {
		String normalizedRole = normalizeRole(userRole);

		// MASTER는 hubId가 없을 수 있음
		if ("MASTER".equals(normalizedRole)) {
			return null;
		}

		try {
			UserClient.UserDTO user = userClient.getUser(userId, userId, userRole);
			if (user.companyId() == null) {
				log.warn("사용자의 companyId가 null입니다. userId: {}", userId);
				return null;
			}

			// DB에서 직접 company 조회하여 hubId 가져오기
			return companyRepository.findById(user.companyId()).map(Company::getHubId).orElse(null);
		} catch (Exception e) {
			log.error("사용자 hubId 조회 실패. userId: {}, error: {}", userId, e.getMessage());
			return null;
		}
	}

	/** 사용자의 companyId를 조회합니다. */
	private UUID getUserCompanyId(Long userId, String userRole) {
		try {
			UserClient.UserDTO user = userClient.getUser(userId, userId, userRole);
			return user.companyId();
		} catch (Exception e) {
			log.error("사용자 companyId 조회 실패. userId: {}, error: {}", userId, e.getMessage());
			return null;
		}
	}

	/** Role 문자열을 정규화합니다. */
	private String normalizeRole(String userRole) {
		if (userRole == null) {
			return null;
		}
		String normalized = userRole.trim().toUpperCase();
		if (normalized.startsWith("ROLE_")) {
			normalized = normalized.substring("ROLE_".length());
		}
		return normalized;
	}

	private void assertCreateAccess(UUID hubId, Long userId, String userRole) {
		if (userId == null || userRole == null) {
			throw new GlobalException(CompanyErrorCode.UNAUTHORIZED);
		}

		String normalizedRole = normalizeRole(userRole);

		if ("MASTER".equals(normalizedRole)) {
			return;
		}

		if ("HUB_MANAGER".equals(normalizedRole)) {
			UUID userHubId = getUserHubId(userId, userRole);
			if (hubId != null && hubId.equals(userHubId)) {
				return;
			}
		}

		throw new GlobalException(CompanyErrorCode.FORBIDDEN);
	}

	private void assertUpdateAccess(UUID hubId, UUID companyId, Long userId, String userRole) {
		if (userId == null || userRole == null) {
			throw new GlobalException(CompanyErrorCode.UNAUTHORIZED);
		}

		String normalizedRole = normalizeRole(userRole);

		if ("MASTER".equals(normalizedRole)) {
			return;
		}

		if ("HUB_MANAGER".equals(normalizedRole)) {
			UUID userHubId = getUserHubId(userId, userRole);
			if (hubId != null && hubId.equals(userHubId)) {
				return;
			}
		}

		if ("COMPANY_MANAGER".equals(normalizedRole)) {
			UUID userCompanyId = getUserCompanyId(userId, userRole);
			if (companyId != null && companyId.equals(userCompanyId)) {
				return;
			}
		}

		throw new GlobalException(CompanyErrorCode.FORBIDDEN);
	}

	private void assertDeleteAccess(UUID hubId, Long userId, String userRole) {
		if (userId == null || userRole == null) {
			throw new GlobalException(CompanyErrorCode.UNAUTHORIZED);
		}

		String normalizedRole = normalizeRole(userRole);

		if ("MASTER".equals(normalizedRole)) {
			return;
		}

		if ("HUB_MANAGER".equals(normalizedRole)) {
			UUID userHubId = getUserHubId(userId, userRole);
			if (hubId != null && hubId.equals(userHubId)) {
				return;
			}
		}

		throw new GlobalException(CompanyErrorCode.FORBIDDEN);
	}

	@Transactional
	public CompanyResult createCompany(CreateCompanyCommand cmd, Long userId, String userRole) {

		assertCreateAccess(cmd.hubId(), userId, userRole);
		assertHubExists(cmd.hubId());

		if (companyRepository.existsByHubIdAndName(cmd.hubId(), cmd.name())) {
			throw new GlobalException(CompanyErrorCode.COMPANY_DUPLICATED);
		}

		Company company =
				Company.create(cmd.hubId(), cmd.name(), cmd.type(), cmd.slackId(), cmd.address());
		return CompanyResult.from(companyRepository.save(company));
	}

	@Transactional
	public CompanyResult updateCompany(UpdateCompanyCommand cmd, Long userId, String userRole) {

		var company =
				companyRepository
						.findById(cmd.companyId())
						.orElseThrow(() -> new GlobalException(CompanyErrorCode.COMPANY_NOT_FOUND));

		assertUpdateAccess(company.getHubId(), company.getCompanyId(), userId, userRole);
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
	public CommonPageResponse<CompanyResult> searchCompany(
			SearchCompanyCommand cmd, CommonPageRequest pageReq) {
		var cond = new CompanySearchCondition(cmd.hubId(), cmd.name(), cmd.type(), cmd.status());
		var page = companyRepository.search(cond, pageReq.toPageable());
		return PagingUtils.convert(page, CompanyResult::from);
	}

	@Transactional
	public CompanyResult changeStatus(ChangeCompanyStatusCommand cmd, Long userId, String userRole) {

		var company =
				companyRepository
						.findById(cmd.companyId())
						.orElseThrow(() -> new GlobalException(CompanyErrorCode.COMPANY_NOT_FOUND));

		assertUpdateAccess(company.getHubId(), company.getCompanyId(), userId, userRole);

		company.changeStatus(cmd.status());
		return CompanyResult.from(company);
	}

	@Transactional
	public void deleteCompany(UUID companyId, Long userId, String userRole) {
		var company =
				companyRepository
						.findById(companyId)
						.orElseThrow(() -> new GlobalException(CompanyErrorCode.COMPANY_NOT_FOUND));

		assertDeleteAccess(company.getHubId(), userId, userRole);

		company.delete(userId);
		companyRepository.save(company);
	}
}
