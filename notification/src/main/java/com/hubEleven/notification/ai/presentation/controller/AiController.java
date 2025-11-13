package com.hubEleven.notification.ai.presentation.controller;

import com.commonLib.common.response.ApiResponse;
import com.commonLib.common.response.ApiResponseEntity;
import com.hubEleven.notification.ai.application.dto.MessageGenerationResponse;
import com.hubEleven.notification.ai.application.service.AiAppService;
import com.hubEleven.notification.ai.presentation.request.MessageGenerateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Ai", description = "Gemini Ai를 활용한 배송 안내 메시지 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/ai/dispatch")
public class AiController {

	private final AiAppService aiAppService;

	@Operation(
			summary = "Ai 배송 안내 메시지 생성 API",
			description = "주문, 경로, 담당자 정보를 바탕으로 Gemini로 최종 발송 시한과 slack 메세지 내용을 생성한다.")
	@PostMapping("/messages")
	public ResponseEntity<ApiResponse<MessageGenerationResponse>> generateMessage(
			@Valid @RequestBody MessageGenerateRequest request) {
		MessageGenerationResponse response = aiAppService.generateDispatchGuidance(request.toCommand());
		return ApiResponseEntity.success(response);
	}
}
