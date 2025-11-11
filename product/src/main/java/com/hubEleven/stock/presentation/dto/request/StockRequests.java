package com.hubEleven.stock.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class StockRequests {

	public record Create(
			@NotNull(message = "수량은 필수 입력 항목입니다.") @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
					Long quantity,
			@NotNull(message = "상품 ID는 필수 입력 항목입니다.") UUID productId,
			@NotNull(message = "업체 ID는 필수 입력 항목입니다.") UUID companyId,
			@NotNull(message = "허브 ID는 필수 입력 항목입니다.") UUID hubId) {}

    public record Update(
            @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
                    Long quantity
    ) {}

    public record Restore(
            @NotNull(message = "상품 ID는 필수 입력 항목입니다.") UUID productId,
            @NotNull(message = "취소 수량은 필수 입력 항목입니다.") @Min(value = 1, message = "취소 수량은 1 이상이어야 합니다.")
                    Long quantity
    ) {}
}
