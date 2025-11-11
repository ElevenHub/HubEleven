package com.hubEleven.user.config;

import com.hubEleven.user.domain.model.User;
import com.hubEleven.user.domain.repository.UserRepository;
import com.hubEleven.user.domain.vo.Role;
import com.hubEleven.user.domain.vo.SignStatus;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) {
		// 이미 마스터 계정이 있으면 스킵
		if (userRepository.findByUsername("master01").isPresent()) {
			log.info("Master account already exists. Skipping initialization.");
			return;
		}

		log.info("Creating initial master account...");

		// Username: master01, Password: Password1!
		String encodedPassword = passwordEncoder.encode("Password1!");

		User master =
				User.create(
						"master01",
						encodedPassword,
						"마스터관리자",
						"U01MASTER001",
						"010-1234-5678",
						Role.MASTER,
						UUID.fromString("550e8400-e29b-41d4-a716-446655440001"));
		master.updateStatus(SignStatus.APPROVED);
		userRepository.save(master);

		log.info("Master account created successfully. Username: master01, Password: Password1!");
	}
}
