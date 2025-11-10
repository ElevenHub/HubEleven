package com.hubEleven.notification.ai.presentation.request;

import com.hubEleven.notification.ai.application.dto.MessageGenerationRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MessagePlanRequest(
		@NotNull UUID orderId,
		@NotBlank String customerName,
		@Email String customerEmail,
		@NotNull @PastOrPresent LocalDateTime orderDateTime,
		@NotNull @Future LocalDateTime requestedArrivalDateTime,
		@NotBlank String sourceHub,
		@Valid List<String> viaHubs,
		@NotBlank String destinationHub,
		@NotBlank String destinationAddress,
		@Size(max = 1000) String requestNote,
		@NotBlank String deliveryManagerName,
		@NotBlank @Email String deliveryManagerEmail,
		@NotEmpty @Valid List<Item> items) {

	public MessageGenerationRequest toCommand() {
		return MessageGenerationRequest.of(
				orderId,
				customerName,
				customerEmail,
				orderDateTime,
				requestedArrivalDateTime,
				sourceHub,
				viaHubs == null ? List.of() : viaHubs,
				destinationHub,
				destinationAddress,
				requestNote,
				deliveryManagerName,
				deliveryManagerEmail,
				items.stream()
						.map(
								item -> MessageGenerationRequest.Item.of(item.name(), item.quantity(), item.note()))
						.toList());
	}

	public record Item(
			@NotBlank @Size(max = 200) String name,
			@Positive int quantity,
			@Size(max = 500) String note) {}
}
