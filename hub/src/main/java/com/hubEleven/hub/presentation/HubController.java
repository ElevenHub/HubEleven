package com.hubEleven.hub.presentation;

import com.hubEleven.hub.application.command.CreateHubCommand;
import com.hubEleven.hub.application.command.DeleteHubCommand;
import com.hubEleven.hub.application.command.UpdateHubCommand;
import com.hubEleven.hub.application.dto.HubListResult;
import com.hubEleven.hub.application.dto.HubResult;
import com.hubEleven.hub.application.service.HubService;
import com.hubEleven.hub.presentation.dto.request.HubCreateRequestDto;
import com.hubEleven.hub.presentation.dto.request.HubUpdateRequestDto;
import com.hubEleven.hub.presentation.dto.response.HubDeleteResponseDto;
import com.hubEleven.hub.presentation.dto.response.HubListResponseDto;
import com.hubEleven.hub.presentation.dto.response.HubResponseDto;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/hubs")
public class HubController {

	private final HubService hubService;

	public HubController(HubService hubService) {
		this.hubService = hubService;
	}

	@PostMapping
	public HubResponseDto createHub(@Valid @RequestBody HubCreateRequestDto request) {
		CreateHubCommand command = request.toCommand();
		HubResult result = hubService.createHub(command);
		return HubResponseDto.from(result);
	}

	@PatchMapping("/{hubId}")
	public HubResponseDto updateHub(
			@PathVariable UUID hubId, @Valid @RequestBody HubUpdateRequestDto request) {
		UpdateHubCommand command = request.toCommand(hubId);
		HubResult result = hubService.updateHub(command);
		return HubResponseDto.from(result);
	}

	@DeleteMapping("/{hubId}")
	public HubDeleteResponseDto deleteHub(@PathVariable UUID hubId) {
		hubService.deleteHub(new DeleteHubCommand(hubId));
		return new HubDeleteResponseDto(hubId, true);
	}

	@GetMapping("/{hubId}")
	public HubResponseDto getHub(@PathVariable UUID hubId) {
		HubResult result = hubService.getHub(hubId);
		return HubResponseDto.from(result);
	}

	@GetMapping
	public HubListResponseDto getHubs() {
		HubListResult result = hubService.getHubs();
		List<HubResponseDto> responses = HubResponseDto.fromList(result.hubs());
		return new HubListResponseDto(responses);
	}
}
