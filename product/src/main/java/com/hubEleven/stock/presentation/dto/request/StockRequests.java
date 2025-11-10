package com.hubEleven.stock.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class StockRequests {

	public record Create(
			@NotNull(message = "수량은 필수 입력 항목입니다.") @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
					Integer quantity,
			@NotNull(message = "상품 ID는 필수 입력 항목입니다.") UUID productId,
			@NotNull(message = "업체 ID는 필수 입력 항목입니다.") UUID companyId,
			@NotNull(message = "허브 ID는 필수 입력 항목입니다.") UUID hubId) {}
}
