package com.hubEleven.notification.ai.presentation.controller;

import com.commonLib.common.response.ApiResponse;
import com.hubEleven.notification.ai.application.dto.response.GenerateMessageResponse;
import com.hubEleven.notification.ai.application.service.GenerateDispatchService;
import com.hubEleven.notification.ai.presentation.dto.request.GenerateMessageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Ai", description = "Gemini Ai를 활용한 배송 안내 메시지 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/ai/dispatch")
public class AiController {

	private final GenerateDispatchService generateDispatchService;

	@Operation(
			summary = "Ai 배송 안내 메시지 생성 API",
			description = "주문/경로/담당자 정보를 바탕으로 Gemini로 최종 발송 시한과 Slack 메시지 내용을 생성한다.")
	@PostMapping("/messages")
	public ResponseEntity<ApiResponse<GenerateMessageResponse>> generateMessage(
			@Valid @RequestBody GenerateMessageRequest request) {
		GenerateMessageResponse response = generateDispatchService.generate(request.toCommand());
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
	}
}
