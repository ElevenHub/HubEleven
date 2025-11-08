package com.hubEleven.hub.presentation.dto.response;

import java.util.UUID;

public record HubDeleteResponseDto(UUID hubId, boolean deleted) {}
