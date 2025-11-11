package com.hubEleven.user.domain.model;

import com.commonLib.common.model.BaseEntity;
import com.hubEleven.user.domain.vo.Role;
import com.hubEleven.user.domain.vo.SignStatus;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "p_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false)
	private Long id;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "slack_id", nullable = false)
	private String slackId;

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@Column(name = "status", nullable = false)
	private SignStatus status;

	@Column(name = "role", nullable = false)
	private Role role;

	@Column(name = "company_id", nullable = false)
	private UUID companyId;

	public static User create(
			String username,
			String password,
			String name,
			String slackId,
			String phoneNumber,
			Role role,
			UUID companyId) {
		User user = new User();
		user.username = username;
		user.password = password;
		user.name = name;
		user.slackId = slackId;
		user.phoneNumber = phoneNumber;
		user.status = SignStatus.PENDING;
		user.role = role;
		user.companyId = companyId;

		return user;
	}

	public void updateStatus(SignStatus newStatus) {
		this.status = newStatus;
	}

	public void updateInfo(
			String name, String slackId, String phoneNumber, Role role, UUID companyId) {
		this.name = name;
		this.slackId = slackId;
		this.phoneNumber = phoneNumber;
		this.role = role;
		this.companyId = companyId;
	}
}
