package com.hubEleven.company.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserClient {
    
    @GetMapping("/v1/user/{id}")
    UserDTO getUser(
            @PathVariable("id") Long id,
            @RequestHeader("X-User-Id") Long requestUserId,
            @RequestHeader("X-User-Role") String requestUserRole);
    
    record UserDTO(
            Long userId,
            String username,
            String name,
            String slackId,
            String phoneNumber,
            String role,
            String status,
            java.util.UUID companyId
    ) {}
}
