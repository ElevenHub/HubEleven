package com.hubEleven.notification.ai.presentation.dto.request;

import com.hubEleven.notification.ai.application.command.GenerateDispatchCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GenerateMessageRequest(
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
		@Email String deliveryManagerEmail,
		@NotEmpty @Valid List<Item> items) {
	public GenerateDispatchCommand toCommand() {
		List<GenerateDispatchCommand.Item> mapped =
				items.stream()
						.map(i -> new GenerateDispatchCommand.Item(i.name(), i.quantity(), i.note()))
						.toList();

		return new GenerateDispatchCommand(
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
				mapped);
	}

	public record Item(
			@NotBlank @Size(max = 200) String name,
			@Positive int quantity,
			@Size(max = 500) String note) {}
}
