package com.hubEleven.notification.ai.infrastructure.client.dto.response;

import com.commonLib.common.exception.GlobalException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hubEleven.notification.ai.exception.NotificationErrorCode;
import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeminiResponse(List<Candidate> candidates) {

	public String primaryText() {
		if (candidates == null || candidates.isEmpty()) {
			throw new GlobalException(NotificationErrorCode.AI_RESPONSE_PARSE_FAIL);
		}

		Optional<String> text =
				candidates.stream()
						.findFirst()
						.flatMap(
								c -> {
									Content content = c.content();
									if (content == null || content.parts() == null || content.parts().isEmpty())
										return Optional.empty();
									return content.parts().stream()
											.map(Part::text)
											.filter(t -> t != null && !t.isBlank())
											.findFirst();
								});

		return text.orElseThrow(
				() -> new GlobalException(NotificationErrorCode.AI_RESPONSE_PARSE_FAIL));
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Candidate(Content content) {}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Content(String role, List<Part> parts) {}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Part(String text) {}
}
