package com.hubEleven.order.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class OrderRequests {

	@Schema(name = "OrderCreateRequest", description = "주문 생성 요청")
	public record Create(
			@NotNull(message = "요청 업체 ID는 필수 입력 항목입니다.") UUID requestorCompanyId,
			@NotNull(message = "수령 업체 ID는 필수 입력 항목입니다.") UUID recipientCompanyId,
			@NotNull(message = "상품 ID는 필수 입력 항목입니다.") UUID productId,
			@NotNull(message = "배송 ID는 필수 입력 항목입니다.") UUID deliveryId,
			@NotNull(message = "수량은 필수 입력 항목입니다.") Long quantity,
			@Size(max = 500, message = "노트는 500자 이하여야 합니다.") String note) {}

	@Schema(name = "OrderUpdateRequest", description = "주문 수정 요청")
	public record Update(
			@Min(value = 1, message = "수량은 1개 이상이어야 합니다.") Long quantity,
			@Size(max = 500, message = "노트는 500자 이하여야 합니다.") String note) {}
}
