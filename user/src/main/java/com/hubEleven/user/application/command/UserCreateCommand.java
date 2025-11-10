package com.hubEleven.user.application.command;

import com.hubEleven.user.domain.vo.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;

public record UserCreateCommand(
		@NotBlank(message = "유저 이름은 필수입니다.")
				@Pattern(regexp = "^[a-z0-9]{4,10}$", message = "유저이름은 4~10자이며, 소문자/숫자로 구성되어야 합니다.")
				String username,
		@NotBlank(message = "비밀번호는 필수입니다.")
				@org.hibernate.validator.constraints.Length(
						min = 8,
						max = 15,
						message = "비밀번호는 8~15자이어야 합니다.")
				String password,
		@NotBlank(message = "이름은 필수입니다.") String name,
		@NotBlank(message = "슬랙 ID는 필수입니다.") String slackId,
		@NotBlank(message = "휴대폰번호는 필수입니다.") String phoneNumber,
		@NotNull(message = "권한은 필수입니다.") Role role,
		@NotNull(message = "업체 id는 필수입니다.") UUID companyId) {}
