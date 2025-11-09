package com.hubEleven.product.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class ProductRequests {

	public record Create(
			@NotBlank(message = "상품명은 필수 입력 항목입니다.") @Size(max = 150, message = "상품명은 150자 이하여야 합니다.")
					String name,
			@NotNull(message = "업체 ID는 필수 입력 항목입니다.") UUID companyId,
			@NotNull(message = "허브 ID는 필수 입력 항목입니다.") UUID hubId) {}

	public record Update(
			@NotBlank(message = "상품명은 필수 입력 항목입니다.") @Size(max = 150, message = "상품명은 150자 이하여야 합니다.")
					String name) {}
}
