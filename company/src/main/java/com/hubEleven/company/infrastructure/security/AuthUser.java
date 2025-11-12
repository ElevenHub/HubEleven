package com.hubEleven.company.infrastructure.security;

import java.util.UUID;

public record AuthUser(Long userId, Role role, UUID hubId, UUID companyId) {}
