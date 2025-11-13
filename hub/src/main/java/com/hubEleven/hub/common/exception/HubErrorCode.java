package com.hubEleven.hub.common.exception;

import com.commonLib.common.code.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum HubErrorCode implements StatusCode {
	DUPLICATE_HUB_NAME(HttpStatus.BAD_REQUEST, "이미 동일한 이름의 허브가 존재합니다."),
	HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "허브를 찾을 수 없습니다."),
	GEOCODING_FAILED(HttpStatus.BAD_REQUEST, "주소로부터 좌표를 찾을 수 없습니다."),
	KAKAO_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 API 호출 중 오류가 발생했습니다."),
	ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "경로를 찾을 수 없습니다."),
	MASTER_ONLY(HttpStatus.FORBIDDEN, "MASTER 권한이 있어야 수행할 수 있습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public String getName() {
		return this.name();
	}
}
