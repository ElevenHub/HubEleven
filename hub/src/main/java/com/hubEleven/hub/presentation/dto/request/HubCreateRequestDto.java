package com.hubEleven.hub.presentation.dto.request;

import com.hubEleven.hub.application.command.CreateHubCommand;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record HubCreateRequestDto(
		@NotBlank(message = "허브 이름은 필수 입력 항목입니다.") @Size(max = 100, message = "허브 이름은 100자 이하여야 합니다.")
				String name,
		@NotBlank(message = "주소는 필수 입력 항목입니다.") @Size(max = 255, message = "주소는 255자 이하여야 합니다.")
				String address,
		@Min(value = -90, message = "위도는 -90 이상이어야 합니다.") @Max(value = 90, message = "위도는 90 이하여야 합니다.")
				Double latitude,
		@Min(value = -180, message = "경도는 -180 이상이어야 합니다.")
				@Max(value = 180, message = "경도는 180 이하여야 합니다.")
				Double longitude,
		@NotBlank(message = "지역 코드는 필수 입력 항목입니다.") @Size(max = 50, message = "지역 코드는 50자 이하여야 합니다.")
				String regionCode) {

	public CreateHubCommand toCommand() {
		return new CreateHubCommand(name, address, latitude, longitude, regionCode);
	}
}
