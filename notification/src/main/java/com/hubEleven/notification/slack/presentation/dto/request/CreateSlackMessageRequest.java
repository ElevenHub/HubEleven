package com.hubEleven.notification.slack.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateSlackMessageRequest(
		@NotNull(message = "주문 ID는 필수 입력 항목입니다.") UUID orderId,
		@NotBlank(message = "수신자 ID는 필수 입력 항목입니다.") @Size(max = 100, message = "수신자 ID는 100자 이하여야 합니다.")
				String recipientId,
		@Size(max = 100, message = "채널은 100자 이하여야 합니다.") String channel,
		@NotBlank(message = "고객 이름은 필수 입력 항목입니다.") String customerName,
		@NotBlank(message = "고객 이메일은 필수 입력 항목입니다.") @Email String customerEmail,
		@NotNull(message = "주문 시간은 필수 입력 항목입니다.") LocalDateTime orderDateTime,
		LocalDateTime requestedArrivalDateTime,
		@NotBlank(message = "발송지 허브는 필수 입력 항목입니다.") String sourceHub,
		List<String> viaHubs,
		@NotBlank(message = "도착지 허브는 필수 입력 항목입니다.") String destinationHub,
		@NotBlank(message = "도착지 주소는 필수 입력 항목입니다.") String destinationAddress,
		String requestNote,
		@NotBlank(message = "배송 담당자 이름은 필수 입력 항목입니다.") String deliveryManagerName,
		@NotBlank(message = "배송 담당자 이메일은 필수 입력 항목입니다.") @Email String deliveryManagerEmail,
		List<Item> items,
		LocalDateTime createdAt) {
	public record Item(String name, int quantity, String note) {}
}
