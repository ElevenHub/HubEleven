package com.hubEleven.company.presentation.request;

import com.hubEleven.company.domain.model.CompanyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class CompanyRequests {

	public record Create(
			@NotNull UUID hubId,
			@NotBlank @Size(max = 150) String name,
			@NotNull CompanyType type,
			@Size(max = 100) String slackId,
			@Size(max = 300) String address) {}

	public record Update(
			@Size(max = 150) String name,
			CompanyType type,
			@Size(max = 100) String slackId,
			@Size(max = 300) String address) {}

	public record StatusChange(@NotNull String status) {}
}
