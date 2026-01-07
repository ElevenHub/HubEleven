package com.hubEleven.company.application.validator;

import com.commonLib.common.code.ErrorCode;
import com.commonLib.common.exception.GlobalException;
import com.hubEleven.company.domain.model.Company;
import com.hubEleven.company.domain.repository.CompanyRepository;
import com.hubEleven.company.exception.CompanyErrorCode;
import com.hubEleven.company.infrastructure.client.HubClient;
import com.hubEleven.company.infrastructure.client.UserClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompanyValidator {

	private final CompanyRepository companyRepository;
	private final HubClient hubClient;
	private final UserClient userClient;

	public void assertHubExists(UUID hubId) {
		try {
			hubClient.getHub(hubId);
		} catch (feign.FeignException.NotFound e) {
			throw new GlobalException(CompanyErrorCode.HUB_NOT_FOUND);
		} catch (feign.FeignException e) {
			throw new GlobalException(ErrorCode.SERVER_ERROR);
		}
	}

	public UUID getUserHubId(Long userId, String userRole) {
		String normalizedRole = normalizeRole(userRole);

		if ("MASTER".equals(normalizedRole)) {
			return null;
		}

		try {
			UserClient.UserDTO user = userClient.getUser(userId, userId, userRole);
			if (user.companyId() == null) {
				log.warn("사용자의 companyId가 null입니다. userId: {}", userId);
				return null;
			}

			return companyRepository.findById(user.companyId()).map(Company::getHubId).orElse(null);
		} catch (Exception e) {
			log.error("사용자 hubId 조회 실패. userId: {}, error: {}", userId, e.getMessage());
			return null;
		}
	}

	public UUID getUserCompanyId(Long userId, String userRole) {
		try {
			UserClient.UserDTO user = userClient.getUser(userId, userId, userRole);
			return user.companyId();
		} catch (Exception e) {
			log.error("사용자 companyId 조회 실패. userId: {}, error: {}", userId, e.getMessage());
			return null;
		}
	}

	public String normalizeRole(String userRole) {
		if (userRole == null) {
			return null;
		}
		String normalized = userRole.trim().toUpperCase();
		if (normalized.startsWith("ROLE_")) {
			normalized = normalized.substring("ROLE_".length());
		}
		return normalized;
	}

	public void assertCreateAccess(UUID hubId, Long userId, String userRole) {
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

	public void assertUpdateAccess(UUID hubId, UUID companyId, Long userId, String userRole) {
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

	public void assertDeleteAccess(UUID hubId, Long userId, String userRole) {
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

	public void assertCompanyNotDuplicated(UUID hubId, String companyName) {
		if (companyRepository.existsByHubIdAndName(hubId, companyName)) {
			throw new GlobalException(CompanyErrorCode.COMPANY_DUPLICATED);
		}
	}

	public void assertCompanyNameNotDuplicated(UUID hubId, String companyName, String existingName) {
		if (companyName != null && !companyName.isBlank()) {
			boolean changed = !companyName.equalsIgnoreCase(existingName);
			if (changed && companyRepository.existsByHubIdAndName(hubId, companyName)) {
				throw new GlobalException(CompanyErrorCode.COMPANY_DUPLICATED);
			}
		}
	}
}
